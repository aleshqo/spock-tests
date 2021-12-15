package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.GetDebtsResponse

import static LoanDateTimeController.getServerDateTime
import static java.util.Objects.nonNull
import static rest.service.loanService.LoanConstants.LOAN_SERVICE
import static rest.service.loanService.LoanConstants.PATH_SERVICE

@Slf4j
class DebtsController {

    /** Find current debts by loan id */
    static def getCurrentDebtsByLoan(def loanId) {
        log.info("-> getDebs, loanId=[{}]", loanId)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}/debts?loanId=${loanId}", GetDebtsResponse) as List<GetDebtsResponse>
        log.info("<- getDebs response=[{}]", response)
        response
    }

    static def getCurrentDebsOnDate(def loanId, String dateTime = getServerDateTime()) {
        log.info("-> getDebs, loanId=[{}], current date=[{}]", loanId, dateTime)
        StringJoiner resultPath = new StringJoiner("&")
        if (nonNull(dateTime)) {
            resultPath.add("dateTime=${dateTime}")
        }
        resultPath.add("loanId=${loanId}")
        def response = LOAN_SERVICE.get("${PATH_SERVICE}/debts?${resultPath}",
                GetDebtsResponse.class)
        log.info("<- getDebs response=[{}]", response)
        response
    }

    static def getCurrentDebs(def loanId) {
        log.info("-> getDebs, loanId=${loanId}")
        def response = LOAN_SERVICE.get("${PATH_SERVICE}/debts?loanId=${loanId}",
                GetDebtsResponse.class)
        log.info("<- getDebs response=[{}]", response)
        response
    }
}
