package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.response.CloseWithDiscountResponse
import rest.service.loanService.response.Discounts
import rest.service.loanService.response.GetDiscountAvailabilityResponse

import static rest.service.loanService.LoanConstants.*

@Slf4j
class DiscountController {


    /** Get discounts by loanId */
    static List<Discounts> getDiscount(def loanId) {
        log.info("-> getDiscount, loanId=[{}]", loanId)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_DISCOUNT}?loanId=${loanId}", Discounts.class) as List<Discounts>
        response.sort({ it.id })
        log.info("<- getDiscount response=[{}]", response)
        response
    }

    /** Check closure with discount availability */
    static def checkDiscountAvailability(def loanId) {
        log.info("-> checkDiscountAvailability, loanId=[{}]", loanId)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_DISCOUNT}/availability?loanId=${loanId}",
                GetDiscountAvailabilityResponse.class)
        log.info("<- checkDiscountAvailability response=[{}]", response)
        response
    }

    /** Close loan with discount if it’s available */
    static def closeLoanWithDiscount(def loanId) {
        log.info("-> closeLoanWithDiscount, loanId=[{}]", loanId)
        def response = LOAN_SERVICE.put("${PATH_SERVICE}${PATH_DISCOUNT}/${loanId}",
                [:], CloseWithDiscountResponse.class)
        log.info("<- closeLoanWithDiscount response=[{}]", response)
        response
    }
}
