package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class GetLoanTransactionsResponse {

    @JsonProperty("amount")
    Long amount
    @JsonProperty("issuedTime")
    String issuedTime
    @JsonProperty("loanId")
    Long loanId
    @JsonProperty("name")
    String name
    @JsonProperty("transactionDate")
    String transactionDate
    @JsonProperty("revokedById")
    Integer revokedById
    @JsonProperty("id")
    Integer id

    @JsonProperty("creditAccount")
    Integer creditAccount
    @JsonProperty("debitAccount")
    Integer debitAccount


    @Override
    public String toString() {
        return "GetLoanTransactionsResponse{" +
                "amount=" + amount +
                ", issuedTime='" + issuedTime + '\'' +
                ", loanId=" + loanId +
                ", name='" + name + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", revokedById=" + revokedById +
                ", id=" + id +
                ", creditAccount=" + creditAccount +
                ", debitAccount=" + debitAccount +
                '}';
    }
}
