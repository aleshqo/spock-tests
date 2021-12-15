package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty


class GetLoanResponse {

    @JsonProperty("id")
    Integer id

    @JsonProperty("loanTypeName")
    String loanTypeName

    @JsonProperty("customerId")
    Integer customerId

    @JsonProperty("loanDate")
    String loanDate

    @JsonProperty("firstPaymentDate")
    String firstPaymentDate

    @JsonProperty("amount")
    Integer amount

    @JsonProperty("loanTerm")
    Integer loanTerm

    @JsonProperty("currentStatus")
    String currentStatus

    @JsonProperty("dueDate")
    String dueDate

    @JsonProperty("daysPastDue")
    Integer daysPastDue

    @JsonProperty("offline")
    Boolean offline

    @JsonProperty("paymentAmount")
    Integer paymentAmount

    @JsonProperty("tariff")
    Tariff tariff


    @Override
    String toString() {
        return "GetLoanResponse{" +
                "id=" + id +
                ", loanTypeName='" + loanTypeName + '\'' +
                ", customerId=" + customerId +
                ", loanDate='" + loanDate + '\'' +
                ", firstPaymentDate='" + firstPaymentDate + '\'' +
                ", amount=" + amount +
                ", loanTerm=" + loanTerm +
                ", currentStatus='" + currentStatus + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", daysPastDue=" + daysPastDue +
                ", offline=" + offline +
                ", paymentAmount=" + paymentAmount +
                ", tariff=" + tariff +
                '}'
    }
}
