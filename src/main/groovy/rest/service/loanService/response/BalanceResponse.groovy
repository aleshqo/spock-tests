package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped

class BalanceResponse {

    @JsonProperty("customerId")
    Long customerId
    @JsonUnwrapped
    List<Payments> payments
    @JsonProperty("remainAmount")
    Long remainAmount
    @JsonProperty("reservedAmount")
    Long reservedAmount
    @JsonUnwrapped
    List<WithdrawalsFromBalance> withdrawalsFromBalance


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("BalanceResponse{")
        sb.append("customerId=").append(customerId)
        sb.append(", payments=").append(payments)
        sb.append(", remainAmount=").append(remainAmount)
        sb.append(", reservedAmount=").append(reservedAmount)
        sb.append(", withdrawalsFromBalance=").append(withdrawalsFromBalance)
        sb.append('}')
        return sb.toString()
    }
}
