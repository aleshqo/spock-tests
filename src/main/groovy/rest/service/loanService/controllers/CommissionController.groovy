package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.Commissions
import rest.service.loanService.response.CommissionsResponse

import static rest.service.loanService.LoanConstants.*

@Slf4j
class CommissionController {

    /** Get commissions by customerId*/
    static List<Commissions> getCommissionsByCustomer(Integer customerId) {
        log.info("-> getCommissionsByCustomer, customerId=[{}]", customerId)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_COMMISSIONS}?customerId=${customerId}",
                Commissions.class) as List<Commissions>
        log.info("<- getCommissionsByCustomer response=[{}]", response)
        response
    }

    /** Get commissions by loanId*/
    static List<Commissions> getCommissionsByLoan(Integer loanId) {
        log.info("-> getCommissionsByLoan, loanId=[{}]", loanId)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_COMMISSIONS}?loanId=${loanId}", Commissions.class) as List<Commissions>
        log.info("<- getCommissionsByLoan response=[{}]", response)
        response
    }

    /** Create commissions */
    static List<CommissionsResponse> createCommissions(Map body) {
        log.info("-> createCommissions, body=[{}]", body)
        def response = LOAN_SERVICE.post("${PATH_SERVICE}${PATH_COMMISSIONS}${PATH_CREATE}", body, CommissionsResponse.class) as List<CommissionsResponse>
        log.info("<- createCommissions response=[{}]", response)
        response
    }

    /** Cancel commissions */
    static def cancelCommission(Integer commissionId) {
        log.info("-> cancelCommission, commissionId=[{}]", commissionId)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_COMMISSIONS}/${commissionId}${PATH_CANCEL}")
        log.info("<- cancelCommission response=[{}]", response)
        response
    }

    /** Cancel unpaid commissions */
    static def cancelUnpaidCommission(Integer commissionId) {
        log.info("-> cancelUnpaidCommission, commissionId=[{}]", commissionId)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_COMMISSIONS}/${commissionId}${PATH_CANCEL_UNPAID}")
        log.info("<- cancelUnpaidCommission response=[{}]", response)
        response
    }

}
