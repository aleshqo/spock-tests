package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.RESUME_LOAN_CALCULATION
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class ResumeLoanCalculationService {

    static void resumeLoanCalculation(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, RESUME_LOAN_CALCULATION)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("resumeLoanCalculation: topic=[{}], message=[{}]", topicName, message)
    }
}
