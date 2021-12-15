package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

import static java.util.Objects.isNull

class AddFreezingResponse {

    @JsonProperty("result")
    Boolean result

    @JsonProperty("id")
    Number id

    @JsonProperty("discounts")
    Discount discounts

    @Override
    String toString() {
        return "AddFreezingResponse{" +
                "result=" + result +
                ", id=" + id +
                ", discounts=" + discounts +
                '}'
    }

    static class Discount {

        @JsonProperty("overdueInterest")
        BigDecimal overdueInterest

        @JsonProperty("interest")
        BigDecimal interest

        @JsonProperty("fine")
        BigDecimal fine

        boolean discountIsNull() {
            return isNull(this.overdueInterest) && isNull(this.interest) && isNull(this.fine)
        }

        @Override
        String toString() {
            return "Discount{" +
                    "overdueInterest=" + overdueInterest +
                    ", interest=" + interest +
                    ", fine=" + fine +
                    '}'
        }
    }
}
