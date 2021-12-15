package kafka.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName
import utils.StringUtils

import static kafka.topics.TopicName.CUSTOMER_CANCELED_PAYMENT

@Slf4j
class CustomerCanceledPaymentService {

    static void sendCustomerCanceledPayment(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = new ObjectMapper().writeValueAsString(messageBody)
        String topicName = topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(CUSTOMER_CANCELED_PAYMENT.toCamelCase())
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendCustomerCanceledPayment: topic=[{}], message=[{}]", topicName, message)
    }
}
