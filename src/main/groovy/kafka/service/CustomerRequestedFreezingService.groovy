package kafka.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName
import utils.StringUtils

import static kafka.topics.TopicName.CUSTOMER_REQUESTED_FREEZING

@Slf4j
class CustomerRequestedFreezingService {

    static void sendCustomerRequestedFreezing(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = new ObjectMapper().writeValueAsString(messageBody)
        String topicName = topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(CUSTOMER_REQUESTED_FREEZING.toCamelCase())
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendCustomerRequestedFreezing: topic=[{}], message=[{}]", topicName, message)
    }
}
