package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class CreateLoanResponse {

    @JsonProperty("id")
    Integer id


    @Override
    String toString() {
        return "CreateLoanResponse{" +
                "id=" + id +
                '}'
    }
}
