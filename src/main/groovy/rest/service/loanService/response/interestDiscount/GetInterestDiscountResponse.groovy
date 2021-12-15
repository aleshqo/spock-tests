package rest.service.loanService.response.interestDiscount

import com.fasterxml.jackson.annotation.JsonProperty

class GetInterestDiscountResponse {


    @JsonProperty("active")
    Boolean active

    @JsonProperty("interestDiscountId")
    BigInteger interestDiscountId

    @JsonProperty("type")
    String type

    @JsonProperty("value")
    BigDecimal value

    @JsonProperty("loanId")
    BigInteger loanId

    @JsonProperty("customerId")
    BigInteger customerId

    @JsonProperty("fromDate")
    String fromDate

    @JsonProperty("toDate")
    String toDate

    @JsonProperty("revoked")
    Boolean revoked


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("GetInterestDiscountResponse{")
        sb.append("active=").append(active)
        sb.append(", interestDiscountId=").append(interestDiscountId)
        sb.append(", type='").append(type).append('\'')
        sb.append(", value=").append(value)
        sb.append(", loanId=").append(loanId)
        sb.append(", customerId=").append(customerId)
        sb.append(", fromDate='").append(fromDate).append('\'')
        sb.append(", toDate='").append(toDate).append('\'')
        sb.append(", revoked=").append(revoked)
        sb.append('}')
        return sb.toString()
    }
}
