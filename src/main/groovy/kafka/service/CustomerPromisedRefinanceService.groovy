package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.CUSTOMER_PROMISED_REFINANCE
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class CustomerPromisedRefinanceService {

    static void sendCustomerPromisedRefinance(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, CUSTOMER_PROMISED_REFINANCE)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendCustomerPromisedRefinance: topic=[{}], message=[{}]", topicName, message)
    }
}
