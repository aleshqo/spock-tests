package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class GetCustomerScoringInfoResponse {

    @JsonProperty("loanId")
    BigInteger loanId

    @JsonProperty("loanDate")
    String loanDate

    @JsonProperty("status")
    String status

    @JsonProperty("dueDate")
    String dueDate

    @JsonProperty("lastOverdueDays")
    Integer lastOverdueDays


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("GetCustomerScoringInfoResponse{")
        sb.append("loanId=").append(loanId)
        sb.append(", loanDate='").append(loanDate).append('\'')
        sb.append(", status='").append(status).append('\'')
        sb.append(", dueDate='").append(dueDate).append('\'')
        sb.append(", lastOverdueDays=").append(lastOverdueDays)
        sb.append('}')
        return sb.toString()
    }
}
