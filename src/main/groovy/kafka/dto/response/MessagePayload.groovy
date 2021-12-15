package kafka.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import groovy.util.logging.Slf4j
import kafka.KafkaClient

import static utils.JsonParserHelper.getValueFromJson

@Slf4j
class MessagePayload {

    @JsonProperty("name")
    String name

    @JsonProperty("version")
    Integer version

    @JsonProperty("eventTime")
    String eventTime

    @JsonProperty("payload")
    @JsonTypeInfo(include = JsonTypeInfo.As.EXTERNAL_PROPERTY, use = JsonTypeInfo.Id.NAME, property = "name")
    @JsonSubTypes([
            @JsonSubTypes.Type(value = LoanUpdatedDto, name = "loanUpdated"),
            @JsonSubTypes.Type(value = MsgProcessingErrorDto, name = "msgProcessingError"),
            @JsonSubTypes.Type(value = BillingReceivedPaymentDto, name = "billingReceivedPayment"),
            @JsonSubTypes.Type(value = TariffGridUpdatedDto, name = "tariffGridUpdated")
    ])
    Payload payload


    static MessagePayload readMessagePayload(KafkaClient kafkaClient, String topic, Long timeout) {
        List<MessageDto<String>> messages = kafkaClient.getMessages(topic, timeout)
        messages.forEach({
            log.trace("read message: topic=[{}], message=[{}]", topic, it.getMessagePayload())
        })
        MessagePayload messagePayload = null
        if (!messages.isEmpty()) {
            messagePayload = getValueFromJson(messages.last().getMessagePayload(), MessagePayload) as MessagePayload
            assert messagePayload.name == topic.split("-").last()
        }
        log.trace("readMessagePayload=[{}]", messagePayload)
        messagePayload
    }


    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("MessagePayload{")
        sb.append("name='").append(name).append('\'')
        sb.append(", version=").append(version)
        sb.append(", eventTime='").append(eventTime).append('\'')
        sb.append(", payload=").append(payload)
        sb.append('}')
        return sb.toString()
    }
}
