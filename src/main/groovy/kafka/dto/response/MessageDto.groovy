package kafka.dto.response

class MessageDto<T> {


    T messagePayload

    Integer partition

    Long offset


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("MessageDto{")
        sb.append("payload=").append(messagePayload)
        sb.append(", partition=").append(partition)
        sb.append(", offset=").append(offset)
        sb.append('}')
        return sb.toString()
    }
}
