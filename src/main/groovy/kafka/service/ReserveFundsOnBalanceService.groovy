package kafka.service


import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.RESERVE_FUNDS_ON_BALANCE
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class ReserveFundsOnBalanceService {

    static void reserveFundsOnBalance(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, RESERVE_FUNDS_ON_BALANCE)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("reserveFundsOnBalance: topic=[{}], message=[{}]", topicName, message)
    }
}
