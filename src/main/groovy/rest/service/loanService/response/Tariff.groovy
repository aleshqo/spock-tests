package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class Tariff {

    @JsonProperty("id")
    Integer id
    @JsonProperty("loanTerm")
    Integer loanTerm
    @JsonProperty("amount")
    Double amount
    @JsonProperty("paymentAmount")
    Double paymentAmount
    @JsonProperty("interest")
    Double interest
    @JsonProperty("fromDate")
    String fromDate
    @JsonProperty("toDate")
    String toDate
    @JsonProperty("yearlyInterestRate")
    Integer yearlyInterestRate
    @JsonProperty("dailyInterestRate")
    Double dailyInterestRate


    @Override
    String toString() {
        return "Tariff{" +
                "id=" + id +
                ", loanTerm=" + loanTerm +
                ", amount=" + amount +
                ", paymentAmount=" + paymentAmount +
                ", interest=" + interest +
                ", fromDate='" + fromDate + '\'' +
                ", toDate='" + toDate + '\'' +
                ", yearlyInterestRate=" + yearlyInterestRate +
                ", dailyInterestRate=" + dailyInterestRate +
                '}'
    }
}
