package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import static rest.service.loanService.LoanConstants.*

@Slf4j
class LoanDateTimeController {

    /** Get current date and time*/
    static String getServerDateTime(def daysToAdd = 0) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        String dateTime = get("${PATH_SERVICE}${PATH_GET_DATE}").split('\\.')[0]
        String res = LocalDateTime.parse(dateTime).plusDays(daysToAdd).format(formatter)
        log.info("getServerDateTime=[{}]", res)
        res
    }

    static String getServerTime() {
        getServerDateTime().split('T').last()
    }

    static getSrvDateTimeMinusDay(int day = 1) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        String dateTime = get("${PATH_SERVICE}${PATH_GET_DATE}").split('\\.')[0]
        LocalDateTime.parse(dateTime).minusDays(day).format(formatter)
    }

    static getSrvDateTimeMinusSec(int sec = 1) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        String dateTime = get("${PATH_SERVICE}${PATH_GET_DATE}").split('\\.')[0]
        LocalDateTime.parse(dateTime).minusSeconds(sec).format(formatter)
    }

    static getSrvDateTimePlusSec(int secToAdd = 1) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        String dateTime = get("${PATH_SERVICE}${PATH_GET_DATE}").split('\\.')[0]
        LocalDateTime.parse(dateTime).plusSeconds(secToAdd).format(formatter)
    }

    static getSrvDateTimePlusMonth(int monthsToAdd = 1) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        String dateTime = get("${PATH_SERVICE}${PATH_GET_DATE}").split('\\.')[0]
        LocalDateTime.parse(dateTime).plusMonths(monthsToAdd).format(formatter)
    }

    static String getServerDate(def daysToAdd = 0) {
        String res = LocalDate.parse(get("${PATH_SERVICE}${PATH_GET_DATE}").split('T')[0]).plusDays(daysToAdd)
        log.info("getServerDate=[{}]", res)
        res
    }

    static String getServerMonth(def monthsToAdd = 0) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM")
        String date = get("${PATH_SERVICE}${PATH_GET_DATE}").split('T')[0]
        String res = LocalDate.parse(date).plusMonths(monthsToAdd).format(formatter)
        log.info("getServerMonth=[{}]", res)
        res
    }

    static getSrvDateMinusDay(def day = 1) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        String date = get("${PATH_SERVICE}${PATH_GET_DATE}").split('T')[0]
        LocalDate.parse(date).minusDays(day).format(formatter)
    }

    /** Clear mocked date */
    static def clearMockedDate() {
        log.info("clearMockedDate")
        get("${PATH_SERVICE}${PATH_CLEAR_DATE}")
    }

    /** Mock system dateTime */
    static void mockDateTime(String dateTime) {
        log.info("-> mockDateTime, date=[{}]", dateTime)
        LOAN_SERVICE.put("${PATH_SERVICE}${PATH_DATE_TIME}/dateTime?dateTime=${dateTime}")
    }

    private static String get(String path) {
        HttpBuilder.configure {
            request.uri = LOAN_SERVICE_URI + path
            request.contentType = 'text/plain'
        }.get {
            response.success { FromServer fs, Object body ->
                return body
            }
        }
    }
}
