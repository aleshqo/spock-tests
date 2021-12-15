package kafka.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

class Agreement {

    @JsonProperty("id")
    BigInteger id

    @JsonProperty("loanType")
    String loanType

    @JsonProperty("loanDate")
    String loanDate

    @JsonProperty("loanAmount")
    BigInteger loanAmount

    @JsonProperty("loanTerm")
    Integer loanTerm


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("Agreement{")
        sb.append("id=").append(id)
        sb.append(", loanType='").append(loanType).append('\'')
        sb.append(", loanDate='").append(loanDate).append('\'')
        sb.append(", loanAmount=").append(loanAmount)
        sb.append(", loanTerm=").append(loanTerm)
        sb.append('}')
        return sb.toString()
    }
}
