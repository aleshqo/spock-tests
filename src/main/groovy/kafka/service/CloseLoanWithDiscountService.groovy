package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.CLOSE_LOAN_WITH_DISCOUNT
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class CloseLoanWithDiscountService {

    static void closeLoanWithDiscount(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, CLOSE_LOAN_WITH_DISCOUNT)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("closeLoanWithDiscount: topic=[{}], message=[{}]", topicName, message)
    }
}
