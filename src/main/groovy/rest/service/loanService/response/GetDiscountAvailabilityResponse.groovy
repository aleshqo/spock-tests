package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class GetDiscountAvailabilityResponse {

    @JsonProperty("amountToForgive")
    Integer amountToForgive
    @JsonProperty("requiredAmount")
    Integer requiredAmount
    @JsonProperty("result")
    Boolean result


    @Override
    String toString() {
        return "GetDiscountAvailabilityResponse{" +
                "amountToForgive=" + amountToForgive +
                ", requiredAmount=" + requiredAmount +
                ", result=" + result +
                '}'
    }
}
