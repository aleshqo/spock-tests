package rest.service.loanService.response.prolongation

import com.fasterxml.jackson.annotation.JsonProperty

class GetProlongationResponse {

    @JsonProperty("id")
    BigInteger id

    @JsonProperty("loanId")
    BigInteger loanId

    @JsonProperty("fromDate")
    String fromDate

    @JsonProperty("toDate")
    String toDate

    @JsonProperty("type")
    String type

    @JsonProperty("paymentId")
    BigInteger paymentId

    @JsonProperty("revoked")
    Boolean revoked

    @JsonProperty("paidInterestSum")
    BigInteger paidInterestSum

    @JsonProperty("dueDateShift")
    Integer dueDateShift

    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("GetProlongationResponse{")
        sb.append("id=").append(id)
        sb.append(", loanId=").append(loanId)
        sb.append(", fromDate='").append(fromDate).append('\'')
        sb.append(", toDate='").append(toDate).append('\'')
        sb.append(", type='").append(type).append('\'')
        sb.append(", paymentId=").append(paymentId)
        sb.append(", revoked=").append(revoked)
        sb.append('}')
        return sb.toString()
    }
}
