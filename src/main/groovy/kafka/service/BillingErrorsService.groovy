package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.dto.response.MessageDto
import kafka.topics.TopicPrefixName
import utils.StringUtils

import static kafka.topics.TopicName.BILLING_ERRORS

@Slf4j
class BillingErrorsService {

    static List getBillingErrors(TopicPrefixName topicPrefixName, Long timeout = 500) {
        String billingErrorTopic = topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(BILLING_ERRORS.toCamelCase())
        List<MessageDto<String>> messageList = ConnectionController.KAFKA_CLIENT.getMessages(billingErrorTopic, timeout)
        log.info("getBillingErrors: topic=[{}], message=[{}]", billingErrorTopic, messageList)
        messageList
    }
}
