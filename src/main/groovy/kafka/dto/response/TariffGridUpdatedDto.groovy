package kafka.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

class TariffGridUpdatedDto implements Payload {

    @JsonProperty("grid")
    List<Grid> grid

    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("Payload{")
        sb.append("grid=").append(grid)
        sb.append('}')
        return sb.toString()
    }

    static class Grid {

        @JsonProperty("id")
        BigInteger id

        @JsonProperty("term")
        Integer term

        @JsonProperty("amount")
        BigDecimal amount

        @JsonProperty("paymentAmount")
        BigDecimal paymentAmount

        @JsonProperty("interest")
        BigDecimal interest

        @JsonProperty("overdueInterest")
        BigDecimal overdueInterest

        @JsonProperty("fine")
        BigDecimal fine

        @Override
        String toString() {
            final StringBuilder sb = new StringBuilder("Grid{")
            sb.append("id=").append(id)
            sb.append(", term=").append(term)
            sb.append(", amount=").append(amount)
            sb.append(", paymentAmount=").append(paymentAmount)
            sb.append(", interest=").append(interest)
            sb.append(", overdueInterest=").append(overdueInterest)
            sb.append(", fine=").append(fine)
            sb.append('}')
            return sb.toString()
        }
    }
}
