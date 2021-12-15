package rest.service.loanService.controllers

import groovy.util.logging.Slf4j

import static java.util.Objects.nonNull
import static rest.service.loanService.LoanConstants.*

@Slf4j
class BankruptController {

    /** Bankrupt customer */
    static def bankruptCustomer(def customerId, def date) {
        log.info("-> bankruptCustomer, customerId=[{}], date=[{}]", customerId, date)
        Map body = [customerId: customerId, date: date]
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_BANKRUPT}", body)
        log.info("<- bankruptCustomer response=[{}]", response)
        response
    }

    /** Stop loan calculation due to bankruptcy */
    static def bankruptcyStopLoan(def loanId, def date) {
        log.info("-> bankruptLoan, loanId=[{}], date=[{}]", loanId, date)
        Map body = [loanId: loanId]
        if (nonNull(date)) {
            body.put('date', date)
        }
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_BANKRUPT}/stop", body)
        log.info("<- bankruptLoan response=[{}]", response)
        response
    }

    /** Stop customer loans calculations due to bankruptcy*/
    static def bankruptcyStopAllCustomerLoans(def customerId, def date) {
        log.info("-> bankruptLoan, customerId=[{}], date=[{}]", customerId, date)
        Map body = [customerId: customerId]
        if (nonNull(date)) {
            body.put('date', date)
        }
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_BANKRUPT}/stop/all", body)
        log.info("<- bankruptLoan response=[{}]", response)
        response
    }
}
