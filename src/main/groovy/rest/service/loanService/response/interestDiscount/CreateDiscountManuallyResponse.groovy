package rest.service.loanService.response.interestDiscount

import com.fasterxml.jackson.annotation.JsonProperty

class CreateDiscountManuallyResponse {

    @JsonProperty("result")
    boolean result


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("CreateDiscountManuallyResponse{")
        sb.append("result=").append(result)
        sb.append('}')
        return sb.toString()
    }
}
