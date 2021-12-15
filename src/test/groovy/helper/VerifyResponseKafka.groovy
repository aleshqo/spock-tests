package helper

import kafka.dto.response.BillingReceivedPaymentDto
import kafka.dto.response.LoanUpdatedDto
import kafka.dto.response.MessageDto
import kafka.dto.response.MsgProcessingErrorDto
import kafka.topics.TopicPrefixName
import rest.service.loanService.enums.LoanStatus

import static dataBaseService.ManipulationDao.*
import static java.util.Objects.isNull
import static kafka.service.BillingErrorsService.getBillingErrors
import static kafka.service.MsgProcessingErrorService.getMsgProcessingError
import static rest.service.loanService.controllers.LoanDateTimeController.getServerDate
import static rest.service.loanService.controllers.LoanDateTimeController.getServerDateTime
import static rest.service.loanService.enums.CommissionsType.getCommissionTypeName
import static rest.service.loanService.enums.InterestDiscountType.*
import static rest.service.loanService.enums.LoanStatus.*
import static rest.service.utils.ReqHelper.*
import static utils.JsonParserHelper.getValueFromJson

class VerifyResponseKafka {

    private static final List<String> WITH_COMMISSION_LOAN_STATES = List.of(OPENED.name(), OVERDUE.name(),
            LoanStatus.PROLONGATION.name(), LoanStatus.FREEZING.name(), STOPPED.name(), FRAUDSTER.name(), WRITTEN_OFF.name())

    static boolean checkSuccessKafka(Map loanBody,
                                     LoanUpdatedDto loanUpdatedMessage,
                                     String status,
                                     TopicPrefixName topicPrefixName,
                                     Long timeout = 100,
                                     def currentStatusDate = getServerDate()) {

        checkLoanUpdated(loanBody, loanUpdatedMessage, status, currentStatusDate)
        checkNoErrors(topicPrefixName, timeout)
        true
    }

    static boolean checkLoanUpdated(Map loanBody, LoanUpdatedDto loanUpdatedMessage, String status, String currentStatusDate) {
        List prolongList = getProlongationDb(loanBody.id)
        List<Map> interestDiscountFromDb = getInterestDiscountDB(loanBody.id)
        def interestDiscountValue = getInterestDiscountValue(interestDiscountFromDb)
        def commissionAmount = 0

        assert loanUpdatedMessage.customerId == loanBody.customerId
        assert loanUpdatedMessage.loanId == loanBody.id
        assert loanUpdatedMessage.loanDate == loanBody.loanDate
        assert loanUpdatedMessage.repaymentDate == calcDueDate(loanBody.loanDate, loanBody.loanTerm)

        assert loanUpdatedMessage.typeName == loanBody.loanTypeName
        assert loanUpdatedMessage.amount == loanBody.amount
        assert loanUpdatedMessage.term == loanBody.loanTerm
        assert loanUpdatedMessage.currentStatus == status.toLowerCase()
        assert loanUpdatedMessage.interestDiscount == (status == OPENED.name() ||
                status == LoanStatus.PROLONGATION.name() ? interestDiscountValue : 0)
        assert loanUpdatedMessage.interest == 1
        assert loanUpdatedMessage.overdueInterest == 1
        assert loanUpdatedMessage.debts // todo: проверять отдельно

//        assert loanUpdatedMessage.prolongationAvailability == // todo: метод для проверки пролонгации
        if (!prolongList.isEmpty()) {
            assert loanUpdatedMessage.prolongations != []
            assert loanUpdatedMessage.daysPastDue == ((status == CLOSED.name() || status == CANCELLED.name() || status == BANKRUPT.name()) ?
                    null : getElapsedDays(prolongList[0].to_time.toString().split(" ").first(), getServerDateTime().split('T')[0]))
        } else {
            assert loanUpdatedMessage.prolongations == []
            assert loanUpdatedMessage.daysPastDue == ((status == CLOSED.name() || status == CANCELLED.name() || status == BANKRUPT.name()) ?
                    null : getElapsedDays(loanUpdatedMessage.repaymentDate, getServerDateTime().split('T')[0]))
        }
        if (!loanUpdatedMessage.commissions.isEmpty() && isLoanStateAllowCommissionExistence(loanUpdatedMessage)) {
            checkCommissionWithDB(loanUpdatedMessage)
            commissionAmount = loanUpdatedMessage.commissions.findAll().stream()
                    .map(c -> c.amount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
        }
        boolean paymentCoversInterest = isPaymentCoversInterest(loanUpdatedMessage)
        if (paymentCoversInterest) {
            loanUpdatedMessage.prolongationOrder == null
        } else {
            loanUpdatedMessage.prolongationOrder != null // todo: сделать проверку подробнее
        }
        assert loanUpdatedMessage.initialRepaymentAmount == getInitialRepaymentAmount(interestDiscountFromDb, loanBody) + commissionAmount
        assert loanUpdatedMessage.currentStatusDate.split("T").first() == currentStatusDate

        true
    }

    static boolean isPaymentCoversInterest(LoanUpdatedDto loanUpdatedMessage) {
        if (!loanUpdatedMessage.payments.isEmpty()) {
            int amount = 0
            BigDecimal interest = isNull(loanUpdatedMessage.debts.interest) ? 0 : loanUpdatedMessage.debts.interest
            BigDecimal overdueInterest = isNull(loanUpdatedMessage.debts.overdueInterest) ? 0 : loanUpdatedMessage.debts.overdueInterest
            loanUpdatedMessage.payments.forEach({ amount += it.amount })
            if (amount >= interest + overdueInterest) {
                return true
            }
        }
        return false
    }

    static def getInitialRepaymentAmount(List<Map> interestDiscountFromDb, Map loanBody) {
        def interestDiscountValue = 0
        if (!interestDiscountFromDb.isEmpty()) {
            interestDiscountValue = interestDiscountFromDb.last().type == FULL.name() ||
                    interestDiscountFromDb.last().type == STANDARD.name() ? interestDiscountFromDb.value : 0
        }
        calcPaymentAmount(loanBody.amount, 1 - interestDiscountValue, loanBody.loanTerm)
    }

    static boolean checkPayments(LoanUpdatedDto loanUpdatedMessage, Map customerMadePaymentReq) {
        assert loanUpdatedMessage.payments.first().amount == customerMadePaymentReq.payload.amount
        assert loanUpdatedMessage.payments.first().paymentTime == customerMadePaymentReq.payload.paymentTime
        assert loanUpdatedMessage.payments.first().categorization.paymentDate == customerMadePaymentReq.payload.paymentTime.split('T').first()
        true
    }

    static boolean checkCustomerBalanceUpdated(Map customerMadePaymentReq, def remainAmount, boolean isCanceled = false) {
        Map customerBalanceUpdatedFromDb = getNotificationMessageFromDb(0)
                .find({ it.name == 'customerBalanceUpdated' && it.payload.customerId == customerMadePaymentReq.payload.customerId })
        assert customerBalanceUpdatedFromDb.payload.loanId == customerMadePaymentReq.payload.loanId
        assert customerBalanceUpdatedFromDb.payload.payments[0].amount == customerMadePaymentReq.payload.amount
        assert customerBalanceUpdatedFromDb.payload.payments[0].externalId == customerMadePaymentReq.payload.paymentId
        assert customerBalanceUpdatedFromDb.payload.payments[0].remainedAmount == remainAmount
        assert customerBalanceUpdatedFromDb.payload.payments[0].cancelled == isCanceled
        true
    }

    static boolean checkPaymentOperations(Map customerMadePaymentReq, def loanId, String type, def amount) {
        Map customerBalanceUpdatedFromDb = getNotificationMessageFromDb(0)
                .find({
                    it.name == 'customerBalanceUpdated' &&
                            it.payload.customerId == customerMadePaymentReq.payload.customerId &&
                            it.payload.payments[0].externalId == customerMadePaymentReq.payload.paymentId
                })

        List<Map> operations = customerBalanceUpdatedFromDb.payload.payments.first().operations
        Map operation = operations.find({ it.type == type })
        assert operation.type == type
        assert operation.loanId == loanId
        assert operation.amount == amount

        true
    }

    static boolean checkCommissionWithDB(LoanUpdatedDto loanUpdatedMessage) {
        def commissions = getCommissionsDataFromDB(loanUpdatedMessage.loanId)
        loanUpdatedMessage.commissions.forEach(c -> {
            def commissionFromDB = commissions.find({ it.get("id") == c.id })

            assert commissionFromDB != null
            assert c.amount / 100 == commissionFromDB.get("amount")
            assert c.type.toLowerCase() == getCommissionTypeName(commissionFromDB.get("commission_type_id") as Integer)
            assert c.state == commissionFromDB.get("state")
            assert c.fromDate == commissionFromDB.get("from_date").toString()
            assert c.toDate == commissionFromDB.get("to_date").toString()
            assert c.externalId == commissionFromDB.get("external_id")
        })

        true
    }

    static boolean checkReceivedPayment(BillingReceivedPaymentDto billingReceivedPayment, Map customerMadePaymentReq) {
        assert billingReceivedPayment.customerId == customerMadePaymentReq.payload.customerId
        assert billingReceivedPayment.loanId == customerMadePaymentReq.payload.loanId
        assert billingReceivedPayment.amount == customerMadePaymentReq.payload.amount
        assert billingReceivedPayment.operationType == customerMadePaymentReq.payload.operationType
        assert billingReceivedPayment.paymentId == customerMadePaymentReq.payload.paymentId
        assert billingReceivedPayment.paymentTime == customerMadePaymentReq.payload.paymentTime

        Map billingReceivedPaymentFromDb = getNotificationMessageFromDb(0)
                .find({ it.name == 'billingReceivedPayment' && it.payload.customerId == customerMadePaymentReq.payload.customerId })
        assert billingReceivedPaymentFromDb.payload.loanId == customerMadePaymentReq.payload.loanId
        true
    }

    private static def getInterestDiscountValue(List<Map> interestDiscount) {
        return !interestDiscount.isEmpty()
                && interestDiscount.last().type != MANUAL.name()
                && !interestDiscount.last().revoked
                ? interestDiscount.last().value : 0
    }

    private static def getProlongStartDate(def loanId) {
        List prolongation = getProlongationDb(loanId)
        return !prolongation.isEmpty() ?
                prolongation.last().FROM_TIME.toString().split(" ").first() : null
    }

    static boolean checkNoErrors(TopicPrefixName topicPrefixName, Long timeout = 500) {
        def msgProcessingError = getMsgProcessingError(topicPrefixName, timeout)
        def billingErrors = getBillingErrors(topicPrefixName, timeout)
        if (msgProcessingError != null) {
            log.warn("Error=[{}], original message=[{}]", msgProcessingError.error, msgProcessingError.originalMessage)
        }
        if (billingErrors != []) {
            log.warn("billingErrors=[{}]", billingErrors)
        }
        assert msgProcessingError == null
        assert billingErrors == []
        true
    }

    static boolean checkFailureKafka(Map originalMessage, String error, TopicPrefixName topicPrefixName, Long timeout = 3000) {
        MsgProcessingErrorDto msgProcessingError = getMsgProcessingError(topicPrefixName, timeout)
        List<MessageDto> billingErrors = getBillingErrors(topicPrefixName, timeout)

        assert billingErrors.size() == 1
        assert msgProcessingError.error == error
        assert msgProcessingError.originalMessage == originalMessage
        assert getValueFromJson(billingErrors.first().messagePayload, Map) == originalMessage
        true
    }

    private static boolean isLoanStateAllowCommissionExistence(LoanUpdatedDto loanUpdatedMessage) {
        return WITH_COMMISSION_LOAN_STATES.contains(loanUpdatedMessage.currentStatus.toUpperCase())
    }
}
