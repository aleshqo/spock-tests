package rest.service.loanService.response.prolongation

import com.fasterxml.jackson.annotation.JsonProperty

class CreateProlongationViaOrderResponse {

    @JsonProperty("id")
    BigInteger id


    @Override
    String toString() {
        return "CreateProlongationViaOrderResponse{" +
                "id=" + id +
                '}'
    }
}
