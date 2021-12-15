package kafka.dto.converter

import kafka.dto.response.MessageDto
import org.apache.kafka.clients.consumer.ConsumerRecord

class MessageDtoConverter<K, V> {

    MessageDto<V> fromRecord(ConsumerRecord<K, V> record) {
        MessageDto<V> dto = new MessageDto<>()
        dto.setMessagePayload(record.value())
        dto.setOffset(record.offset())
        dto.setPartition(record.partition())
        return dto
    }
}
