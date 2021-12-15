package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.LOAN_SOLD
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class LoanSoldService {

    static void sellLoan(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, LOAN_SOLD)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sellLoan: topic=[{}], message=[{}]", topicName, message)
    }
}
