package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.exception.SpockBillingException
import rest.service.loanService.response.GetPaymentsResponse

import static java.util.Objects.isNull
import static java.util.Objects.nonNull
import static rest.service.loanService.LoanConstants.*

@Slf4j
class PaymentController {


    /** Get Payments */
    static def getPayments(def loanId = null, def customerId = null) {
        if (isNull(loanId) && isNull(customerId)) {
            throw new SpockBillingException("One of the following elements should be set: loanId, customerId")
        }
        String pathCustomerId = isNull(customerId) ? "" : "clientId=${customerId}"
        String pathLoanId = isNull(loanId) ? "" : "loanId=${loanId}"
        String and = nonNull(customerId) && nonNull(loanId) ? "&" : ""
        log.info("-> getPayments request, customerId=[{}], loanId=[{}]", customerId, loanId)

        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_PAYMENTS}?${pathCustomerId}${and}${pathLoanId}",
                GetPaymentsResponse.class) as List<GetPaymentsResponse>
        log.info("<- getPayments response=[{}]", response)
        response
    }

    /** Return payment back to the customer */
    static def givePaymentBack(def externalId) {
        log.info("-> givePaymentBack, externalId=[{}]", externalId)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_PAYMENTS}${PATH_GIVE_PAYMENT_BACK}?externalId=${externalId}", null)
        log.info("<- givePaymentBack, response=[{}]", response)
        response
    }

    /** Create Payment */
    static def createPayment(Map request) {
        log.info("-> createPayment request, body=[{}]", request)
        def response = LOAN_SERVICE.post("${PATH_SERVICE}${PATH_PAYMENTS}", request, null)
        log.info("<- createPayment, response=[{}]", response)
        response
    }

    /** Reserve funds on balance */
    static def reserveBalance(def customerId, def amount) {
        Map body = ["amount": amount, "customerId": customerId]
        log.info("-> reserveBalance request, body=[{}]", body)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_PAYMENTS}${PATH_RESERVE_BALANCE}", body)
        log.info("<- reserveBalance, response=[{}]", response)
        response
    }

    /** Cancel funds reservation on balance */
    static def cancelReservation(def customerId) {
        log.info("-> cancelReservation request, customerId=[{}]", customerId as String)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_PAYMENTS}${PATH_CANCEL_RESERVATION}?customerId=${customerId}")
        log.info("<- cancelReservation, response=[{}]", response)
        response
    }


    /** Cancel Payment by external id */
    static def cancelPayment(def externalId) {
        log.info("-> cancelPayment request, externalId=[{}]", externalId as String)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_PAYMENTS}?externalId=${externalId}")
        log.info("<- cancelPayment, response=[{}]", response)
        response
    }

    /** Withdraw funds from balance */
    static def withdrawFromBalance(def customerId, def amount) {
        Map body = ["amount": amount, "customerId": customerId]
        log.info("-> withdrawFromBalance, body=[{}]", body)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_PAYMENTS}${PATH_WITHDRAW}", body)
        log.info("<- withdrawFromBalance, response=[{}]", response)
        response
    }

    /** Pay loan from customer's balance */
    static def payFromBalance(def amount, def loanId) {
        Map body = ["amount": amount, "loanId": loanId]
        log.info("-> payFromBalance, body=[{}]", body)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_PAYMENTS}${PATH_PAY_FROM_BALANCE}", body)
        log.info("<- payFromBalance, response=[{}]", response)
        response
    }
}
