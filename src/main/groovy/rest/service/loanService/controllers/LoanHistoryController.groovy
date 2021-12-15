package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.GetLoanHistoryResponse

import static rest.service.loanService.LoanConstants.*

@Slf4j
class LoanHistoryController {

    static List<GetLoanHistoryResponse> getLoanHistoryByLoanId(def loanId) {
        log.info("-> getLoanHistoryByLoanId, loanId=[{}]", loanId)
        def response = LOAN_SERVICE.get("${PATH_LOAN}/${loanId}${PATH_HISTORY}", GetLoanHistoryResponse.class) as List<GetLoanHistoryResponse>
        log.info("<- getLoansByCustomer response=[{}]", response)
        response.sort({ it.fromTime })
        response as List<GetLoanHistoryResponse>
    }
}
