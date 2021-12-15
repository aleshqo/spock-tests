package kafka.service


import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.CUSTOMER_REQUESTED_FREEZING
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class FreezingService {

    static void sendFreezingLoan(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, CUSTOMER_REQUESTED_FREEZING)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendFreezingLoan: topic=[{}], message=[{}]", topicName, message)
    }
}
