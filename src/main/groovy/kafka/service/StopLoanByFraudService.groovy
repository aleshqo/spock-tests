package kafka.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName
import utils.StringUtils

import static kafka.topics.TopicName.STOP_LOANS_BY_FRAUD_CUSTOMER

@Slf4j
class StopLoanByFraudService {

    static void sendStopCustomerByFraud(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = new ObjectMapper().writeValueAsString(messageBody)
        String topicName = topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(STOP_LOANS_BY_FRAUD_CUSTOMER.toCamelCase())
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendStopLoanByFraud: topic=[{}], message=[{}]", topicName, message)
    }
}
