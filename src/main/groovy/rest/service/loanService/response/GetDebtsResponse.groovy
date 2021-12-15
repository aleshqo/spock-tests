package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class GetDebtsResponse {

    @JsonProperty("debts")
    Debts debts

    @JsonProperty("paymentScheduleDate")
    String paymentScheduleDate


    @Override
    String toString() {
        return "GetDebtsResponse{" +
                "debts=" + debts +
                ", paymentScheduleDate='" + paymentScheduleDate + '\'' +
                '}'
    }
}
