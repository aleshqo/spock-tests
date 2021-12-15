package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class GetLoanHistoryResponse {

    @JsonProperty("loanId")
    BigInteger loanId

    @JsonProperty("actionName")
    String actionName

    @JsonProperty("statusName")
    String statusName

    @JsonProperty("fromTime")
    String fromTime

    @JsonProperty("toTime")
    String toTime


    @Override
     String toString() {
        return "GetLoanHistoryResponse{" +
                "loanId=" + loanId +
                ", actionName='" + actionName + '\'' +
                ", statusName='" + statusName + '\'' +
                ", fromTime='" + fromTime + '\'' +
                ", toTime='" + toTime + '\'' +
                '}'
    }
}
