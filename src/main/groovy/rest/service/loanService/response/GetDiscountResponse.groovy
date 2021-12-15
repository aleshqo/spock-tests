package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonUnwrapped

class GetDiscountResponse {


    @JsonUnwrapped
    List<Discounts> discounts


    @Override
    String toString() {
        return "GetDiscountResponse{" +
                "discounts=" + discounts +
                '}'
    }
}
