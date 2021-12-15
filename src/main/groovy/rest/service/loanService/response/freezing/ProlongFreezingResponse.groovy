package rest.service.loanService.response.freezing

import com.fasterxml.jackson.annotation.JsonProperty

class ProlongFreezingResponse {

    @JsonProperty("result")
    Boolean result


    @Override
    String toString() {
        return "ProlongFreezingResponse{" +
                "result=" + result +
                '}'
    }
}
