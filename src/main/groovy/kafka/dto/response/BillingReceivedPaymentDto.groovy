package kafka.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

class BillingReceivedPaymentDto implements Payload {

    @JsonProperty("customerId")
    BigInteger customerId

    @JsonProperty("loanId")
    BigInteger loanId

    @JsonProperty("amount")
    BigDecimal amount

    @JsonProperty("operationType")
    String operationType

    @JsonProperty("paymentTime")
    String paymentTime

    @JsonProperty("paymentId")
    BigInteger paymentId


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("BillingReceivedPaymentDto{")
        sb.append("customerId=").append(customerId)
        sb.append(", loanId=").append(loanId)
        sb.append(", amount=").append(amount)
        sb.append(", operationType='").append(operationType).append('\'')
        sb.append(", paymentTime='").append(paymentTime).append('\'')
        sb.append(", paymentId=").append(paymentId)
        sb.append('}')
        return sb.toString()
    }
}
