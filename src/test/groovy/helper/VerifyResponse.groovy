package helper

import rest.service.loanService.enums.DebtType
import rest.service.loanService.enums.LoanActionName
import rest.service.loanService.enums.LoanStatus
import rest.service.loanService.enums.OperationType
import rest.service.loanService.response.*
import rest.service.loanService.response.interestDiscount.GetInterestDiscHistResponse
import rest.service.loanService.response.interestDiscount.GetInterestDiscountResponse

import java.util.stream.Collectors

import static Wrappers.getNotificationByStatus
import static dataBaseService.ManipulationDao.*
import static java.util.Objects.nonNull
import static rest.service.loanService.controllers.BalanceController.getBalance
import static rest.service.loanService.controllers.CommissionController.getCommissionsByCustomer
import static rest.service.loanService.controllers.CommissionController.getCommissionsByLoan
import static rest.service.loanService.controllers.DebtsController.getCurrentDebsOnDate
import static rest.service.loanService.controllers.DebtsController.getCurrentDebtsByLoan
import static rest.service.loanService.controllers.InterestDiscountController.findInterestDiscount
import static rest.service.loanService.controllers.InterestDiscountController.getInterestDiscountHistory
import static rest.service.loanService.controllers.LoanController.*
import static rest.service.loanService.controllers.LoanDateTimeController.getServerDate
import static rest.service.loanService.controllers.LoanDateTimeController.getServerDateTime
import static rest.service.loanService.controllers.LoanHistoryController.getLoanHistoryByLoanId
import static rest.service.loanService.controllers.ScoringController.getCustomerScoringInfo
import static rest.service.loanService.controllers.TestController.getNotificationData
import static rest.service.loanService.enums.CommissionState.REVOKED
import static rest.service.loanService.enums.LoanActionName.*
import static rest.service.loanService.enums.LoanStatus.*
import static rest.service.loanService.enums.OperationType.*
import static rest.service.loanService.enums.ProlongationType.RU_LATE
import static rest.service.loanService.enums.ProlongationType.RU_STANDARD
import static rest.service.utils.ReqHelper.*
import static utils.StringUtils.snakeCaseToCamelCase

class VerifyResponse {

    private static final List<String> LOAN_STATES_WITH_NO_DEBTS =
            List.of(CLOSED.name(), CANCELLED.name(), BANKRUPT.name())

    private static final List<String> LOAN_STATES_WITHOUT_DAYS_PAST_DUE =
            List.of(CLOSED.name(), CANCELLED.name(), BANKRUPT.name(), SOLD.name())

    static boolean checkLoan(Map loanBody, String status) {
        GetLoanResponse response = waitResponse({ getLoanById(loanBody.id) },
                { it.currentStatus == status.toLowerCase() }) as GetLoanResponse

        Map tariff = getTariffFromDb(loanBody.amount, loanBody.loanTerm, loanBody.loanTypeName)

        List prolongation = getProlongationDb(loanBody.id)
        String prolongStartDate = null
        if (!prolongation.isEmpty()) {
            prolongStartDate = prolongation.last().FROM_TIME.toString().split(" ")[0]
        }

        assert response.id == loanBody.id
        assert response.loanTypeName == loanBody.loanTypeName
        assert response.customerId == loanBody.customerId
        assert response.loanDate == loanBody.loanDate
        assert response.amount == loanBody.amount
        assert response.loanTerm == loanBody.loanTerm
        assert response.currentStatus == status.toLowerCase()

        assert response.dueDate == (nonNull(prolongStartDate) ? //если были пролонгации то dueDate сдвигается
                calcDueDate(prolongStartDate, loanBody.loanTerm) : calcDueDate(loanBody.loanDate, loanBody.loanTerm))

        assert response.daysPastDue == (LOAN_STATES_WITHOUT_DAYS_PAST_DUE.contains(status) ?
                null : getElapsedDays(response.dueDate, getServerDate()))

        assert response.tariff.id == tariff.id
        assert response.tariff.loanTerm == loanBody.loanTerm as Long
        assert response.tariff.amount == loanBody.amount
        assert response.tariff.paymentAmount == calcPaymentAmount(loanBody.amount, 1, loanBody.loanTerm)
        assert response.tariff.interest == tariff.interest
        assert response.tariff.fromDate == tariff.from_date as String
        assert response.tariff.toDate == tariff.to_date as String
        assert response.tariff.yearlyInterestRate == 365 // todo: мб неправильно
        assert response.tariff.dailyInterestRate == tariff.interest
        true
    }

    static boolean checkTariff(Map loanBody) {
        GetLoanResponse getLoanResponse = getLoansByCustomer(loanBody.customerId).first()
        Map tariff = getTariffFromDb(loanBody.amount, loanBody.loanTerm, loanBody.loanTypeName)
        assert getLoanResponse.tariff.id == tariff.id
        assert getLoanResponse.tariff.loanTerm == loanBody.loanTerm as Long
        assert getLoanResponse.tariff.amount == loanBody.amount
        assert getLoanResponse.tariff.paymentAmount == calcPaymentAmount(loanBody.amount, 1, loanBody.loanTerm)
        assert getLoanResponse.tariff.interest == tariff.interest
        assert getLoanResponse.tariff.fromDate == tariff.from_date as String
        assert getLoanResponse.tariff.toDate == tariff.to_date as String
        assert getLoanResponse.tariff.yearlyInterestRate == 365
        assert getLoanResponse.tariff.dailyInterestRate == tariff.interest
        true
    }

    static boolean checkPayment(GetPaymentsResponse response, Map paymentReq, Map loanReq) {

        assert response.externalId == paymentReq.externalId
        assert response.loanId == paymentReq.loanId
        assert response.amount == paymentReq.amount
        assert response.remainAmount == 0
        assert response.reservedAmount == 0
        assert response.categorization[0].paymentDate == calcDueDate(loanReq.loanDate, loanReq.loanTerm)
        assert response.categorization[0].paymentCategorizationDate == paymentReq.paymentTime.split('T')[0]
//        assert response.categorization[0].issuedTime.contains(paymentReq.paymentTime as String)
        assert response.categorization[0].loanId == loanReq.id
        assert response.categorization[0].debts.mainDebt == 200000
        assert response.categorization[0].debts.interest == 70000
        assert response.categorization[0].debts.overdueInterest == 30000
        assert response.operationType == OperationType.PAYMENT.name()
        assert response.paymentTime == paymentReq.paymentTime
        true
    }

    static boolean checkLastNotification(def balanceDebt, Map loanBody, String onDate, LoanStatus status) {
        NotificationResponse notificationResponse = getNotificationByStatus(loanBody.id, onDate, status.name().toLowerCase())

        assert notificationResponse.action == "loanUpdated"
        assert notificationResponse.data.debts.mainDebt == loanBody.amount
        assert notificationResponse.data.debts.interest == calcInterest(balanceDebt, loanBody)
        assert notificationResponse.data.debts.overdueInterest == calcOverdueInterest(balanceDebt, loanBody)
        assert notificationResponse.data.debts.fine == calculateFine(balanceDebt, loanBody)
        assert notificationResponse.data.currentStatus == status.name().toLowerCase()
        true
    }

    static boolean checkDebts(def balanceDebt, Map loanBody, boolean isOverdue) {
        List<GetDebtsResponse> getDebtsResponse = waitResponse(
                { getCurrentDebtsByLoan(loanBody.id) }, { !it.isEmpty() }) as List<GetDebtsResponse>

        Map debts = getDebtsFromDB(loanBody.id)

        assert getDebtsResponse[0].debts.mainDebt == loanBody.amount
        assert getDebtsResponse[0].debts.interest == calcInterest(balanceDebt, loanBody)

        if (isOverdue) {
            assert getDebtsResponse[0].debts.overdueInterest == calcOverdueInterest(balanceDebt, loanBody)
            assert getDebtsResponse[0].debts.fine == calculateFine(loanBody.amount, loanBody)
        } else {
            assert getDebtsResponse[0].debts.overdueInterest == null
            assert getDebtsResponse[0].debts.fine == null
        }

        assert debts.main_debt == loanBody.amount
        assert debts.interest == calcInterest(balanceDebt, loanBody)
        assert debts.overdue_interest == calcOverdueInterest(balanceDebt, loanBody)
        assert debts.fine == calculateFine(loanBody.amount, loanBody)

        true
    }

    static boolean checkDebtsIsEmpty(def loanId) {
        List<GetDebtsResponse> getDebtsResponse = getCurrentDebtsByLoan(loanId) as List<GetDebtsResponse>
        List debtsInBd = getDebtsData(loanId)

        assert getDebtsResponse == []

        debtsInBd.forEach({
            assert it.value == 0
        })
        true
    }

    @SuppressWarnings('GroovyPointlessBoolean')
    static boolean checkInterestDiscount(Map loanBody,
                                         def discountType,
                                         def discountValue,
                                         def fromDate = getServerDate(),
                                         boolean isActive = true,
                                         boolean isRevoked = false,
                                         def toDate = '2200-01-01') {
        GetInterestDiscountResponse interestDiscountResponse = findInterestDiscount(loanBody.id)
                .find({ it.type == discountType && it.revoked == isRevoked })
        Map interestDiscountInDb = getInterestDiscountDB(loanBody.id)
                .find({ it.type == discountType && it.revoked == isRevoked })

        assert interestDiscountResponse.active == isActive
        assert interestDiscountResponse.type == discountType
        assert interestDiscountResponse.value == discountValue
        assert interestDiscountResponse.loanId == loanBody.id
        assert interestDiscountResponse.customerId == loanBody.customerId
        assert interestDiscountResponse.fromDate == fromDate
        assert interestDiscountResponse.toDate == toDate
        assert interestDiscountResponse.revoked == isRevoked

        assert interestDiscountInDb.type == discountType
        true
    }

    static boolean checkInterestDiscountHistory(Map loanBody,
                                                def discountType,
                                                def discountValue,
                                                String reason,
                                                def fromDate = getServerDate(),
                                                boolean isRevoked = false) {
        GetInterestDiscHistResponse interestDiscountHistory = getInterestDiscountHistory(loanBody.id)
                .find({ it.type == discountType && it.revoked == isRevoked && it.date == fromDate })
        assert interestDiscountHistory.date == fromDate
        assert interestDiscountHistory.interestDiscount == discountValue
        assert interestDiscountHistory.newInterestRate == 1 - discountValue
        assert interestDiscountHistory.type == discountType
        assert interestDiscountHistory.revoked == isRevoked
        assert interestDiscountHistory.reason == reason
        true
    }

    static boolean checkPaymentIsCanceled(Map paymentBody) {
        List<Map> payments = waitResponse({ getPaymentData(paymentBody.loanId, paymentBody.externalId) },
                { it.size() == 2 }) as List<Map>
        assert (payments.first().revoked_by_id != null) || (payments.last().revoked_by_id != null)
        true
    }

    static boolean checkBankruptcyData(Map loanBody, boolean paymentsIsCanceled = true) {
        NotificationResponse notificationResponse = getNotificationByStatus(loanBody.id, getServerDate(), BANKRUPT.name().toLowerCase())
        GetLoanHistoryResponse loanHistoryResponse = getLoanHistoryByLoanId(loanBody.id).last()

        if (paymentsIsCanceled) {
            assert notificationResponse.data.payments == []
        } else {
            assert !notificationResponse.data.payments.isEmpty()
        }

        assert notificationResponse.data.debts.mainDebt == 0
        assert notificationResponse.data.debts.interest == 0
        assert loanHistoryResponse.actionName == BANKRUPTCY.name().toLowerCase()
        assert loanHistoryResponse.statusName == BANKRUPT.name().toLowerCase()
        true
    }

    static boolean checkFraudsterData(Map loanBody) {
        NotificationResponse notificationResponse = getNotificationByStatus(loanBody.id, getServerDate(), FRAUDSTER.name().toLowerCase())
        GetLoanHistoryResponse loanHistoryResponse = getLoanHistoryByLoanId(loanBody.id).last()

        assert notificationResponse.data.debts.mainDebt == loanBody.amount
        assert notificationResponse.data.currentStatus == FRAUDSTER.name().toLowerCase()
        assert loanHistoryResponse.actionName == STOP_CALCULATION_BY_FRAUD.name().toLowerCase()
        assert loanHistoryResponse.statusName == FRAUDSTER.name().toLowerCase()

        true
    }

    static boolean checkResumeLoanCalculationData(Map loanBody, LoanStatus status, LoanActionName action) {
        NotificationResponse notificationResponse = getNotificationByStatus(loanBody.id, getServerDate(), status.name().toLowerCase())
        List<GetLoanHistoryResponse> loanHistoryResponse = getLoanHistoryByLoanId(loanBody.id)
        Map loanHistoryFromDb = getLonHistoryFromDb(loanBody.id).find({ it.loan_status == FRAUDSTER.name() })

        assert notificationResponse.data.debts.mainDebt == loanBody.amount
        assert notificationResponse.data.currentStatus == status.name().toLowerCase()
        assert loanHistoryResponse.last().actionName == action.name().toLowerCase()
        assert loanHistoryResponse.last().statusName == status.name().toLowerCase()

        // поверяем ревок фрода
        assert loanHistoryFromDb.loan_status == FRAUDSTER.name()
        assert loanHistoryFromDb.loan_action == STOP_CALCULATION_BY_FRAUD.name()
        assert loanHistoryFromDb.revoked_by_id != null

        true
    }

    static boolean checkFraudsterLoanClosingData(Map loanBody) {
        NotificationResponse notificationResponse = getNotificationByStatus(loanBody.id, getServerDate(), CLOSED.name().toLowerCase())
        List<GetLoanHistoryResponse> loanHistoryResponse = getLoanHistoryByLoanId(loanBody.id)
        Map loanHistoryFromDb = getLonHistoryFromDb(loanBody.id).last()
        List<GetDebtsResponse> debtsResponse = getCurrentDebsOnDate(loanBody.id, getServerDateTime()) as List<GetDebtsResponse>
        Map debtsFromDb = getDebtsFromDB(loanBody.id)

        assert notificationResponse.data.debts.mainDebt == 0
        assert notificationResponse.data.debts.interest == 0
        assert notificationResponse.data.debts.overdueInterest == 0
        assert notificationResponse.data.debts.fine == 0

        assert debtsResponse == []
        assert debtsFromDb.overdue_interest == notificationResponse.data.debts.overdueInterest
        assert debtsFromDb.interest == notificationResponse.data.debts.interest
        assert debtsFromDb.main_debt == notificationResponse.data.debts.mainDebt
        assert debtsFromDb.fine == notificationResponse.data.debts.fine

        assert notificationResponse.data.currentStatus == CLOSED.name().toLowerCase()
        assert loanHistoryResponse.last().actionName == LOAN_CLOSE.name().toLowerCase()
        assert loanHistoryResponse.last().statusName == CLOSED.name().toLowerCase()
        assert loanHistoryResponse.last().fromTime == getServerDateTime()

        assert loanHistoryFromDb.loan_status == CLOSED.name()
        assert loanHistoryFromDb.loan_action == LOAN_CLOSE.name()
        assert loanHistoryFromDb.revoked_by_id == null

        true
    }

    static boolean checkProlongationData(Map loanBody) {
        NotificationResponse notificationResponse = getNotificationByStatus(loanBody.id, getServerDate(), LoanStatus.PROLONGATION.name().toLowerCase())
        List<GetLoanHistoryResponse> loanHistoryResponse = getLoanHistoryByLoanId(loanBody.id)
        List loanHistoryFromDb = getLonHistoryFromDb(loanBody.id)
        Map prolongationFromDb = getProlongationDb(loanBody.id).last()

        assert notificationResponse.data.debts.mainDebt == loanBody.amount
        assert notificationResponse.data.currentStatus == LoanStatus.PROLONGATION.name().toLowerCase()
        assert loanHistoryResponse.last().actionName == PROLONGATION_START.name().toLowerCase()
        assert loanHistoryResponse.last().statusName == LoanStatus.PROLONGATION.name().toLowerCase()

        assert loanHistoryFromDb.last().loan_status == LoanStatus.PROLONGATION.name()
        assert loanHistoryFromDb.last().loan_action == PROLONGATION_START.name()
        assert loanHistoryFromDb.last().revoked_by_id == null
        if (loanHistoryFromDb.find({ it.containsValue(OVERDUE.name()) })) {
            assert prolongationFromDb.type == RU_LATE.name()
        } else {
            assert prolongationFromDb.type == RU_STANDARD.name()
        }

        true
    }

    static boolean checkFreezingData(Map loanBody) {
        NotificationResponse notificationResponse = getNotificationByStatus(loanBody.id, getServerDate(), LoanStatus.FREEZING.name().toLowerCase())
        List<GetLoanHistoryResponse> loanHistoryResponse = getLoanHistoryByLoanId(loanBody.id)
        Map loanHistoryFromDb = getLonHistoryFromDb(loanBody.id).last()

        assert notificationResponse.data.debts.mainDebt == loanBody.amount
        assert notificationResponse.data.currentStatus == LoanStatus.FREEZING.name().toLowerCase()
        assert loanHistoryResponse.last().actionName == FREEZING_START.name().toLowerCase()
        assert loanHistoryResponse.last().statusName == LoanStatus.FREEZING.name().toLowerCase()

        assert loanHistoryFromDb.loan_status == LoanStatus.FREEZING.name()
        assert loanHistoryFromDb.loan_action == FREEZING_START.name()
        assert loanHistoryFromDb.revoked_by_id == null

        true
    }

    static boolean checkWrittenOffData(Map loanBody, String writeOffDate = getServerDate()) {
        NotificationResponse notificationResponse = getNotificationByStatus(loanBody.id, getServerDate(), WRITTEN_OFF.name().toLowerCase())
        Map debtsFromDb = getDebtsFromDB(loanBody.id)
        List<GetLoanHistoryResponse> loanHistoryResponse = getLoanHistoryByLoanId(loanBody.id)
        Map loanHistoryFromDb = getLonHistoryFromDb(loanBody.id).find({ it.loan_status == WRITTEN_OFF.name() })

        assert notificationResponse.data.debts.writtenOffOverdueInterest == calcOverdueInterest(loanBody.amount, loanBody, 1, writeOffDate)
        assert notificationResponse.data.debts.writtenOffInterest == calcInterest(loanBody.amount, loanBody, 1, writeOffDate)
        assert notificationResponse.data.debts.writtenOffMainDebt == loanBody.amount
        assert notificationResponse.data.debts.writtenOffFinePay == null
        assert notificationResponse.data.currentStatus == WRITTEN_OFF.name().toLowerCase()

        assert debtsFromDb.overdue_interest == 0
        assert debtsFromDb.interest == 0
        assert debtsFromDb.main_debt == 0
        assert debtsFromDb.fine == 0

        assert loanHistoryResponse.last().actionName == WRITE_OFF.name().toLowerCase()
        assert loanHistoryResponse.last().statusName == WRITTEN_OFF.name().toLowerCase()

        assert loanHistoryFromDb.loan_status == WRITTEN_OFF.name()
        assert loanHistoryFromDb.loan_action == WRITE_OFF.name()
        assert loanHistoryFromDb.revoked_by_id == null

        true
    }

    static boolean checkWrittenOffClosedData(Map loanBody) {
        NotificationResponse notificationResponse = getNotificationByStatus(loanBody.id, getServerDate(), CLOSED.name().toLowerCase())
        Map debtsFromDb = getDebtsFromDB(loanBody.id)
        List<GetLoanHistoryResponse> loanHistoryResponse = getLoanHistoryByLoanId(loanBody.id)
        Map loanHistoryFromDb = getLonHistoryFromDb(loanBody.id).last()

        assert notificationResponse.data.debts.overdueInterest == 0
        assert notificationResponse.data.debts.interest == 0
        assert notificationResponse.data.debts.mainDebt == 0
        assert notificationResponse.data.debts.fine == 0
        assert notificationResponse.data.currentStatus == CLOSED.name().toLowerCase()

        assert notificationResponse.data.debts.writtenOffOverdueInterest == 0
        assert notificationResponse.data.debts.writtenOffInterest == 0
        assert notificationResponse.data.debts.writtenOffMainDebt == 0
        assert notificationResponse.data.debts.writtenOffFinePay == null


        assert debtsFromDb.overdue_interest == 0
        assert debtsFromDb.interest == 0
        assert debtsFromDb.main_debt == 0
        assert debtsFromDb.fine == 0


        assert loanHistoryResponse.last().actionName == LOAN_CLOSE.name().toLowerCase()
        assert loanHistoryResponse.last().statusName == CLOSED.name().toLowerCase()

        assert loanHistoryFromDb.loan_status == CLOSED.name()
        assert loanHistoryFromDb.loan_action == LOAN_CLOSE.name()
        assert loanHistoryFromDb.revoked_by_id == null
        true
    }


    static boolean checkTransactionAndDiscountAfterBankrupt(Map loanBody, String terminationOfChargesDate = getServerDate()) {
        List transactions = getTransactionsData(loanBody.id)
        List discount = getDiscountData(loanBody.id)
        String onDate = minusDay(terminationOfChargesDate) // за текущий день начислений не будет
        boolean isMainPeriod = !getLoanHistoryByLoanId(loanBody.id)
                .find({ it.statusName == OVERDUE.name().toLowerCase() })


        def interest = calcInterest(loanBody.amount, loanBody, 1, onDate)
        def overdueInterest = calcOverdueInterest(loanBody.amount, loanBody, 1, onDate)
        def fine = calculateFine(loanBody.amount, loanBody, 0.2, onDate)

        assert transactions.find({ it.name == INTEREST_BANKRUPTCY_LOST.name() }).amount == interest
        assert transactions.find({ it.name == MAIN_DEBT_BANKRUPTCY_LOST.name() }).amount == loanBody.amount

        assert discount.find({ it.operation_type.equalsIgnoreCase(INTEREST_BANKRUPTCY_LOST.name()) }).amount == interest
        assert discount.find({ it.operation_type.equalsIgnoreCase(MAIN_DEBT_BANKRUPTCY_LOST.name()) }).amount == loanBody.amount

        if (!isMainPeriod) {
            assert transactions.find({ it.name == OVERDUE_INTEREST_BANKRUPTCY_LOST.name() }).amount == overdueInterest
            assert transactions.find({ it.name == FINE_DISCOUNT.name() }).amount == fine

            assert discount.find({ it.operation_type.equalsIgnoreCase(OVERDUE_INTEREST_BANKRUPTCY_LOST.name()) }).amount == overdueInterest
            assert discount.find({ it.operation_type.equalsIgnoreCase(FINE_DISCOUNT.name()) }).amount == fine
        }

        true
    }

    static boolean checkScoringInfo(Map loanBody, String status) {
        GetCustomerScoringInfoResponse getCustomerScoringInfo = getCustomerScoringInfo(loanBody.customerId).first() as GetCustomerScoringInfoResponse
        assert getCustomerScoringInfo.loanId == loanBody.id
        assert getCustomerScoringInfo.loanDate == loanBody.loanDate
        assert getCustomerScoringInfo.dueDate == calcDueDate(loanBody.loanDate, loanBody.loanTerm)
        assert getCustomerScoringInfo.status == status
        assert getCustomerScoringInfo.lastOverdueDays == getOverdueDays(loanBody, getServerDate())
        true
    }

    static boolean checkDebt(Map debts, DebtType debtType, def expectedValue) {

        def debtValueByType = debts.get(debtType.name().toLowerCase())
        assert debtValueByType == expectedValue
        return true
    }

    static boolean checkCategorization(Map categorizations, OperationType operationType, def expectedValue) {

        def categorizationValueByType = categorizations.get(operationType.name().toLowerCase())
        assert categorizationValueByType == expectedValue
        return true
    }

    static boolean checkCategorizationsCancellation(def loanId, def paymentResp) {
        int revokedCount
        List categorizationListFromDb = getCategorizationsByLoanId(loanId).findAll({ it.get('payment_id') == paymentResp.paymentId })
        Map categorizationsFromDb = [:]
        categorizationListFromDb.forEach({
            categorizationsFromDb.put(getNameById(it.operation_type_id as int), it.amount)
            if (it.revoked_by_id != null) { revokedCount++ }
        })

        Map categorizations = categorizationsFromDb.entrySet().stream()
                .filter({ !it.key.toString().contains('cancellation') })
                .collect(Collectors.toMap({ it.key }, { it.value }))
                .sort({ it.value })
        Map cancellations = categorizationsFromDb.entrySet().stream()
                .filter({ it.key.toString().contains('cancellation') })
                .collect(Collectors.toMap({ it.key }, { it.value }))
                .sort({ it.value })

        assert categorizationsFromDb.size() == paymentResp.categorization.find({it.loanId == loanId}).debts.getCountOfDeclaredFields() * 2
        assert categorizations.size() == revokedCount
        categorizations.entrySet().forEach({
            assert it.value == cancellations.get(it.key + '_cancellation')
        })
        return true
    }

    static boolean checkGivePaymentBackSuccess(Map loanBody,
                                               LoanStatus status,
                                               def paymentResponse,
                                               closure = { it.data.payments.isEmpty() && it.data.currentStatus == status.name().toLowerCase() },
                                               def mainDebt = loanBody.amount,
                                               def interest = calcInterest(loanBody.amount, loanBody)) {
        def balance = getBalance(loanBody.customerId) as BalanceResponse
        def loanUpdated = getNotificationData(loanBody.id, getServerDate())
                .find(closure)

        checkLoan(loanBody, status.name())

        assert balance.remainAmount == paymentResponse.amount
        assert balance.reservedAmount == balance.remainAmount
        assert balance.payments.last().transactions.size() == 1
        assert balance.payments.last().transactions.last().type == 'TOP_UP_BALANCE'
        assert balance.payments.last().transactions.last().amount == paymentResponse.amount


        assert loanUpdated.data.debts.mainDebt == mainDebt
        assert loanUpdated.data.debts.interest == interest
        if (status != LoanStatus.FREEZING) {
            assert loanUpdated.data.debts.overdueInterest == calcOverdueInterest(loanBody.amount, loanBody)
                    || loanUpdated.data.debts.overdueInterest == null
            assert loanUpdated.data.debts.fine == calculateFine(loanBody.amount, loanBody)
                    || loanUpdated.data.debts.fine == null
        }

        checkCategorizationsCancellation(loanBody.id, paymentResponse)
    }

    static boolean checkCommissionCancellation(Map loanBody, String currentLoanStatus, date = getServerDate()) {

        checkCommissionRevokedState(loanBody, currentLoanStatus, date)

        if (!LOAN_STATES_WITH_NO_DEBTS.contains(currentLoanStatus)) {
            Debts debts = getCurrentDebtsByLoan(loanBody.id).last().debts as Debts
            assert debts.getCountOfDeclaredCommissions() == 0
        }
        true
    }

    static boolean checkCommissionRevokedState(Map loanBody, String currentLoanStatus, date = getServerDate()) {
        List<Commissions> commissionsByLoanId = getCommissionsByLoan(loanBody.id as Integer)
        List<Commissions> commissionsByCustomerId = getCommissionsByCustomer(loanBody.customerId as Integer)
        List commissionDataFromDB = getCommissionsDataFromDB(loanBody.id)

        commissionsByLoanId.forEach(c -> {
            if (!LOAN_STATES_WITH_NO_DEBTS.contains(currentLoanStatus)) {
                GetLoanDataOnDateResponse loanData = getLoanDataOnDate(loanBody.id) as GetLoanDataOnDateResponse

                assert loanData.cancelledCommissionCharges.size() == commissionsByLoanId.size()
                assert loanData.cancelledCommissionCharges.get(snakeCaseToCamelCase("commission_".concat(c.type.toLowerCase())))
                        == c.amount

                if (WRITTEN_OFF.name() != currentLoanStatus) {
                    NotificationResponse notificationResponse = getNotificationData(loanBody.id, date).last()

                    assert notificationResponse.data.commissions.size() == commissionsByLoanId.size()
                    notificationResponse.data.commissions.forEach(com -> {
                        assert c.state == REVOKED.name()
                    })
                }
            }
            assert c.state == REVOKED.name()
        })


        commissionsByCustomerId.forEach(c -> {
            assert c.state == REVOKED.name()
        })

        assert commissionDataFromDB.size() == commissionsByLoanId.size()
        commissionDataFromDB.forEach(c -> {
            assert c.get("state") == REVOKED.name()
            assert c.get("modified").toString().split(" ").first() == date
        })

        true
    }

    static boolean checkEmptyGetBalance(BalanceResponse response) {
        assert response.remainAmount == 0
        assert response.reservedAmount == 0
        assert response.payments.isEmpty()
        assert response.withdrawalsFromBalance.isEmpty()
        return true
    }

    static boolean checkChargesCorrectnessAfterPaymentBack(def expectedChargesBefore,
                                                           def expectedChargesAfter,
                                                           def actualChargesBefore,
                                                           def actualChargesAfter) {
        assert actualChargesBefore < actualChargesAfter
        assert actualChargesBefore == expectedChargesBefore
        assert actualChargesAfter == expectedChargesAfter
        return true
    }
}
