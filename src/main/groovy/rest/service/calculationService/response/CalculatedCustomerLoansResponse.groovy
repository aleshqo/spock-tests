package rest.service.calculationService.response

import com.fasterxml.jackson.annotation.JsonProperty

class CalculatedCustomerLoansResponse {

    @JsonProperty("failedLoansCount")
    Integer failedLoansCount

    @JsonProperty("processedCount")
    Integer processedCount


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("CalculatedCustomerLoansResponse{")
        sb.append("failedLoansCount=").append(failedLoansCount)
        sb.append(", processedCount=").append(processedCount)
        sb.append('}')
        return sb.toString()
    }
}
