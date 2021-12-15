package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class BooleanResponse {

    @JsonProperty("result")
    Boolean result


    @Override
    String toString() {
        return "BooleanResponse{" +
                "result=" + result +
                '}'
    }
}
