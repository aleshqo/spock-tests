package rest.service.loanService.response.freezing

import com.fasterxml.jackson.annotation.JsonProperty

class GetFreezeAvailResponse {

    @JsonProperty("result")
    Boolean result

    @JsonProperty("maxAvailableAmount")
    BigDecimal maxAvailableAmount


    @Override
    String toString() {
        return "GetFreezeAvailResponse{" +
                "result=" + result +
                ", maxAvailableAmount=" + maxAvailableAmount +
                '}'
    }
}
