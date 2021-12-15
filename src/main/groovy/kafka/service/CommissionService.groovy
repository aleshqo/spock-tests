package kafka.service


import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.CANCEL_COMMISSION
import static kafka.topics.TopicName.CUSTOMER_REQUESTED_COMMISSION
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class CommissionService {

    static def sendAddCommission(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, CUSTOMER_REQUESTED_COMMISSION)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("addCommission: topic=[{}], message=[{}]", topicName, message)
    }

    static def cancelCommission(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, CANCEL_COMMISSION)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("cancelCommission: topic=[{}], message=[{}]", topicName, message)
    }
}
