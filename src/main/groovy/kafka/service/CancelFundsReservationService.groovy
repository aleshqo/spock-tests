package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.CANCEL_FUNDS_RESERVATION
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class CancelFundsReservationService {

    static void cancelFundsReservation(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, CANCEL_FUNDS_RESERVATION)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("cancelFundsReservation: topic=[{}], message=[{}]", topicName, message)
    }
}
