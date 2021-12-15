package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.CLOSE_BAD_LOAN
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class CloseBadLoanService {

    static void closeBadLoan(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, CLOSE_BAD_LOAN)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("closeBadLoan: topic=[{}], message=[{}]", topicName, message)
    }
}
