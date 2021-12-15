package rest.service.loanService.response.freezing

import com.fasterxml.jackson.annotation.JsonProperty
import rest.service.loanService.response.Debts

class Forgiveness {

    @JsonProperty("total")
    BigDecimal total

    @JsonProperty("detailed")
    Debts detailed


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("Forgiveness{")
        sb.append("total=").append(total)
        sb.append(", detailed=").append(detailed)
        sb.append('}')
        return sb.toString()
    }
}
