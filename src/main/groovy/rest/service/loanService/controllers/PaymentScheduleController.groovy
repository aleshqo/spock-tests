package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.GetExistPaymentSchedule

import static java.util.Objects.isNull
import static rest.service.loanService.LoanConstants.*
import static rest.service.utils.ReqHelper.getDate

@Slf4j
class PaymentScheduleController {

    /** Get existing Payment Schedule */
    static def getPaymentScheduleByLoanId(def loanId, String date = null) {
        String currentDate = (isNull(date) ? getDate() : date)
        log.info("-> getPaymentScheduleByLoanId, loanId=[{}], current date=[{}]", loanId, currentDate)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_SCHEDULES}/${loanId}?date=${currentDate}",
                GetExistPaymentSchedule.class) as List<GetExistPaymentSchedule>
        log.info("<- getPaymentScheduleByLoanId response=[{}]", response)
        response
    }
}
