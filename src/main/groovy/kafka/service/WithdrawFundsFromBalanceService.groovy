package kafka.service


import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.WITHDRAW_FUNDS_FROM_BALANCE
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class WithdrawFundsFromBalanceService {

    static void withdrawFundsFromBalance(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, WITHDRAW_FUNDS_FROM_BALANCE)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("reserveFundsOnBalance: topic=[{}], message=[{}]", topicName, message)
    }
}
