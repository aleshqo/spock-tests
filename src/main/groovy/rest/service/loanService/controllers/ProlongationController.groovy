package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.prolongation.CreateProlongationViaOrderResponse
import rest.service.loanService.response.prolongation.GetAvailProlongResponse
import rest.service.loanService.response.prolongation.GetProlongationResponse

import static rest.service.loanService.LoanConstants.*

@Slf4j
class ProlongationController {

    /** Get Prolongation */
    static def getProlongations(def loanId) {
        log.info("-> getProlongations, body=[{}]", loanId as String)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_PROLONGATION}?loanId=${loanId}", GetProlongationResponse)
        log.info("<- getProlongations response=[{}]", response)
        response
    }

    /** Create prolongation via order */
    static def createProlongationViaOrder(Map body) {
        log.info("-> createProlongationViaOrder, body=[{}]", body)
        def response = LOAN_SERVICE.post("${PATH_SERVICE}${PATH_PROLONGATION}", body, CreateProlongationViaOrderResponse)
        log.info("<- createProlongationViaOrder response=[{}]", response)
        response
    }

    /** Retrieve list of possible prolongations by loanId */
    static def getAvailableProlongation(def loanId, def beginDate) {
        log.info("-> getAvailProlongation, loanId=[{}], beginDate=[{}]", loanId, beginDate)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_PROLONGATION}/available?beginDate=${beginDate}&loanId=${loanId}",
                GetAvailProlongResponse.class)
        log.info("<- getAvailProlongation response=[{}]", response)
        response
    }
}
