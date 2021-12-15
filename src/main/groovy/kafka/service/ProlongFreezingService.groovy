package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.PROLONG_FREEZING
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class ProlongFreezingService {

    static void prolongFreezing(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, PROLONG_FREEZING)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("prolongFreezing: topic=[{}], message=[{}]", topicName, message)
    }
}
