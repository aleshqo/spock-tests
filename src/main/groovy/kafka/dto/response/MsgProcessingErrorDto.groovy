package kafka.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

class MsgProcessingErrorDto implements Payload {

    @JsonProperty("originalMessage")
    Map originalMessage

    @JsonProperty("error")
    String error

    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("Payload{")
        sb.append("originalMessage=").append(originalMessage)
        sb.append(", error='").append(error).append('\'')
        sb.append('}')
        return sb.toString()
    }
}
