package kafka.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import rest.service.loanService.response.Categorization
import rest.service.loanService.response.Commissions
import rest.service.loanService.response.Debts
import rest.service.loanService.response.Freezing
import rest.service.loanService.response.prolongation.ProlongationOrderDataResponse

class LoanUpdatedDto implements Payload {

    @JsonProperty("term")
    Integer term

    @JsonProperty("debts")
    Debts debts

    @JsonProperty("amount")
    BigDecimal amount

    @JsonProperty("loanId")
    BigInteger loanId

    @JsonProperty("freezing")
    Freezing freezing

    @JsonProperty("interest")
    BigDecimal interest

    @JsonProperty("loanDate")
    String loanDate

    @JsonProperty("payments")
    @JsonUnwrapped
    List<Payments> payments

    @JsonProperty("typeName")
    String typeName

    @JsonProperty("customerId")
    Integer customerId

    @JsonProperty("commissions")
    @JsonUnwrapped
    List<Commissions> commissions

    @JsonProperty("currentStatus")
    String currentStatus

    @JsonProperty("prolongations")
    @JsonUnwrapped
    List<Prolongations> prolongations

    @JsonProperty("repaymentDate")
    String repaymentDate

    @JsonProperty("overdueInterest")
    BigDecimal overdueInterest

    @JsonProperty("interestDiscount")
    BigDecimal interestDiscount

    @JsonProperty("currentStatusDate")
    String currentStatusDate

    @JsonProperty("prolongationOrder")
    ProlongationOrderDataResponse prolongationOrder

    @JsonProperty("initialRepaymentAmount")
    BigDecimal initialRepaymentAmount

    @JsonProperty("prolongationAvailability")
    List prolongationAvailability

    @JsonProperty("daysPastDue")
    Integer daysPastDue

    @JsonProperty("offline")
    Boolean offline

    @Override
    String toString() {
        return "NotificationData{" +
                "term=" + term +
                ", debts=" + debts +
                ", amount=" + amount +
                ", loanId=" + loanId +
                ", freezing='" + freezing + '\'' +
                ", interest=" + interest +
                ", loanDate='" + loanDate + '\'' +
                ", payments=" + payments +
                ", typeName='" + typeName + '\'' +
                ", customerId=" + customerId +
                ", commissions=" + commissions +
                ", currentStatus='" + currentStatus + '\'' +
                ", prolongations=" + prolongations +
                ", repaymentDate='" + repaymentDate + '\'' +
                ", overdueInterest=" + overdueInterest +
                ", interestDiscount=" + interestDiscount +
                ", currentStatusDate='" + currentStatusDate + '\'' +
                ", prolongationOrder='" + prolongationOrder + '\'' +
                ", initialRepaymentAmount=" + initialRepaymentAmount +
                ", prolongationAvailability=" + prolongationAvailability +
                ", daysPastDue=" + daysPastDue +
                ", offline=" + offline +
                '}'
    }


    static class Payments {
        @JsonProperty("paymentId")
        Integer paymentId

        @JsonProperty("externalId")
        Integer externalId

        @JsonProperty("amount")
        Integer amount

        @JsonProperty("remainAmount")
        BigDecimal remainAmount

        @JsonProperty("reservedAmount")
        BigDecimal reservedAmount

        @JsonProperty("categorization")
        Categorization categorization

        @JsonProperty("operationType")
        String operationType

        @JsonProperty("paymentTime")
        String paymentTime


        @Override
        String toString() {
            final StringBuilder sb = new StringBuilder("Payments{")
            sb.append("paymentId=").append(paymentId)
            sb.append(", externalId=").append(externalId)
            sb.append(", amount=").append(amount)
            sb.append(", remainAmount=").append(remainAmount)
            sb.append(", reservedAmount=").append(reservedAmount)
            sb.append(", categorization=").append(categorization)
            sb.append(", operationType='").append(operationType).append('\'')
            sb.append(", paymentTime='").append(paymentTime).append('\'')
            sb.append('}')
            return sb.toString()
        }
    }

    private static class Prolongations {
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

        @JsonProperty("term")
        BigInteger term

        @JsonProperty("externalId")
        BigInteger externalId

        @JsonProperty("revoked")
        Boolean revoked


        @Override
        String toString() {
            final StringBuilder sb = new StringBuilder("Prolongations{")
            sb.append("id=").append(id)
            sb.append(", loanId=").append(loanId)
            sb.append(", fromDate='").append(fromDate).append('\'')
            sb.append(", toDate='").append(toDate).append('\'')
            sb.append(", type='").append(type).append('\'')
            sb.append(", term=").append(term)
            sb.append(", externalId=").append(externalId)
            sb.append(", revoked=").append(revoked)
            sb.append('}')
            return sb.toString()
        }
    }
}

