package tests.dopoluchkino.negative

import rest.service.loanService.response.ErrorResponse
import spock.lang.Specification

import static dataBaseService.ManipulationDao.getLoanStatusFromDb
import static dataBaseService.ManipulationDao.getPaymentData
import static helper.Wrappers.sendLoan
import static helper.restrequests.DopoluchkinoRestRequests.getLoanBodyReq
import static helper.restrequests.DopoluchkinoRestRequests.getPaymentBodyReq
import static rest.service.loanService.controllers.LoanDateTimeController.getServerDateTime
import static rest.service.loanService.controllers.PaymentController.createPayment
import static rest.service.loanService.enums.LoanStatus.OPENED
import static rest.service.utils.ReqHelper.checkFailure

class NegativePaymentsTest extends Specification {

    private static Map loanBody
    private static Map paymentBody
    private static ErrorResponse errorResponse

    def setup() {
        loanBody = getLoanBodyReq()
        paymentBody = getPaymentBodyReq(null, loanBody.customerId, loanBody.id)
    }

    def "payment with wrong amount format"() {
        setup:
        sendLoan(loanBody)

        when: "отправляем платёж в формате с точкой"
        paymentBody.amount = 1500000.00
        paymentBody.loanId = null
        errorResponse = createPayment(paymentBody) as ErrorResponse

        then:
        checkFailure(errorResponse, 406, "Money format ${paymentBody.amount} expects no decimal point for this project. ")
        assert getLoanStatusFromDb(loanBody.id)
        assert getPaymentData(loanBody.id, paymentBody.externalId) == []
    }

    def "payment with future date"() {
        setup:
        sendLoan(loanBody)

        when:
        paymentBody.amount = 1500000
        paymentBody.loanId = loanBody.id
        paymentBody.paymentTime = getServerDateTime(1)
        errorResponse = createPayment(paymentBody) as ErrorResponse

        then:
        checkFailure(errorResponse, 400, "Payment time should be in past or present")
        assert getLoanStatusFromDb(loanBody.id) == OPENED.name()
        assert getPaymentData(loanBody.id, paymentBody.externalId) == []
    }
}
