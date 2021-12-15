package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.dto.response.MessageDto
import kafka.topics.TopicPrefixName

import static kafka.KafkaUtils.buildTopicName
import static kafka.topics.TopicName.CUSTOMER_CANCELLED_REFINANCE_INTENT
import static utils.JsonParserHelper.convertValueToJson

@Slf4j
class CustomerCancelledRefinanceIntentService {

    static void sendCustomerCancelledRefinanceIntent(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = convertValueToJson(messageBody)
        String topicName = buildTopicName(topicPrefixName, CUSTOMER_CANCELLED_REFINANCE_INTENT)
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("sendCustomerCancelledRefinanceIntent: topic=[{}], message=[{}]", topicName, message)
    }

    static List<MessageDto<String>> readFromCustomerCancelledRefinanceIntentAsBillingConsumer(TopicPrefixName topicPrefixName, Long timeout = 500) {
        String topicName = buildTopicName(topicPrefixName, CUSTOMER_CANCELLED_REFINANCE_INTENT)
        List<MessageDto<String>> messageList = ConnectionController.KAFKA_CLIENT.getMessagesASBillingConsumer(topicName, timeout)
        log.info("readFromCustomerCancelledRefinanceIntentAsBillingConsumer: topic=[{}], message=[{}]", topicName, messageList)
        messageList
    }

    static List<MessageDto<String>> readFromCustomerCancelledRefinanceIntent(TopicPrefixName topicPrefixName, Long timeout = 500) {
        String topicName = buildTopicName(topicPrefixName, CUSTOMER_CANCELLED_REFINANCE_INTENT)
        List<MessageDto<String>> messageList = ConnectionController.KAFKA_CLIENT.getMessages(topicName, timeout)
        log.info("readFromCustomerCancelledRefinanceIntentAsBillingConsumer: topic=[{}], message=[{}]", topicName, messageList)
        messageList
    }

}
