package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class Freezing {

    @JsonProperty("id")
    BigInteger id

    @JsonProperty("fromDate")
    String fromDate

    @JsonProperty("toDate")
    String toDate


    @Override
    String toString() {
        return "Freezing{" +
                "id=" + id +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                '}'
    }
}
