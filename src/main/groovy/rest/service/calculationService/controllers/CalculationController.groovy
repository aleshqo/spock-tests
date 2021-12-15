package rest.service.calculationService.controllers

import groovy.util.logging.Slf4j
import rest.service.calculationService.response.CalculatedCustomerLoansResponse

import static rest.service.calculationService.CalculationConstants.*

@Slf4j
class CalculationController {

    /** Calculate loan */
    static void calculateLoan(def loanId) {
        log.info("calculateLoan, loanId=[{}]", loanId as String)
        CALCULATE_SERVICE.put("${PATH_SERVICE}${PATH_CALCULATION}/${loanId}")
    }

    static void calculateAllLoans() {
        log.info("calculateAllLoans")
        CALCULATE_SERVICE.put("${PATH_SERVICE}${PATH_CALCULATION}")
    }

    /** Calculate loans by customer ids */
    static void calculateLoansByCustomerIds(List customerIds) {
        log.info("calculateLoansByCustomerIds, ids={}", customerIds)
        def response = CALCULATE_SERVICE.put("${PATH_SERVICE}${PATH_CALCULATION}/customers", customerIds, CalculatedCustomerLoansResponse)
        log.info("-> calculateLoansByCustomerIds response=[]", response)
    }

    /** Calculate customer balances for given report period
     * For example: 2021-07
     * */
    static void calculateCustomerBalances(String reportPeriod) {
        log.info("calculateCustomerBalances, period=[{}]", reportPeriod)
        CALCULATE_SERVICE.post("${PATH_SERVICE}${PATH_CALCULATION}${PATH_CUSTOMER_BALANCES}/${reportPeriod}", [:], null)
    }

    /** Calculate overdue days for report period
     * For example: 2021-08
     * */
    static void calculateOverdueDays(String reportPeriod) {
        log.info("calculateOverdueDays, period=[{}]", reportPeriod)
        CALCULATE_SERVICE.put("${PATH_SERVICE}${PATH_CALCULATION}/overdueDays?reportPeriodName=${reportPeriod}", [:])
    }

    //http://localhost:8082/calculation-service/calculations/calculate?fromDate=2021-09-01&toDate=2021-09-02
    //http://localhost:8082/calculation-service/calculations/calculate?fromDate=2021-09-01&toDate=2021-09-02
    /** Calculate psk */
    static void calculatePsk(String fromDate, String toDate) {
        log.info("calculatePsk, fromDate=[{}], toDate=[{}]", fromDate, toDate)
        CALCULATE_SERVICE.post("${PATH_SERVICE}${PATH_CALCULATION}/calculate?fromDate=${fromDate}&toDate=${toDate}", [:], null)
    }
}
