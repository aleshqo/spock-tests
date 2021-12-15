package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.GetCustomerScoringInfoResponse
import rest.service.loanService.response.GetLoanResponse

import static rest.service.loanService.LoanConstants.*

@Slf4j
class ScoringController {

    //http://localhost:8080/loan-service/scoring/customer-info?customerId=1
    /** Get scoring info for given customer id */
    static def getCustomerScoringInfo(def customerId) {
        log.info("-> getScoringInfo, id=[{}]", customerId as String)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_SCORING}/customer-info?customerId=${customerId}", GetCustomerScoringInfoResponse.class)
        log.info("<- getScoringInfo response=[{}]", response)
        response
    }
}
