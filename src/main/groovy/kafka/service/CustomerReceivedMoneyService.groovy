package kafka.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName
import utils.StringUtils

import static kafka.topics.TopicName.CUSTOMER_RECEIVED_MONEY

@Slf4j
class CustomerReceivedMoneyService {

    static void sendCustomerReceivedMoneyMsg(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = new ObjectMapper().writeValueAsString(messageBody)
        String topicName = topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(CUSTOMER_RECEIVED_MONEY.toCamelCase())
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendCustomerReceivedMoneyMsg: topic=[{}], message=[{}]", topicName, message)
    }
}
