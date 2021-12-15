package kafka.service


import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.LOAN_WRITTEN_OFF
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class WriteOffService {

    static void sendWriteOffLoan(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, LOAN_WRITTEN_OFF)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendWriteOffLoan: topic=[{}], message=[{}]", topicName, message)
    }
}
