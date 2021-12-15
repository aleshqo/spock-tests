package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.Debts
import rest.service.loanService.response.NotificationResponse
import rest.service.loanService.response.prolongation.ProlongationOrderDataResponse

import static LoanDateTimeController.getServerDate
import static rest.service.loanService.LoanConstants.*

@Slf4j
class TestController {

    /** Get all actions on date by loanId */
    static List<NotificationResponse> getNotificationData(def loanId, def date) {
        log.info("-> getNotificationData, loanId=[{}], date=[{}], addData=[{}]", loanId, date)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_TEST}/getNotificationData?addData=true&date=${date}&loanId=${loanId}",
                NotificationResponse) as List<NotificationResponse>
        log.info("-> getNotificationData, response=[{}]", response)
//        response.sort({ it.id })
        response
    }

    /** Get all actions on date by loanId (sorted) */
    static List<NotificationResponse> getNotificationDataSorted(def loanId, def date) {
        def response = getNotificationData(loanId, date) as List<NotificationResponse>
        response.sort(new Comparator<NotificationResponse>() {
            @Override
            int compare(NotificationResponse o1, NotificationResponse o2) {
                return o1.getData().getDebts().mistakenlyIssuedDebt <=> o2.getData().getDebts().mistakenlyIssuedDebt
            }
        })
        response
    }

    /** Get accrual data by loanId and charge date */
    static Debts getChargeDataOnDate(def loanId, def date = getServerDate()) {
        log.info("-> getChargeDataOnDate, loanId=[{}], date=[{}]", loanId, date)
        Debts response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_TEST}/getChargeDataOnDate?chargeDate=${date}&loanId=${loanId}",
                Debts.class) as Debts
        log.info("-> getChargeDataOnDate, response=[{}]", response)
        response
    }

    /** Get prolongation order data by loan id */
    static def getProlongationOrderData(def loanId) {
        log.info("-> getProlongationOrderData, loanId=[{}]", loanId as String)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_TEST}/prolongationOrderData/${loanId}",
                ProlongationOrderDataResponse.class)
        log.info("<- getProlongationOrderData, response=[{}]", response)
        response as List<ProlongationOrderDataResponse>
    }

    static def calculateDayByDay(def loanId, def toDate) {
        log.info("-> calculateDateByDate request, loanId=[{}], date=[{}]", loanId, toDate)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_TEST}${PATH_CALCULATE}/dayByDay?date=${toDate}&loanId=${loanId}",
                [:], ProlongationOrderDataResponse.class)
        log.info("-> calculateDateByDate, response=[{}]", response)
        response as List<ProlongationOrderDataResponse>
    }
}
