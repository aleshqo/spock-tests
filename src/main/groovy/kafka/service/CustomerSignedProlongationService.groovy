package kafka.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName
import utils.StringUtils

import static kafka.topics.TopicName.CUSTOMER_SIGNED_PROLONGATION

@Slf4j
class CustomerSignedProlongationService {

    static void sendCustomerSignedProlongation(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = new ObjectMapper().writeValueAsString(messageBody)
        String topicName = topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(CUSTOMER_SIGNED_PROLONGATION.toCamelCase())
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendCustomerSignedProlongation: topic=[{}], message=[{}]", topicName, message)
    }
}
