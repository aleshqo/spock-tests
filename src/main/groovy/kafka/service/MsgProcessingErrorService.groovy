package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.dto.response.MessagePayload
import kafka.dto.response.MsgProcessingErrorDto
import kafka.topics.TopicPrefixName
import utils.StringUtils

import static java.util.Objects.isNull
import static kafka.dto.response.MessagePayload.readMessagePayload
import static kafka.topics.TopicName.MSG_PROCESSING_ERROR

@Slf4j
class MsgProcessingErrorService {

    static final Long DEFAULT_TIMEOUT = 3000L

    static MsgProcessingErrorDto getMsgProcessingError(TopicPrefixName topicPrefixName, Long timeout = DEFAULT_TIMEOUT) {
        String topic = topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(MSG_PROCESSING_ERROR.toCamelCase())

        MessagePayload messagePayload = readMessagePayload(ConnectionController.KAFKA_CLIENT, topic, timeout)
        if (isNull(messagePayload)) {
            return null
        }

        assert messagePayload.payload instanceof MsgProcessingErrorDto

        MsgProcessingErrorDto errorMessage = messagePayload.payload as MsgProcessingErrorDto
        log.info("getMsgProcessingError=[{}]", errorMessage)
        errorMessage
    }
}
