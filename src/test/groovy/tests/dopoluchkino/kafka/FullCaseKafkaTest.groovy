package tests.dopoluchkino.kafka

import kafka.dto.response.BillingReceivedPaymentDto
import kafka.dto.response.LoanUpdatedDto
import rest.service.loanService.response.GetDebtsResponse
import rest.service.loanService.response.NotificationResponse
import spock.lang.Specification
import spock.lang.Unroll

import static dataBaseService.ManipulationDao.getProlongationOrderFromDb
import static helper.KafkaRequests.*
import static helper.VerifyResponse.*
import static helper.VerifyResponseKafka.checkReceivedPayment
import static helper.VerifyResponseKafka.checkSuccessKafka
import static helper.Wrappers.findCurrentDebtOnDate
import static helper.Wrappers.getNotifications
import static helper.restrequests.DopoluchkinoRestRequests.getLoanBodyReq
import static kafka.service.BillingReceivedPaymentService.getBillingReceivedPayment
import static kafka.service.CleanTopicService.cleanTopics
import static kafka.service.CustomerMadePaymentService.sendCustomerMadePayment
import static kafka.service.CustomerReceivedMoneyService.sendCustomerReceivedMoneyMsg
import static kafka.service.CustomerRequestedFreezingService.sendCustomerRequestedFreezing
import static kafka.service.CustomerSignedProlongationService.sendCustomerSignedProlongation
import static kafka.service.LoanUpdatedService.getLoanUpdated
import static kafka.topics.TopicName.*
import static kafka.topics.TopicPrefixName.DOPOLUCHKINO
import static rest.service.loanService.controllers.DebtsController.getCurrentDebsOnDate
import static rest.service.loanService.controllers.LoanDateTimeController.*
import static rest.service.loanService.enums.InterestDiscountType.FULL
import static rest.service.loanService.enums.InterestDiscountType.STANDARD
import static rest.service.loanService.enums.LoanStatus.*
import static rest.service.utils.ReqHelper.*

class FullCaseKafkaTest extends Specification {

    private static Map loanBody
    private static Map customerReceivedMoneyReq
    private static Map customerRequestedFreezingReq
    private static Map customerMadePaymentReq
    private static Map customerSignedProlongationReq

    private static LoanUpdatedDto loanUpdatedMessage
    private static NotificationResponse notificationResponse
    private static GetDebtsResponse getDebtsResponse
    private static BillingReceivedPaymentDto billingReceivedPayment


    def setupSpec() {
        cleanTopics([BILLING_ERRORS, MSG_PROCESSING_ERROR, LOAN_UPDATED], DOPOLUCHKINO)
    }

    def setup() {
        loanBody = getLoanBodyReq()
        customerReceivedMoneyReq = getCustomerReceivedMoneyMsg(loanBody)
        customerRequestedFreezingReq = getCustomerRequestedFreezingMsg(loanBody)
        customerMadePaymentReq = getCustomerMadePaymentMsg(loanBody)
        customerSignedProlongationReq = getCustomerSignedProlongation(loanBody, customerMadePaymentReq.payload.paymentId)
    }

    @Unroll
    def "check case: loan with discount -> overdue -> freezing -> prolongation -> closed"() {
        setup: "добавляем 100% скидку"
        customerReceivedMoneyReq.payload.agreement += [
                interestDiscount: [
                        "type" : discountType,
                        "value": discountValue]
        ]

        when:
        println "loan created: " + getServerDateTime()
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)

        then:
        checkSuccessKafka(loanBody, loanUpdatedMessage, OPENED.name(), DOPOLUCHKINO)
        checkLoan(loanBody, OPENED.name())

        when:
        notificationResponse = getNotifications(loanBody.id, getServerDate(), OPENED.name().toLowerCase()).last()

        then: "проверяем скидку"
        assert notificationResponse.data.interestDiscount == discountValue
        assert checkInterestDiscount(loanBody, discountType, discountValue)

        when: "двигаем на последний день основного периода"
        calculateToDate(loanBody.id, getServerDateTime(loanBody.loanTerm))
        getDebtsResponse = findCurrentDebtOnDate(loanBody.id)

        then:
        assert getDebtsResponse.debts.mainDebt == loanBody.amount
        assert getDebtsResponse.debts.interest == (discountValue == 1 ? null : calcInterest(loanBody.amount, loanBody, 1 - discountValue))
        assert getDebtsResponse.debts.overdueInterest == null
        assert getDebtsResponse.debts.fine == null

        when: "двигаем в просрочку"
        calculateToDate(loanBody.id, getServerDateTime(1))
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)
        getDebtsResponse = findCurrentDebtOnDate(loanBody.id)
        String overdueStartTime = getServerDate()

        then: "проценты доначислены"
        checkLoan(loanBody, OVERDUE.name())
        checkSuccessKafka(loanBody, loanUpdatedMessage, OVERDUE.name(), DOPOLUCHKINO)
        assert getDebtsResponse.debts.mainDebt == loanBody.amount
        assert getDebtsResponse.debts.interest == calcInterest(loanBody.amount, loanBody)
        assert getDebtsResponse.debts.overdueInterest == calcOverdueInterest(loanBody.amount, loanBody)
        assert getDebtsResponse.debts.fine == calculateFine(loanBody.amount, loanBody)

        when: "двигаем в заморозку"
        customerRequestedFreezingReq.payload.freezing.fromDate = getServerDate()
        sendCustomerRequestedFreezing(customerRequestedFreezingReq, DOPOLUCHKINO)
        loanUpdatedMessage = waitResponse({ getLoanUpdated(DOPOLUCHKINO) },
                { it.currentStatus == FREEZING.name().toLowerCase() }) as LoanUpdatedDto
        println "freezing: " + getServerDateTime()

        then:
        checkLoan(loanBody, FREEZING.name())
        checkSuccessKafka(loanBody, loanUpdatedMessage, FREEZING.name(), DOPOLUCHKINO, 1000)
        checkFreezingData(loanBody)
        assert loanUpdatedMessage.freezing.fromDate == getServerDate()
        assert loanUpdatedMessage.freezing.toDate == getServerDate(customerRequestedFreezingReq.payload.freezing.term)

        when: "выходим из заморозки"
        calculateToDate(loanBody.id, getServerDateTime(customerRequestedFreezingReq.payload.freezing.term + 1))
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)
        println "freezing ended: " + getServerDateTime()

        then:
        checkLoan(loanBody, OVERDUE.name())
        checkSuccessKafka(loanBody, loanUpdatedMessage, OVERDUE.name(), DOPOLUCHKINO, 100, overdueStartTime)

        when: "оплачиваем проценты"
        customerMadePaymentReq.payload.amount = calcInterest(loanBody.amount, loanBody) + calcOverdueInterest(loanBody.amount, loanBody)
        customerMadePaymentReq.payload.paymentTime = getSrvDateTimeMinusSec(2).concat("+07:00")
        sendCustomerMadePayment(customerMadePaymentReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)
        billingReceivedPayment = getBillingReceivedPayment(DOPOLUCHKINO)
        getDebtsResponse = findCurrentDebtOnDate(loanBody.id)

        then:
        checkLoan(loanBody, OVERDUE.name())
        checkSuccessKafka(loanBody, loanUpdatedMessage, OVERDUE.name(), DOPOLUCHKINO, 100, overdueStartTime)
        checkReceivedPayment(billingReceivedPayment, customerMadePaymentReq)
        assert getDebtsResponse.debts.interest == null
        assert getDebtsResponse.debts.overdueInterest == null
        assert getDebtsResponse.debts.mainDebt == loanBody.amount
        assert getDebtsResponse.debts.fine == calculateFine(loanBody.amount, loanBody)
        assert getProlongationOrderFromDb(loanBody.id) != null

        when: "двигаем в пролонгацию"
        customerSignedProlongationReq.payload.activationTime = getSrvDateTimeMinusSec(1).concat("+07:00")
        sendCustomerSignedProlongation(customerSignedProlongationReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)

        then:
        checkLoan(loanBody, PROLONGATION.name())
        checkSuccessKafka(loanBody, loanUpdatedMessage, PROLONGATION.name(), DOPOLUCHKINO)
        checkProlongationData(loanBody)

        when: "закрываем займ на второй день пролонгации"
        println findCurrentDebtOnDate(loanBody.id)
        calculateToDate(loanBody.id, getServerDateTime(2))
        customerMadePaymentReq.payload.amount = loanBody.amount +
                getInterestPerDay(loanBody.amount, percenValueInProlongation) * 2
        customerMadePaymentReq.payload.paymentId = getNewExternalId()
        customerMadePaymentReq.payload.paymentTime = getEventTime()
        sendCustomerMadePayment(customerMadePaymentReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)
        billingReceivedPayment = getBillingReceivedPayment(DOPOLUCHKINO)

        then:
        checkLoan(loanBody, CLOSED.name())
        checkSuccessKafka(loanBody, loanUpdatedMessage, CLOSED.name(), DOPOLUCHKINO)
        assert getCurrentDebsOnDate(loanBody.id, getServerDateTime()) == []

        cleanup:
        clearDate()

        where:
        discountType    | discountValue | percenValueInProlongation
        FULL.name()     | 1             | 1
        STANDARD.name() | 0.6           | 0.4
    }
}
