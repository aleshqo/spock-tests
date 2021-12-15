package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.APPLY_COURT_DECISION
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class ApplyCourtDecisionService {

    static void applyCourtDecision(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, APPLY_COURT_DECISION)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("applyCourtDecision: topic=[{}], message=[{}]", topicName, message)
    }
}
