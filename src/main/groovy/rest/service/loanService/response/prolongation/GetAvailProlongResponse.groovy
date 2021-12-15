package rest.service.loanService.response.prolongation

import com.fasterxml.jackson.annotation.JsonProperty

class GetAvailProlongResponse {

    @JsonProperty("fromDate")
    String fromDate

    @JsonProperty("toDate")
    String toDate

    @JsonProperty("term")
    Integer term

    @JsonProperty("price")
    BigDecimal price

    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("GetAvailProlongResponse{")
        sb.append("fromDate='").append(fromDate).append('\'')
        sb.append(", toDate='").append(toDate).append('\'')
        sb.append(", term=").append(term)
        sb.append(", price='").append(price).append('\'')
        sb.append('}')
        return sb.toString()
    }
}
