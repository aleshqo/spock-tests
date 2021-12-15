package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped

class Payments {

    @JsonProperty("externalId")
    Long externalId
    @JsonProperty("loanId")
    Long loanId
    @JsonProperty("operationType")
    String operationType
    @JsonProperty("paymentTime")
    String paymentTime
    @JsonUnwrapped
    List<TransactionsResponse> transactions


    @Override
    String toString() {
        return "Payments{" +
                "externalId=" + externalId +
                ", loanId=" + loanId +
                ", operationType='" + operationType + '\'' +
                ", paymentTime='" + paymentTime + '\'' +
                ", transactionsList=" + transactions +
                '}'
    }
}
