package rest.service.utils

import groovy.util.logging.Slf4j
import rest.service.calculationService.controllers.CalculateDateTimeController
import rest.service.calculationService.controllers.CalculationController
import rest.service.loanService.controllers.LoanDateTimeController
import rest.service.loanService.response.ErrorResponse

import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.concurrent.ThreadLocalRandom

import static rest.service.loanService.controllers.LoanDateTimeController.getServerDate

@Slf4j
class ReqHelper {

    private static final Integer INT32_MAX = 2147483646


    static String getDate(Long daysToAdd = 0) {
        LocalDate.now().plusDays(daysToAdd)
    }

    static String getPastDate(Long days) {
        LocalDate.now().minusDays(days)
    }

    static String getDateTime(Long daysToAdd = 0) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime.now().plusDays(daysToAdd)/*.plusMinutes(5)*/.format(formatter)
    }

    static String getPastDateTime(Long days = 1) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime.now().minusDays(days).format(formatter)
    }

    static String getDateTimeWithMs(Long daysToAdd = 0) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        LocalDateTime.now().plusDays(daysToAdd).format(formatter)
    }

    static String getPastDateTimeWithMs(Long days = 0) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SS")
        LocalDateTime.now().minusDays(days).format(formatter)
    }

    static String calcDueDate(def loanDate, def daysToAdd) {
        LocalDate.parse(loanDate).plusDays(daysToAdd)
    }

    static String addDays(def currentDate, def daysToAdd) {
        LocalDate.parse(currentDate).plusDays(daysToAdd)
    }

    static String addSecond(def currentDateTime, def sec = 1) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime.parse(currentDateTime).plusSeconds(sec).format(formatter)
    }

    static String minusDay(def currentDate, def minusDays = 1) {
        LocalDate.parse(currentDate).minusDays(minusDays)
    }

    static Integer getElapsedDays(String currentDate, String anotherDate) {
        ChronoUnit.DAYS.between(LocalDate.parse(currentDate), LocalDate.parse(anotherDate))
    }

    static BigDecimal calcPaymentAmount(def mainDebt, def interest, def loanTerm) {
        return mainDebt + (mainDebt * interest / 100 * loanTerm)
    }

    // остаток ОД * ставка СП * кол-во дней
    static BigDecimal calcInterest(def balanceDebt, Map loanReq, def percent = 1, String onDate = getServerDate()) {
        int term = loanReq.loanTerm as int
        int elapsedDays = Math.abs(getElapsedDays(onDate, loanReq.loanDate as String))
        int daysCount = (elapsedDays >= (term) ? term : elapsedDays)
        return getRoundedNum(balanceDebt * percent / 100) * daysCount
    }

    // остаток ОД * ставка ПП * кол-во дней просрочки
    @SuppressWarnings('GroovyAssignabilityCheck')
    static BigDecimal calcOverdueInterest(def balanceDebt, Map loanReq, def percent = 1, String onDate = getServerDate()) {
        int overdueDays = getOverdueDays(loanReq, onDate)
        return getInterestPerDay(balanceDebt, percent) * overdueDays
    }

    static BigDecimal getInterestPerDay(def balanceDebt, def percent = 1) {
        getRoundedNum(balanceDebt * percent / 100)
    }

    // остаток ОД * ставка П * кол-во дней просрочки
    @SuppressWarnings('GroovyAssignabilityCheck')
    static BigDecimal calculateFine(def balanceDebt, Map loanReq, def finePercent = 0.2, String onDate = getServerDate()) {
        BigDecimal finePerDay = getFinePerDay(balanceDebt, finePercent)
        int overdueDays = getOverdueDays(loanReq, onDate)
        return finePerDay * overdueDays
    }

    static BigDecimal getFinePerDay(def balanceDebt, finePercent = 0.2) {
        getRoundedNum(balanceDebt * (finePercent / 365))
    }

    @SuppressWarnings('GroovyAssignabilityCheck')
    static def getOverdueDays(Map loanReq, String onDate) {
        String currentSrvDate = onDate
        String overdueDate = LocalDate.parse(loanReq.loanDate).plusDays(loanReq.loanTerm)
        int elapsedDays = getElapsedDays(currentSrvDate, overdueDate)
        if (elapsedDays >= 0) {
            return 0
        }
        return Math.abs(elapsedDays)
    }

    static def getRoundedNum(BigDecimal num) {
        return num.setScale(0, RoundingMode.DOWN)
    }

    static Integer getNewLoanId() {
        Integer id = getNewId()
        log.info("Loan id=[{}]", id)
        id
    }

    static Integer getNewCustomerId() {
        Integer id = getNewId()
        log.info("Customer id=[{}]", id)
        id
    }

    static Integer getNewExternalId() {
        Integer id = getNewId()
        log.info("External id=[{}]", id)
        id
    }

    static Integer getNewId() {
        ThreadLocalRandom.current().nextInt(1, INT32_MAX)
    }

    static def waitForResponse(Closure action, Closure condition) {
        int count = 0
        def response = action.call()

        while (!condition(response) && count <= 10) {
            response = action.call()
            log.info("try to get response, count: ${count}, response: ${response}")
            sleep 275
            ++count
        }
        return response
    }

    static def waitResponse(Closure action, Closure condition) {
        int count = 0
        def response = action.call()
        while (!condition(response) && count <= 10) {
            response = action.call()
            log.info("try to get response, count: ${count}, response: ${response}")
            sleep 275
            ++count
        }
        return response
    }

    // todo: зарефакторить на calculateToDate
    static void mockDate(def dateTime) {
        LoanDateTimeController.mockDateTime(dateTime)
        CalculateDateTimeController.mockDateTime(dateTime)
    }

    static void calculateToDate(def loanId, def dateTime) {
//        calculateDayByDay(loanId, dateTime.split('T').first())
        LoanDateTimeController.mockDateTime(dateTime)
        CalculateDateTimeController.mockDateTime(dateTime)
        CalculationController.calculateLoan(loanId)
    }

    static void calculateToDateByCustomer(List customerIds, def dateTime) {
        LoanDateTimeController.mockDateTime(dateTime)
        CalculateDateTimeController.mockDateTime(dateTime)
        CalculationController.calculateLoansByCustomerIds(customerIds)
    }


    static void clearDate() {
        LoanDateTimeController.clearMockedDate()
        CalculateDateTimeController.clearMockedDate()
    }

    static boolean checkFailure(ErrorResponse response, def code, String message) {
        assert response.statusCode == code
        assert response.message == message
        true
    }

    static calcCharges(def mainDebt, Map loanBody, def percent = 1, String onDate = getServerDate()) {
        return calcOverdueInterest(mainDebt, loanBody, percent, onDate) +
                calcInterest(mainDebt, loanBody, percent, onDate) +
                calculateFine(mainDebt, loanBody, 0.2, onDate)
    }

    static calOverdueCharges(def mainDebt, Map loanBody) {
        return calcOverdueInterest(mainDebt, loanBody) + calculateFine(mainDebt, loanBody)
    }
}
