package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class Commissions {

    @JsonProperty("id")
    Integer id
    @JsonProperty("loanId")
    Integer loanId
    @JsonProperty("type")
    String type
    @JsonProperty("amount")
    BigDecimal amount
    @JsonProperty("fromDate")
    String fromDate
    @JsonProperty("toDate")
    String toDate
    @JsonProperty("state")
    String state
    @JsonProperty("externalId")
    Integer externalId


    @Override
     String toString() {
        return "Commissions{" +
                "id=" + id +
                ", loanId=" + loanId +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", state='" + state + '\'' +
                ", externalId=" + externalId +
                '}'
    }
}
