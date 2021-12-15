package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.PAY_LOAN_FROM_BALANCE
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class PayLoanFromBalanceService {

    static void payLoanFromBalance(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, PAY_LOAN_FROM_BALANCE)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("payLoanFromBalance: topic=[{}], message=[{}]", topicName, message)
    }
}
