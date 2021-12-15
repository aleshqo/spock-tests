package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class Discounts {

    @JsonProperty("amount")
    BigDecimal amount
    @JsonProperty("discountTime")
    String discountTime
    @JsonProperty("id")
    BigInteger id
    @JsonProperty("issuedTime")
    String issuedTime
    @JsonProperty("operationType")
    String operationType
    @JsonProperty("scheduleId")
    Integer scheduleId
    @JsonProperty("revokedBy")
    String revokedBy

    @Override
    String toString() {
        return "Discounts{" +
                "amount=" + amount +
                ", discountTime='" + discountTime + '\'' +
                ", id=" + id +
                ", issuedTime='" + issuedTime + '\'' +
                ", operationType='" + operationType + '\'' +
                ", scheduleId=" + scheduleId +
                ", revokedBy='" + revokedBy + '\'' +
                '}'
    }
}
