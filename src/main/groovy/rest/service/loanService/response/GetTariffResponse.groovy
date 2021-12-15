package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped

class GetTariffResponse {

    @JsonProperty("id")
    BigInteger id

    @JsonProperty("loanTerm")
    BigInteger loanTerm

    @JsonProperty("amount")
    BigDecimal amount

    @JsonProperty("interest")
    BigDecimal interest

    @JsonProperty("yearlyInterest")
    BigInteger yearlyInterest

    @JsonProperty("overdueInterest")
    BigDecimal overdueInterest

    @JsonProperty("fine")
    BigDecimal fine

    @JsonProperty("paymentAmount")
    BigDecimal paymentAmount

    @JsonUnwrapped
    Map options


    @Override
    String toString() {
        return "GetTariffResponse{" +
                "id=" + id +
                ", loanTerm=" + loanTerm +
                ", amount=" + amount +
                ", interest=" + interest +
                ", yearlyInterest=" + yearlyInterest +
                ", overdueInterest=" + overdueInterest +
                ", fine=" + fine +
                ", paymentAmount=" + paymentAmount +
                ", options=" + options +
                '}'
    }
}
