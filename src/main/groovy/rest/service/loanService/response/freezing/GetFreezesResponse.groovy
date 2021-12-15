package rest.service.loanService.response.freezing

import com.fasterxml.jackson.annotation.JsonProperty

class GetFreezesResponse {

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

    @JsonProperty("state")
    String state

    @JsonProperty("forgivenAmount")
    BigDecimal forgivenAmount

    @JsonProperty("forgiveness")
    Forgiveness forgiveness

    @JsonProperty("requiredPayment")
    BigDecimal requiredPayment


    @Override
    String toString() {
        return "GetFreezesResponse{" +
                "id=" + id +
                ", loanId=" + loanId +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", type='" + type + '\'' +
                ", state='" + state + '\'' +
                ", forgivenAmount=" + forgivenAmount +
                ", forgiveness=" + forgiveness +
                ", requiredPayment=" + requiredPayment +
                '}'
    }
}
