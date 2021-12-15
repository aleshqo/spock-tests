package helper.kzRequests

import rest.service.loanService.enums.LoanTypeName

import static rest.service.loanService.controllers.LoanDateTimeController.getServerDate
import static rest.service.utils.ReqHelper.getNewCustomerId
import static rest.service.utils.ReqHelper.getNewLoanId

class KzRestRequests {

    static Map getLoanBodyReq(Integer amount = 1000000, Integer term = 10, boolean offline = true) {
        [
                amount      : amount,
                customerId  : getNewCustomerId(),
                id          : getNewLoanId(),
                loanDate    : getServerDate(),
                loanTerm    : term,
                loanTypeName: LoanTypeName.PDL_KZ.name(),
                offline     : offline,
                tariffType  : "BASIC"
        ]
    }
}
