package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.BalanceResponse

import static rest.service.loanService.LoanConstants.*

@Slf4j
class BalanceController {

    /** Get customer balance for given customer id */
    static def getBalance(def customerId) {
        log.info("getLoanById, id=[{}]", customerId)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_BALANCES}?customerId=${customerId}", BalanceResponse.class)
        log.info("getLoanById response=[{}]", response)
        response
    }
}
