package dataBaseService

import groovy.json.JsonSlurper
import rest.service.loanService.enums.DebtType
import rest.service.loanService.enums.LoanActionName
import rest.service.loanService.enums.LoanStatus
import rest.service.loanService.enums.LoanTypeName

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static java.util.Objects.isNull
import static rest.service.loanService.enums.DebtType.getDebtType
import static rest.service.loanService.enums.OperationType.getNameById
import static rest.service.utils.ReqHelper.clearDate

class ManipulationDao {

    private static final PostgreConnect CHARGE = new PostgreConnect("CHARGE")
    private static final PostgreConnect CUSTOMER_BALANCE = new PostgreConnect("CUSTOMER_BALANCE")
    private static final PostgreConnect DEBT = new PostgreConnect("DEBT")
    private static final PostgreConnect DISCOUNT = new PostgreConnect("DISCOUNT")
    private static final PostgreConnect FREEZING = new PostgreConnect("FREEZING")
    private static final PostgreConnect FUNDS_RESERVATION_HISTORY = new PostgreConnect("FUNDS_RESERVATION_HISTORY")
    private static final PostgreConnect INTEREST_DISCOUNT = new PostgreConnect("INTEREST_DISCOUNT")
    private static final PostgreConnect LOAN = new PostgreConnect("LOAN")
    private static final PostgreConnect LOAN_HISTORY = new PostgreConnect("LOAN_HISTORY")
    private static final PostgreConnect NOTIFICATION_HISTORY = new PostgreConnect("NOTIFICATION_HISTORY")
    private static final PostgreConnect OPERATION_TYPE = new PostgreConnect("OPERATION_TYPE")
    private static final PostgreConnect OVERDUE_DAYS = new PostgreConnect("OVERDUE_DAYS")
    private static final PostgreConnect PAYMENT = new PostgreConnect("PAYMENT")
    private static final PostgreConnect PAYMENT_CATEGORIZATION = new PostgreConnect("PAYMENT_CATEGORIZATION")
    private static final PostgreConnect PAYMENT_SCHEDULE = new PostgreConnect("PAYMENT_SCHEDULE")
    private static final PostgreConnect PROLONGATION = new PostgreConnect("PROLONGATION")
    private static final PostgreConnect PROLONGATION_ORDER = new PostgreConnect("PROLONGATION_ORDER")
    private static final PostgreConnect PSK = new PostgreConnect("PSK")
    private static final PostgreConnect REPORT_PERIOD = new PostgreConnect("REPORT_PERIOD")
    private static final PostgreConnect REFINANCE_INTENT = new PostgreConnect("REFINANCE_INTENT")
    private static final PostgreConnect TARIFF = new PostgreConnect("TARIFF")
    private static final PostgreConnect TRANSACTION = new PostgreConnect("TRANSACTION")
    private static final PostgreConnect COMMISSION = new PostgreConnect("COMMISSION")


    static def getAllChargesDB(def loanId) {
        List charge = CHARGE.selectAll([LOAN_ID: loanId])
        charge.forEach({
            it.put(getNameById(it.operation_type_id).toString(), it.amount)
        })
        charge
    }

    static def getChargesSumDb(def loanId) {
        String query = "SELECT SUM(amount) FROM CHARGE WHERE LOAN_ID = '$loanId'" +
                " AND REVOKED_BY_ID IS NULL" +
                " AND id NOT IN (SELECT revoked_by_id FROM CHARGE WHERE revoked_by_id IS NOT NULL)"
        def result = CHARGE.selectOne(query).sum
        return isNull(result) ? 0 : result * 100 as Integer
    }

    static def getChargesSumFromDate(def loanId, def fromDate) {
        String query = "SELECT SUM(amount) FROM CHARGE WHERE LOAN_ID = '$loanId'" +
                " AND REVOKED_BY_ID IS NULL" +
                " AND CHARGE_DATE >= '${fromDate}'" +
                " AND id NOT IN (SELECT revoked_by_id FROM CHARGE WHERE revoked_by_id IS NOT NULL)"
        def result = CHARGE.selectOne(query).sum
        return isNull(result) ? 0 : result * 100
    }

    static List getChargesFromDate(def loanId, def fromDate) {
        List allCharges = CHARGE.selectAll("SELECT * FROM CHARGE WHERE LOAN_ID = '${loanId}'" +
                " AND CHARGE_DATE >= '${fromDate}'" +
                " AND id NOT IN (SELECT revoked_by_id FROM CHARGE WHERE revoked_by_id IS NOT NULL)")
        return allCharges
    }

    static List getNonRevokedChargesFromDate(def loanId, def fromDate) {
        List allCharges = CHARGE.selectAll("SELECT * FROM CHARGE" +
                " WHERE LOAN_ID = '${loanId}'" +
                " AND CHARGE_DATE >= '${fromDate}'" +
                " AND REVOKED_BY_ID IS NULL" +
                " AND id NOT IN (SELECT revoked_by_id FROM CHARGE WHERE revoked_by_id IS NOT NULL)")
        return allCharges
    }

    static def getRevokedChargesSum(def loanId) {
        def result = CHARGE.selectOne("SELECT SUM(amount) FROM CHARGE WHERE LOAN_ID = '${loanId}' AND REVOKED_BY_ID IS NOT NULL").sum
        return isNull(result) ? 0 : result * 100
    }

    static def getSumOfPercents(def loanId) {
        String query = "SELECT SUM(amount) FROM CHARGE WHERE LOAN_ID = '$loanId'" +
                " AND OPERATION_TYPE_ID IN ( '1', '3')" +
                " AND REVOKED_BY_ID IS NULL" +
                " AND id NOT IN (SELECT revoked_by_id FROM CHARGE WHERE revoked_by_id IS NOT NULL)"
        def result = CHARGE.selectOne(query).sum
        return isNull(result) ? 0 : result * 100
    }

    static def getWithdrawalsFromDB(def customerId) {
        String query = "SELECT * FROM payment WHERE customer_id = '$customerId'" +
                " AND REVOKED_BY_ID IS NULL" +
                " AND OPERATION_TYPE_ID = '23'"
        def result = PAYMENT.selectAll(query)
        return result
    }

    static def getCustomerBalanceData(def customerId) {
        CUSTOMER_BALANCE.selectOne([CUSTOMER_ID: customerId])
    }

    static Map getDebtsFromDB(def loanId) {
        List debtsInBd = getDebtsData(loanId)
        Map debts = [:]
        debtsInBd.forEach({ debts.put(getDebtType(it.debt_type_id as int), it.value * 100) })
        debts
    }

    static List getDebtsData(def loanId) {
        List result = DEBT.selectAll([LOAN_ID: loanId])
        result.sort({ it.debt_type_id })
        return result
    }

    static Map getDiscountsFromDB(def loanId) {
        List discountsInBd = getDiscountData(loanId)
        Map discounts = [:]
        discountsInBd.forEach({ discounts.put(getNameById(it.operation_type_id as int), it.amount) })
        discounts
    }

    static List getCommissionsDataFromDB(def loanId) {
        List result = COMMISSION.selectAll([LOAN_ID: loanId])
        result.sort({ it.commission_type_id })
        return result
    }

    static List<Map> getDiscountData(def loanId) {
        List result = DISCOUNT.selectAll([LOAN_ID: loanId])
        result.forEach({ Map it ->
            it.put("operation_type", getNameById(it.operation_type_id))
            it.amount = it.amount * 100
        })

        result.sort({ it.id })
        return result
    }

    static void setDebt(def loanId, def value, DebtType debtType) {
        DEBT.update([VALUE: value], [LOAN_ID: loanId, DEBT_TYPE_ID: debtType.getId()])
    }

    static def getFreezingListFromDb(def loanId) {
        return FREEZING.selectAll([LOAN_ID: loanId]).sort({ it.id })
    }

    static def getFreezingFromDb(def loanId) {
        return FREEZING.selectOne([LOAN_ID: loanId])
    }

    static Map getFundsReservationHistoryFromDb(def paymentId, String action) {
        Map fundReserveHistory = FUNDS_RESERVATION_HISTORY.selectOne([payment_id: paymentId, action: action])
        fundReserveHistory.amount = fundReserveHistory.amount * 100
        fundReserveHistory
    }

    static def getInterestDiscountDB(def loanId) {
        INTEREST_DISCOUNT.selectAll([LOAN_ID: loanId])
    }

    static String getLoanData(def loanId) {
        LOAN.selectOne([ID: loanId])
    }

    static String getLoanStatusFromDb(def loanId) {
        LoanStatus.getStatusById(LOAN.selectOne([ID: loanId]).LOAN_STATUS_ID)
    }

    static def getLonHistoryFromDb(def loanId) {
        List<HashMap> result = LOAN_HISTORY.selectAll([LOAN_ID: loanId]).sort({ it.id })
        result.forEach({
            it.put("loan_status", LoanStatus.getStatusById(it.loan_status_id))
            it.put("loan_action", LoanActionName.getActionById(it.loan_action_id))
        })
        result
    }

    static def getNotificationMessageFromDb(def loanId) {
        List result = []
        NOTIFICATION_HISTORY.selectAll([LOAN_ID: loanId]).sort({ it.ID }).forEach({
            result.add(new JsonSlurper().parseText(it.MESSAGE as String))
        })
        return result
    }

    static List getAllNotifications(def loanId) {
        NOTIFICATION_HISTORY.selectAll([LOAN_ID: loanId]).sort({ it.ID })
    }

    static String getLastPaymentOperation(def externalId) {
        int operationId = PAYMENT.selectAll([EXTERNAL_ID: externalId]).sort({ it.ID }).last().OPERATION_TYPE_ID as int
        OPERATION_TYPE.selectOne([ID: operationId]).NAME
    }

    static List getOverdueDaysFromDb(def loanId) {
        OVERDUE_DAYS.selectAll([LOAN_ID: loanId])
    }

    static void clearOverdueDaysTab() {
        OVERDUE_DAYS.deleteAll()
    }

    static List getPaymentData(def loanId, def externalId) {
        PAYMENT.selectAll([LOAN_ID: loanId, EXTERNAL_ID: externalId]).sort({ it.id })
    }

    static List getReservationHistoryData(def paymentId) {
        FUNDS_RESERVATION_HISTORY.selectAll([PAYMENT_ID: paymentId]).sort({it.id})
    }

    static Map getRevokedPaymentFromDb(def loanId, def externalId) {
        PAYMENT.selectOne("SELECT * FROM PAYMENT WHERE LOAN_ID = '${loanId}' AND EXTERNAL_ID = '${externalId}' AND REVOKED_BY_ID IS NOT NULL")
    }

    static List getPaymentCategorization(def loanId) {
        List categorization = PAYMENT_CATEGORIZATION.selectAll([LOAN_ID: loanId]).sort({ it.id })

        categorization.forEach({
            it.issued_time = LocalDateTime.parse(it.issued_time.toString().split('\\.').first(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        })
        categorization
    }

    static Map getCategorizationsFromDB(def loanId) {
        List categorizationsInBd = getCategorizationsByLoanId(loanId)
        Map categorizations = [:]
        categorizationsInBd.forEach({ categorizations.put(getNameById(it.operation_type_id as int), it.amount) })
        categorizations
    }

    static List getCategorizationsByLoanId(def loanId) {
        List categorization = PAYMENT_CATEGORIZATION.selectAll([LOAN_ID: loanId])
        categorization.forEach({ Map it ->
            it.put("operation_type", getNameById(it.operation_type_id))
            it.amount = it.amount * 100
        })
        return categorization
    }

    static Map getPaymentScheduleDb(def loanId) {
        PAYMENT_SCHEDULE.selectOne([LOAN_ID: loanId])
    }

    static List getProlongationDb(def loanId) {
        PROLONGATION.selectAll([LOAN_ID: loanId]).sort({ it.ID })
    }

    static Map getProlongationOrderFromDb(def loanId) {
        PROLONGATION_ORDER.selectAll([LOAN_ID: loanId]).sort({ it.ID }).last()
    }

    static List getAllProlongationOrdersDb(def loanId) {
        PROLONGATION_ORDER.selectAll([LOAN_ID: loanId])
    }

    static Map getPskFromDb(def loanId) {
        PSK.selectOne([LOAN_ID: loanId])
    }

    static def getReportPeriodData(def id) {
        REPORT_PERIOD.selectOne([ID: id])
    }

    static Map getTariffFromDb(def amount, def term, def loanTypeName) {
        def tariffs = TARIFF.selectAll([LOAN_TYPE_ID: LoanTypeName.getIdByName(loanTypeName)])
        Map m = tariffs.find({
            it.amount == amount / 100 && it.loan_term == term
        }) as Map
        m.options = new JsonSlurper().parseText(m.options as String) as Map
        return m
    }

    static List getAllTariffByLoanType(def loanTypeName) {
        TARIFF.selectAll([LOAN_TYPE_ID: LoanTypeName.getIdByName(loanTypeName)])
    }

    static void setRefinanceIntentState(def loanId, boolean state) {
        REFINANCE_INTENT.update([ACTIVE: state], [LOAN_ID: loanId])
    }

    static void getRefinanceIntent(def loanId) {
        REFINANCE_INTENT.selectOne([LOAN_ID: loanId])
    }

    static List getTransactionsData(def loanId) {
        List<Map> result = TRANSACTION.selectAll([LOAN_ID: loanId])
        result.forEach({ it.amount = it.amount * 100 })
        result.sort({ it.id })
    }
}
