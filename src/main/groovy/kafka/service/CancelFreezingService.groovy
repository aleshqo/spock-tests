package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.CANCEL_FREEZING
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class CancelFreezingService {

    static void cancelFreezing(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, CANCEL_FREEZING)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("cancelFreezing: topic=[{}], message=[{}]", topicName, message)
    }
}
