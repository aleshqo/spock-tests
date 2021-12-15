package rest.service.loanService.response.interestDiscount

import com.fasterxml.jackson.annotation.JsonProperty

class GetFullInterestDiscAmountResponse {

    @JsonProperty("amount")
    BigInteger amount


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("GetFullInterestDiscAmountResponse{")
        sb.append("amount=").append(amount)
        sb.append('}')
        return sb.toString()
    }
}
