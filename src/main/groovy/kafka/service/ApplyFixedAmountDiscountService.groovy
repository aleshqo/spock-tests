package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.APPLY_FIXED_AMOUNT_DISCOUNT
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class ApplyFixedAmountDiscountService {

    static void applyFixedAmountDiscount(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, APPLY_FIXED_AMOUNT_DISCOUNT)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("applyFixedAmountDiscount: topic=[{}], message=[{}]", topicName, message)
    }
}
