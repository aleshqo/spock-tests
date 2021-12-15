package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class Transactions {

    @JsonProperty("amount")
    Long amount
    @JsonProperty("issuedTime")
    String issuedTime
    @JsonProperty("loanId")
    Long loanId
    @JsonProperty("type")
    String type


    @Override
    String toString() {
        return "Transactions{" +
                "amount=" + amount +
                ", issuedTime='" + issuedTime + '\'' +
                ", loanId=" + loanId +
                ", type='" + type + '\'' +
                '}'
    }
}
