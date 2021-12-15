package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.dto.response.BillingReceivedPaymentDto
import kafka.dto.response.MessagePayload
import kafka.topics.TopicPrefixName

import static java.util.Objects.isNull
import static kafka.KafkaUtils.buildTopicName
import static kafka.dto.response.MessagePayload.readMessagePayload
import static kafka.topics.TopicName.BILLING_RECEIVED_PAYMENT

@Slf4j
class BillingReceivedPaymentService {

    static BillingReceivedPaymentDto getBillingReceivedPayment(TopicPrefixName topicPrefixName, Long timeout = 500) {
        String topicName = buildTopicName(topicPrefixName, BILLING_RECEIVED_PAYMENT)
        MessagePayload messagePayload = readMessagePayload(ConnectionController.KAFKA_CLIENT, topicName, timeout)
        if (isNull(messagePayload)) {
            return null
        }
        assert messagePayload.payload instanceof BillingReceivedPaymentDto

        BillingReceivedPaymentDto billingReceivedPaymentDto = messagePayload.payload as BillingReceivedPaymentDto
        log.info("getBillingReceivedPayment message=[{}]", BillingReceivedPaymentDto)
        billingReceivedPaymentDto
    }
}
