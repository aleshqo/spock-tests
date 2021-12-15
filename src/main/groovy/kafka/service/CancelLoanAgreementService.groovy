package kafka.service

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.topics.TopicPrefixName
import utils.StringUtils

import static kafka.topics.TopicName.CANCEL_LOAN_AGREEMENT

@Slf4j
class CancelLoanAgreementService {

    static def cancelLoanAgreement(Map messageBody, TopicPrefixName topicPrefixName) {
        String message = new ObjectMapper().writeValueAsString(messageBody)
        String topicName = topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(CANCEL_LOAN_AGREEMENT.toCamelCase())
        ConnectionController.KAFKA_CLIENT.sendMessage(topicName, message)
        log.info("cancelLoanAgreement: topic=[{}], message=[{}]", topicName, message)
    }
}
