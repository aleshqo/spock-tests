package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class GetPskLoanResponse {

    @JsonProperty("value")
    BigDecimal value

    @Override
    String toString() {
        return "GetPskLoanResponse{" +
                "value=" + value +
                '}'
    }
}
