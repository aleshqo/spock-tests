package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.CancelInterestDiscountResponse
import rest.service.loanService.response.interestDiscount.CreateDiscountManuallyResponse
import rest.service.loanService.response.interestDiscount.GetFullInterestDiscAmountResponse
import rest.service.loanService.response.interestDiscount.GetInterestDiscHistResponse
import rest.service.loanService.response.interestDiscount.GetInterestDiscountResponse

import static LoanDateTimeController.getServerDate
import static rest.service.loanService.LoanConstants.*

@Slf4j
class InterestDiscountController {

    /** Find interest discounts by loan id */
    static List<GetInterestDiscountResponse> findInterestDiscount(def loanId) {
        log.info("-> findInterestDiscount, loanId=[{}]", loanId as String)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_INTEREST_DISCOUNTS}?loanId=${loanId}", GetInterestDiscountResponse.class) as List<GetInterestDiscountResponse>
        log.info("<- findInterestDiscount response=[{}]", response)
        response
    }

    /** Get interest discounts history by loan id */
    static List<GetInterestDiscHistResponse> getInterestDiscountHistory(def loanId) {
        log.info("-> findInterestDiscount, loanId=[{}]", loanId as String)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_INTEREST_DISCOUNTS}${PATH_HISTORY}?loanId=${loanId}",
                GetInterestDiscHistResponse.class) as List<GetInterestDiscHistResponse>
        response.sort({ it.date })
        log.info("<- findInterestDiscount response=[{}]", response)
        response
    }

    /** Cancel interest discount by loanId and interest discount type. */
    static CancelInterestDiscountResponse cancelInterestDiscount(def loanId, def discountType) {
        log.info("-> cancelInterestDiscount, loanId=[{}]", loanId as String)
        Map body = ["interestDiscountType": discountType, "loanId": loanId]
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_INTEREST_DISCOUNTS}", body,
                CancelInterestDiscountResponse.class) as CancelInterestDiscountResponse
        log.info("<- cancelInterestDiscount response=[{}]", response)
        response
    }

    /** Get amount of full interest discount */
    static def getFullInterestDiscAmount(def loanId) {
        log.info("-> getFullInterestDiscAmount, loanId=[{}]", loanId as String)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_INTEREST_DISCOUNTS}/full-amount?loanId=${loanId}",
                GetFullInterestDiscAmountResponse.class)
        log.info("<- getFullInterestDiscAmount response=[{}]", response)
        response
    }

    /** Create interest discount manually */
    static def createDiscountManually(def loanId, def type, def value, String fromDate = getServerDate(1)) {
        log.info("-> getFullInterestDiscAmount, loanId=[{}]", loanId as String)
        Map body = [
                "loanId"  : loanId,
                "type"    : type,
                "value"   : value,
                "fromDate": fromDate
        ]
        def response = LOAN_SERVICE.post("${PATH_SERVICE}${PATH_INTEREST_DISCOUNTS}", body,
                CreateDiscountManuallyResponse.class)
        log.info("<- getFullInterestDiscAmount response=[{}]", response)
        response
    }


}
