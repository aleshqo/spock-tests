package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Getter

@Getter
class TransactionsResponse {

    @JsonProperty("id")
    Long id

    @JsonProperty("name")
    String name

    @JsonProperty("amount")
    Long amount

    @JsonProperty("issuedTime")
    String issuedTime

    @JsonProperty("loanId")
    Long loanId

    @JsonProperty("revokedById")
    String revokedById

    @JsonProperty("type")
    String type

    @JsonProperty("creditAccount")
    Long creditAccount

    @JsonProperty("debitAccount")
    Long debitAccount

    @JsonProperty("transactionDate")
    String transactionDate


    @Override
    String toString() {
        return "Transactions{" +
                "id=" + id +
                ", name=" + name +
                ", amount=" + amount +
                ", issuedTime='" + issuedTime + '\'' +
                ", loanId=" + loanId +
                ", revokedById='" + revokedById + '\'' +
                ", type='" + type + '\'' +
                ", creditAccount=" + creditAccount +
                ", debitAccount=" + debitAccount +
                ", transactionDate='" + transactionDate + '\'' +
                '}';
    }
}
