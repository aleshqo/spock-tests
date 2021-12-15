package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class CancelInterestDiscountResponse {

    @JsonProperty("result")
    Boolean result


    @Override
    String toString() {
        return "CancelInterestDiscountResponse{" +
                "result=" + result +
                '}'
    }
}
