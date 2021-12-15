package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.*
import rest.service.loanService.response.freezing.GetFreezeAvailResponse
import rest.service.loanService.response.freezing.GetFreezesResponse
import rest.service.loanService.response.freezing.ProlongFreezingResponse

import static java.util.Objects.nonNull
import static rest.service.loanService.LoanConstants.*

@Slf4j
class FreezingController {

    /** Retrieve list of loan freezings */
    static def getFreezesByLoanId(def loanId) {
        log.info("-> GetFreezesByLoanId, loanId=[{}]", loanId as String)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_FREEZINGS}?loanId=${loanId}", GetFreezesResponse.class)
        log.info("<- GetFreezesByLoanId response=[{}]", response)
        response
    }

    /** Activate freezing */
    static def addFreezing(Map body) {
        log.info("-> addFreezing, body=[{}]", body)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_FREEZINGS}", body, AddFreezingResponse.class)
        log.info("<- GetFreezesByLoanId response=[{}]", response)
        response
    }

    /** Check freezing creation availability */
    static def getFreezingAvailability(Map body) {
        log.info("-> getFreezingAvailability, body=[{}]", body)
        StringJoiner resultPath = new StringJoiner("&")
        body.forEach({ k, v ->
            if (nonNull(v)) {
                resultPath.add("${k}=${v}")
            }
        })
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_FREEZINGS}/availability?${resultPath}", GetFreezeAvailResponse.class)
        log.info("<- getFreezingAvailability response=[{}]", response)
        response
    }

    /** Cancel freezing */
    static def cancelFreezing(def loanId) {
        log.info("-> cancelFreezing, loanId=[{}]", loanId)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_FREEZINGS}${PATH_CANCEL}", [loanId: loanId], BooleanResponse.class)
        log.info("<- cancelFreezing response=[{}]", response)
        response
    }

    /** Prolong freezing */
    static def prolongFreezing(def loanId, def toDate) {
        log.info("-> prolongFreezing, loanId=[{}], toDate=[{}]", loanId, toDate)
        Map body = [loanId: loanId, toDate: toDate]
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_FREEZINGS}${PATH_PROLONG}", body, ProlongFreezingResponse.class)
        log.info("<- prolongFreezing response=[{}]", response)
        response
    }
}
