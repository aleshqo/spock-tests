package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped

class GetPaymentsResponse {

    @JsonProperty("paymentId")
    Integer paymentId

    @JsonProperty("externalId")
    Integer externalId

    @JsonProperty("loanId")
    Integer loanId

    @JsonProperty("amount")
    Integer amount

    @JsonProperty("remainAmount")
    BigDecimal remainAmount

    @JsonProperty("reservedAmount")
    BigDecimal reservedAmount

    @JsonUnwrapped
    @JsonProperty("categorization")
    List<Categorization> categorization

    @JsonProperty("operationType")
    String operationType

    @JsonProperty("paymentTime")
    String paymentTime


    @Override
    String toString() {
        return "GetPaymentsResponse{" +
                "amount=" + amount +
                ", categorization=" + categorization +
                ", externalId=" + externalId +
                ", loanId=" + loanId +
                ", operationType='" + operationType + '\'' +
                ", paymentId=" + paymentId +
                ", paymentTime='" + paymentTime + '\'' +
                ", remainAmount=" + remainAmount +
                ", reservedAmount=" + reservedAmount +
                '}'
    }
}
