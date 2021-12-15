package tests.dopoluchkino.kafka.base


import kafka.dto.response.LoanUpdatedDto
import spock.lang.Specification

import static helper.KafkaRequests.getCancelLoanAgreementMsg
import static helper.KafkaRequests.getCustomerReceivedMoneyMsg
import static helper.VerifyResponse.checkLoan
import static helper.VerifyResponseKafka.checkSuccessKafka
import static kafka.service.CancelLoanAgreementService.cancelLoanAgreement
import static kafka.service.CleanTopicService.cleanTopics
import static kafka.service.CustomerReceivedMoneyService.sendCustomerReceivedMoneyMsg
import static kafka.service.LoanUpdatedService.getLoanUpdated
import static kafka.topics.TopicName.BILLING_ERRORS
import static kafka.topics.TopicName.LOAN_UPDATED
import static kafka.topics.TopicName.MSG_PROCESSING_ERROR
import static kafka.topics.TopicPrefixName.DOPOLUCHKINO
import static helper.restrequests.DopoluchkinoRestRequests.getLoanBodyReq
import static rest.service.loanService.enums.LoanStatus.CANCELLED
import static rest.service.loanService.enums.LoanStatus.MISTAKENLY_ISSUED
import static rest.service.loanService.enums.LoanStatus.OPENED
import static dataBaseService.ManipulationDao.getDebtsFromDB

class BaseKafkaLoanTest extends Specification {

    private static Map loanBody
    private static Map customerReceivedMoneyReq
    private static Map cancelLoanAgreementReq
    private static LoanUpdatedDto loanUpdatedMessage

    def setupSpec() {
        cleanTopics([BILLING_ERRORS, MSG_PROCESSING_ERROR, LOAN_UPDATED], DOPOLUCHKINO)
    }

    def setup() {
        loanBody = getLoanBodyReq()
        customerReceivedMoneyReq = getCustomerReceivedMoneyMsg(loanBody)
        cancelLoanAgreementReq = getCancelLoanAgreementMsg(loanBody)
    }

    def "creating a loan with the flag mistakenlyIssued = true through kafka"() {
        setup: "Создаем ошибочный займ через Kafka"
        loanBody.mistakenlyIssued = true
        customerReceivedMoneyReq = getCustomerReceivedMoneyMsg(loanBody)
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)

        when: "Получаем данные займа через Kafka"
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)

        then: "Проверяем полученные данные"
        assert loanUpdatedMessage.currentStatus == MISTAKENLY_ISSUED.name().toLowerCase()
        assert loanUpdatedMessage.loanId == loanBody.id
        assert loanUpdatedMessage.loanDate == loanBody.loanDate
        assert loanUpdatedMessage.amount == 1000000
    }

    def "check create loan using topic customerReceivedMoney"() {
        when:
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)
        Map debts = getDebtsFromDB(loanBody.id)

        then:
        checkSuccessKafka(loanBody, loanUpdatedMessage, OPENED.name(), DOPOLUCHKINO)
        checkLoan(loanBody, OPENED.name())
        assert debts.main_debt == loanBody.amount
        assert debts.interest == null
        assert debts.overdue_interest == null
        assert debts.fine == null

        when: "отменяем займ"
        cancelLoanAgreement(cancelLoanAgreementReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)
        debts = getDebtsFromDB(loanBody.id)

        then:
        checkSuccessKafka(loanBody, loanUpdatedMessage, CANCELLED.name(), DOPOLUCHKINO)
        checkLoan(loanBody, CANCELLED.name())
        assert debts.main_debt == 0
        assert debts.interest == null
        assert debts.overdue_interest == null
        assert debts.fine == null
    }

    def "check create with transferTime loan using topic customerReceivedMoney"() {
        when:
        sendCustomerReceivedMoneyMsg(customerReceivedMoneyReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)
        Map debts = getDebtsFromDB(loanBody.id)

        then:
        checkSuccessKafka(loanBody, loanUpdatedMessage, OPENED.name(), DOPOLUCHKINO)
        checkLoan(loanBody, OPENED.name())
        assert debts.main_debt == loanBody.amount
        assert debts.interest == null
        assert debts.overdue_interest == null
        assert debts.fine == null

        when: "отменяем займ"
        cancelLoanAgreement(cancelLoanAgreementReq, DOPOLUCHKINO)
        loanUpdatedMessage = getLoanUpdated(DOPOLUCHKINO)
        debts = getDebtsFromDB(loanBody.id)

        then:
        checkSuccessKafka(loanBody, loanUpdatedMessage, CANCELLED.name(), DOPOLUCHKINO)
        checkLoan(loanBody, CANCELLED.name())
        assert debts.main_debt == 0
        assert debts.interest == null
        assert debts.overdue_interest == null
        assert debts.fine == null
    }
}
