package kafka.service

import groovy.util.logging.Slf4j
import kafka.ConnectionController
import kafka.dto.response.MessagePayload
import kafka.dto.response.TariffGridUpdatedDto
import kafka.topics.TopicPrefixName

import static java.util.Objects.isNull
import static kafka.KafkaUtils.buildTopicName
import static kafka.dto.response.MessagePayload.readMessagePayload
import static kafka.topics.TopicName.TARIFF_GRID_UPDATED

@Slf4j
class TariffGridUpdatedService {

    static TariffGridUpdatedDto readFromTariffGridUpdated(TopicPrefixName topicPrefixName, Long timeout = 500) {
        String topicName = buildTopicName(topicPrefixName, TARIFF_GRID_UPDATED)
        MessagePayload messagePayload = readMessagePayload(ConnectionController.KAFKA_CLIENT, topicName, timeout)
        if (isNull(messagePayload)) {
            return null
        }
        assert messagePayload.payload instanceof TariffGridUpdatedDto

        TariffGridUpdatedDto tariffGridUpdatedDto = messagePayload.payload as TariffGridUpdatedDto
        log.info("readFromTariffGridUpdated: topic=[{}], count of records=[{}]", topicName, tariffGridUpdatedDto.grid.size())
        tariffGridUpdatedDto
    }
}
