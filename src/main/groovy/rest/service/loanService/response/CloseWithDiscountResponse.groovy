package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class CloseWithDiscountResponse {

    @JsonProperty("result")
    Boolean result


    @Override
    String toString() {
        return "CloseWithDiscountResponse{" +
                "result=" + result +
                '}'
    }
}
