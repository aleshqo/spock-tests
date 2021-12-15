package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.TransactionsResponse

import static rest.service.loanService.LoanConstants.LOAN_SERVICE
import static rest.service.loanService.LoanConstants.PATH_SERVICE

@Slf4j
class TransactionsController {

    /** Get loan transactions */
    static def getTransactionsByLoan(def loanId) {
        log.info("-> getTransactions, loanId=[{}]", loanId)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}/loans/${loanId}/transactions",
                List<TransactionsResponse>.class)
        log.info("<- getTransactions response=[{}]", response)
        (List<TransactionsResponse>) response
    }
}
