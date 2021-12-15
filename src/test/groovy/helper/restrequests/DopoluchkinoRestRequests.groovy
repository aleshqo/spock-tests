package helper.restrequests

import static rest.service.loanService.controllers.LoanDateTimeController.getServerDate
import static rest.service.loanService.controllers.LoanDateTimeController.getSrvDateTimeMinusSec
import static rest.service.utils.ReqHelper.*

class DopoluchkinoRestRequests {

    static Map getLoanBodyReq(Integer amount = 1000000, Integer term = 7) {
        [
                "amount"      : amount,
                "customerId"  : getNewCustomerId(),
                "id"          : getNewLoanId(),
                "loanDate"    : getServerDate(),
                "loanTerm"    : term,
                "loanTypeName": "PDL_DOPOLUCHKINO",
        ]
    }

    static Map getPaymentBodyReq(Integer amount, def custId, def loanId, String dateTime = getSrvDateTimeMinusSec(2)) {
        [
                "amount"           : amount,
                "customerId"       : custId,
                "externalId"       : getNewExternalId(),
                "loanId"           : loanId,
                "operationTypeName": "PAYMENT",
                "paymentTime"      : dateTime
        ]
    }

    static Map getFreezeBody(def loanId, Integer amount = 0, Integer term = 5, String fromDate = getServerDate(1)) {
        [
                "loanId"  : loanId,
                "fromDate": fromDate,
                "amount"  : amount,
                "term"    : term
        ]
    }

    static Map getConfirmProlongBodyReq(Map paymentBody, def term, def dateTime = getDateTime()) {
        [
                "externalId"    : paymentBody.externalId,
                "loanId"        : paymentBody.loanId,
                "term"          : term,
                "activationTime": dateTime
        ]
    }

    static Map getApplyCourtDecisionBodyReq(def loanId, String fromDate = getServerDate()) {
        [
                "loanId"         : loanId,
                "overdueInterest": 0,
                "interest"       : 0,
                "mainDebt"       : 0,
                "fine"           : 0,
                "stateFee"       : 0,
                "date"           : fromDate
        ]
    }
}
