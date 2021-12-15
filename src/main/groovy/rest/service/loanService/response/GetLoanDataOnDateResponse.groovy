package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class GetLoanDataOnDateResponse {

    @JsonProperty("loanId")
    BigInteger loanId

    @JsonProperty("dateTime")
    String dateTime

    @JsonProperty("dueDate")
    String  dueDate

    @JsonProperty("debts")
    Debts debts

    @JsonProperty("charges")
    Debts charges

    @JsonProperty("categorizations")
    Debts categorizations

    @JsonProperty("discounts")
    Debts discounts

    @JsonProperty("reserveDevaluations")
    Debts reserveDevaluations

    @JsonProperty("cancelledCommissionCharges")
    Map<String, BigDecimal> cancelledCommissionCharges


    @Override
    String toString() {
        return "GetLoanDataOnDateResponse{" +
                "loanId=" + loanId +
                ", dateTime='" + dateTime + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", debts=" + debts +
                ", charges=" + charges +
                ", categorizations=" + categorizations +
                ", discounts=" + discounts +
                ", reserveDevaluations=" + reserveDevaluations +
                ", cancelledCommissionCharges=" + cancelledCommissionCharges +
                '}'
    }
}
