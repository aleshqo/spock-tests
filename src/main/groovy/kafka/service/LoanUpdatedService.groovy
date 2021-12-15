package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.dto.response.LoanUpdatedDto
import kafka.dto.response.MessagePayload
import kafka.topics.TopicPrefixName
import utils.StringUtils

import static java.util.Objects.nonNull
import static kafka.dto.response.MessagePayload.readMessagePayload
import static kafka.topics.TopicName.LOAN_UPDATED

@Slf4j
class LoanUpdatedService {


    static LoanUpdatedDto getLoanUpdated(TopicPrefixName topicPrefixName, Long timeout = 3000) {
        String topic = topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(LOAN_UPDATED.toCamelCase())

        LoanUpdatedDto loanUpdatedDto = null
        MessagePayload messagePayload = readMessagePayload(ConnectionController.KAFKA_CLIENT, topic, timeout)
        if (nonNull(messagePayload)) {
            assert messagePayload.payload instanceof LoanUpdatedDto
            loanUpdatedDto = messagePayload.payload as LoanUpdatedDto
        }
        log.info("getLoanUpdated message=[{}]", loanUpdatedDto)
        loanUpdatedDto
    }
}
