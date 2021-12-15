package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.*

import static LoanDateTimeController.getServerDateTime
import static java.util.Objects.isNull
import static java.util.Objects.nonNull
import static rest.service.loanService.LoanConstants.*
import static rest.service.loanService.controllers.LoanDateTimeController.getServerDate

@Slf4j
class LoanController {


    static List<GetLoanResponse> getLoansByCustomer(def customerId) {
        log.info("-> getLoansByCustomer, customerId=[{}]", customerId)
        def response = LOAN_SERVICE.get("${PATH_LOAN}?customerId=${customerId}", GetLoanResponse.class) as List<GetLoanResponse>
        log.info("<- getLoansByCustomer response=[{}]", response)
        response
    }

    static List<GetLoanResponse> getLoansByCustomerSorted(def customerId) {
        def response = LOAN_SERVICE.get("${PATH_LOAN}?customerId=${customerId}", GetLoanResponse.class) as List<GetLoanResponse>
        response.sort(new Comparator<GetLoanResponse>() {
            @Override
            int compare(GetLoanResponse o1, GetLoanResponse o2) {
                return o1.currentStatus <=> o2.currentStatus
            }
        })
        response.reverse()
    }

    static def createLoan(Map request) {
        log.info("-> CreateLoan request=[{}]", request)
        def response = LOAN_SERVICE.post(PATH_LOAN, request, CreateLoanResponse.class)
        log.info("<- CreateLoan response=[{}]", response)
        response
    }

    static def getLoanById(def id) {
        log.info("-> getLoanById, id=[{}]", id)
        def response = LOAN_SERVICE.get("${PATH_LOAN}/${id}", GetLoanResponse.class)
        log.info("<- getLoanById response=[{}]", response)
        response
    }

    static def cancelLoan(def loanId) {
        log.info("-> cancelLoan, loanId=[{}]", loanId)
        def response = LOAN_SERVICE.put("${PATH_LOAN}/${loanId}${PATH_CANCEL}?id=${loanId}")
        log.info("<- cancelLoan response=[{}]", response)
        response
    }

    static GetPskLoanResponse getPskLoan(def id) {
        log.info("-> getPskLoan request , id=[{}]", id)
        def response = LOAN_SERVICE.get("${PATH_LOAN}/${id}${PATH_PSK}", GetPskLoanResponse.class) as GetPskLoanResponse
        log.info("<- getPskLoan response=[{}]", response)
        response
    }

    /** Get loan data on date */
    static def getLoanDataOnDate(def loanId, def dateTime = getServerDateTime()) {
        log.info("-> getLoanDataOnDate, loanId=[{}], dateTime=[{}]", loanId, dateTime)
        String dataOnDate = "dataOnDate?"
        String path = isNull(dateTime) ? dataOnDate.concat("loanId=${loanId}") : dataOnDate.concat("dateTime=${dateTime}&loanId=${loanId}")
        def response = LOAN_SERVICE.get("${PATH_LOAN}/${path}", GetLoanDataOnDateResponse.class)
        log.info("<- getLoanDataOnDate response=[{}]", response)
        response
    }

    /** Stop loan calculation due to fraud */
    static def stopLoanByFraud(def loanId) {
        log.info("-> stopLoanByFraud, loanId=[{}]", loanId as String)
        def response = LOAN_SERVICE.put("${PATH_LOAN}${PATH_STOP}?loanId=${loanId}", [:], null)
        log.info("<- stopLoanByFraud response=[{}]", response)
        response
    }

    /** Stop customer loans calculations due to fraud */
    static def stopCustomerLoansByFraud(def customerId) {
        log.info("-> stopCustomersLoanByFraud, customerId=[{}]", customerId as String)
        def response = LOAN_SERVICE.put("${PATH_LOAN}${PATH_STOP}/all?customerId=${customerId}", [:], null)
        log.info("<- stopCustomersLoanByFraud response=[{}]", response)
        response
    }

    /** Resume loan calculation */
    static def resumeLoanCalculation(def loanId) {
        log.info("-> resumeLoanCalculation, loanId=[{}]", loanId as String)
        def response = LOAN_SERVICE.put("${PATH_LOAN}${PATH_RESUME_CALCULATION}?loanId=${loanId}", [:])
        log.info("<- resumeLoanCalculation response=[{}]", response)
        response
    }

    static def writeOffLoan(Map body) {
        log.info("-> writeOffLoan request=[{}]", body)
        def response = LOAN_SERVICE.put("${PATH_LOAN}/${body.id}${PATH_WRITE_OFF}", body, BooleanResponse)
        log.info("<- writeOffLoan response=[{}]", response)
        response
    }

    static def getLoanTransactions(def loanId, String fromDate = null, String toDate = null) {
        log.info("-> getLoanTransaction, loanId=[{}], fromDate=[{}], toDate=[{}]", loanId as String, fromDate, toDate)
        StringJoiner path = new StringJoiner("&")
        path.add("${PATH_TRANSACTIONS}?id=${loanId}")
        if (nonNull(fromDate)) {
            path.add("fromDate=${fromDate}")
        }
        if (nonNull(toDate)) {
            path.add("toDate=${toDate}")
        }
        def response = LOAN_SERVICE.get("${PATH_LOAN}/${loanId}${path.toString()}", GetLoanTransactionsResponse) as List<GetLoanTransactionsResponse>
        log.info("<- getLoanTransaction response=[{}]", response)
        response
    }

    static def closeBadLoan(def loanId) {
        Map body = [loanId: loanId]
        log.info("-> closeBadLoan, request=[{}]", body)
        def response = LOAN_SERVICE.put("${PATH_LOAN}${PATH_BAD_CLOSE}", body, BooleanResponse)
        log.info("<- closeBadLoan response=[{}]", response)
        response
    }

    static def sellLoan(def loanId, date = getServerDate()) {
        log.info("-> sellLoan, loanId=[{}], date=[{}]", loanId as String, date)
        StringJoiner path = new StringJoiner("&")
        path.add("${PATH_SELL_LOAN}?loanId=${loanId}")
        path.add("date=${date}")
        def response = LOAN_SERVICE.put("${PATH_LOAN}${path.toString()}")
        log.info("<- sellLoan response=[{}]", response)
        response
    }
}
