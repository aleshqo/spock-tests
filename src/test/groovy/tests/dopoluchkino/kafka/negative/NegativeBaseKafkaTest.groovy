package tests.dopoluchkino.kafka.negative

import kafka.dto.response.LoanUpdatedDto
import kafka.dto.response.MessageDto
import kafka.dto.response.MsgProcessingErrorDto
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

import static helper.KafkaRequests.getCancelLoanAgreementMsg
import static helper.KafkaRequests.getCustomerReceivedMoneyMsg
import static helper.VerifyResponseKafka.checkFailureKafka
import static kafka.service.BillingErrorsService.getBillingErrors
import static kafka.service.CleanTopicService.cleanTopics
import static kafka.service.CustomerReceivedMoneyService.sendCustomerReceivedMoneyMsg
import static kafka.service.LoanUpdatedService.getLoanUpdated
import static kafka.service.MsgProcessingErrorService.getMsgProcessingError
import static kafka.topics.TopicName.BILLING_ERRORS
import static kafka.topics.TopicName.MSG_PROCESSING_ERROR
import static kafka.topics.TopicPrefixName.DOPOLUCHKINO
import static helper.restrequests.DopoluchkinoRestRequests.getLoanBodyReq
import static rest.service.loanService.controllers.LoanDateTimeController.getSrvDateTimePlusSec
import static rest.service.loanService.enums.LoanTypeName.PDL_DOPOLUCHKINO
import static rest.service.utils.ReqHelper.getDate
import static rest.service.utils.ReqHelper.getNewLoanId
import static dataBaseService.ManipulationDao.getTariffFromDb
import static utils.JsonParserHelper.getValueFromJson

class NegativeBaseKafkaTest extends Specification {

    private static Map loanBody
    private static Map customerReceivedMoneyReq
    private static Map cancelLoanAgreementReq
    private static LoanUpdatedDto loanUpdatedMessage

    def setupSpec() {
        // Если в предыдущих тестах были ошибки, вычитаем их, чтобы они не помешали текущим
        cleanTopics([BILLING_ERRORS, MSG_PROCESSING_ERROR], DOPOLUCHKINO)
    }

    def setup() {
        loanBody = getLoanBodyReq()
        customerReceivedMoneyReq = getCustomerReceivedMoneyMsg(loanBody)
        cancelLoanAgreementReq = getCancelLoanAgreementMsg(loanBody)
    }

    @Unroll
    def "create loan without transferTime"() {
        when:
        customerReceivedMoneyReq.payload.remove('transferTime')
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)

        then:
        checkFailureKafka(customerReceivedMoneyReq,
                "Field 'payload.transferTime' can't be null.",
                DOPOLUCHKINO)
    }

    def "create loan with transferTime in future"() {
        when:
        String futureTransferTime = getSrvDateTimePlusSec(10)
        customerReceivedMoneyReq.payload.transferTime = futureTransferTime.concat("+07:00")
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)

        and:
        MsgProcessingErrorDto msgProcessingError = getMsgProcessingError(DOPOLUCHKINO, 3000)
        List<MessageDto> billingErrors = getBillingErrors(DOPOLUCHKINO)

        then:
        assert billingErrors.size() == 1
        assert msgProcessingError.error.startsWith("transferTime=$futureTransferTime with the difference bigger than it's allowed")
        assert msgProcessingError.originalMessage == customerReceivedMoneyReq
        assert getValueFromJson(billingErrors.first().messagePayload, Map) == customerReceivedMoneyReq
    }

    @Unroll
    def "create loan with wrong #testCase"() {
        when:
        customerReceivedMoneyReq.payload.agreement.loanAmount = (amount * 100) as Long
        customerReceivedMoneyReq.payload.agreement.loanTerm = term
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)

        then:
        checkFailureKafka(customerReceivedMoneyReq,
                "No such tariff: amount = '${amount}', loanTerm = '${term}', loan type name = '${PDL_DOPOLUCHKINO.name()}', date = ${getDate()}.",
                DOPOLUCHKINO)

        where:
        amount   | term | testCase
        10000.01 | 7    | "amount"
        10000.00 | 32   | "term"
    }

    def "create two loan for one customer"() {
        when:
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)

        and:
        customerReceivedMoneyReq = getCustomerReceivedMoneyMsg(loanBody)
        customerReceivedMoneyReq.payload.agreement.id = getNewLoanId()
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)

        then:
        checkFailureKafka(customerReceivedMoneyReq,
                "Customer with id=${loanBody.customerId} cannot has more than one opened loan!",
                DOPOLUCHKINO)
    }

    def "create loan with field firstPaymentDate"() {
        when:
        customerReceivedMoneyReq.payload.agreement.firstPaymentDate = loanBody.loanDate
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)

        then:
        checkFailureKafka(customerReceivedMoneyReq, "The first payment date should be null for Loan with type 'PDL_DOPOLUCHKINO'.",
                DOPOLUCHKINO)
    }

    def "create loan with date earlier than tariff created"() {
        setup:
        Map tariff = getTariffFromDb(loanBody.amount, loanBody.loanTerm, loanBody.loanTypeName)
        String dateBeforeTariffCreate = LocalDate.parse(tariff.from_date as String).minusDays(1)

        when:
        customerReceivedMoneyReq.payload.transferTime = dateBeforeTariffCreate.concat("T10:10:10.101010+07:00")
        customerReceivedMoneyReq.payload.agreement.loanDate = dateBeforeTariffCreate
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)

        then:
        checkFailureKafka(customerReceivedMoneyReq,
                "No such tariff: "
                        .concat("amount = '${loanBody.amount / 100}.00', ")
                        .concat("loanTerm = '${loanBody.loanTerm}', ")
                        .concat("loan type name = '${loanBody.loanTypeName}', ")
                        .concat("date = ${dateBeforeTariffCreate}."),
                DOPOLUCHKINO)
    }
}
