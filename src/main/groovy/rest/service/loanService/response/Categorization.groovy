package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class Categorization {

    @JsonProperty("paymentDate")
    String paymentDate

    @JsonProperty("paymentCategorizationDate")
    String paymentCategorizationDate

    @JsonProperty("issuedTime")
    String issuedTime

    @JsonProperty("loanId")
    Long loanId

    @JsonProperty("debts")
    Debts debts


    @Override
    String toString() {
        return "Categorization{" +
                "debts=" + debts +
                ", issuedTime='" + issuedTime + '\'' +
                ", loanId=" + loanId +
                ", paymentCategorizationDate='" + paymentCategorizationDate + '\'' +
                ", paymentDate='" + paymentDate + '\'' +
                '}'
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Categorization)) return false

        Categorization that = (Categorization) o

        if (debts != that.debts) return false
        if (issuedTime != that.issuedTime) return false
        if (loanId != that.loanId) return false
        if (paymentCategorizationDate != that.paymentCategorizationDate) return false
        if (paymentDate != that.paymentDate) return false

        return true
    }

    int hashCode() {
        int result
        result = (paymentDate != null ? paymentDate.hashCode() : 0)
        result = 31 * result + (paymentCategorizationDate != null ? paymentCategorizationDate.hashCode() : 0)
        result = 31 * result + (issuedTime != null ? issuedTime.hashCode() : 0)
        result = 31 * result + (loanId != null ? loanId.hashCode() : 0)
        result = 31 * result + (debts != null ? debts.hashCode() : 0)
        return result
    }
}
