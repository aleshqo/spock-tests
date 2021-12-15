package rest.service.loanService.response.interestDiscount

import com.fasterxml.jackson.annotation.JsonProperty

class GetInterestDiscHistResponse {


    @JsonProperty("date")
    String date

    @JsonProperty("interestDiscount")
    BigDecimal interestDiscount

    @JsonProperty("newInterestRate")
    BigDecimal newInterestRate

    @JsonProperty("type")
    String type

    @JsonProperty("revoked")
    Boolean revoked

    @JsonProperty("reason")
    String reason


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("GetInterestDiscHistResponse{")
        sb.append("date='").append(date).append('\'')
        sb.append(", interestDiscount=").append(interestDiscount)
        sb.append(", newInterestRate=").append(newInterestRate)
        sb.append(", type='").append(type).append('\'')
        sb.append(", revoked=").append(revoked)
        sb.append(", reason='").append(reason).append('\'')
        sb.append('}')
        return sb.toString()
    }
}
