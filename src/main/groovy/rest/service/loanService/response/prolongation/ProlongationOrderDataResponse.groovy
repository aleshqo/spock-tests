package rest.service.loanService.response.prolongation

import com.fasterxml.jackson.annotation.JsonProperty

import static java.util.Objects.isNull

class ProlongationOrderDataResponse {

    @JsonProperty("expiringDate")
    String expiringDate

    @JsonProperty("prolongationId")
    String prolongationId

    @JsonProperty("startDate")
    String startDate

    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("ProlongationOrderDataResponse{")
        sb.append("expiringDate='").append(expiringDate).append('\'')
        sb.append(", prolongationId='").append(prolongationId).append('\'')
        sb.append(", startDate='").append(startDate).append('\'')
        sb.append('}')
        return sb.toString()
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        ProlongationOrderDataResponse that = (ProlongationOrderDataResponse) o

        if (expiringDate != that.expiringDate) return false
        if (prolongationId != that.prolongationId) {
            return (isNull(prolongationId) || prolongationId.isBlank() || prolongationId.isEmpty()) &&
                    (isNull(that.prolongationId) || that.prolongationId.isBlank() || that.prolongationId.isEmpty())
        }
        if (startDate != that.startDate) return false

        return true
    }

    int hashCode() {
        int result
        result = (expiringDate != null ? expiringDate.hashCode() : 0)
        result = 31 * result + (prolongationId != null ? prolongationId.hashCode() : 0)
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0)
        return result
    }
}
