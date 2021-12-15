package kafka.service


import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.STOP_LOAN
import static kafka.topics.TopicName.STOP_LOANS_BY_BANKRUPT_CUSTOMER
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class StopLoanService {

    static void sendStopLoanByBankrupt(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, STOP_LOAN)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendStopLoanByBankrupt: topic=[{}], message=[{}]", topicName, message)
    }

    static void sendStopLoansByCustomer(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, STOP_LOANS_BY_BANKRUPT_CUSTOMER)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendStopLoansByCustomer: topic=[{}], message=[{}]", topicName, message)
    }

}
