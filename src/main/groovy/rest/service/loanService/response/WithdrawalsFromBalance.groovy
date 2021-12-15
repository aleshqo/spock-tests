package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class WithdrawalsFromBalance {


    @JsonProperty("amount")
    BigDecimal amount
    @JsonProperty("issuedTime")
    String issuedTime
    @JsonProperty("loanId")
    Integer loanId
    @JsonProperty("type")
    String type


    @Override
    String toString() {
        return "WithdrawalsFromBalance{" +
                "amount=" + amount +
                ", issuedTime='" + issuedTime + '\'' +
                ", loanId=" + loanId +
                ", type='" + type + '\'' +
                '}'
    }
}
