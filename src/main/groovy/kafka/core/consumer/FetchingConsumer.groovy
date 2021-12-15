package kafka.core.consumer

import groovy.util.logging.Slf4j
import kafka.dto.converter.MessageDtoConverter
import kafka.dto.response.MessageDto
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration

@Slf4j
class FetchingConsumer {

    private final KafkaConsumer<String, String> consumer
    private final MessageDtoConverter<String, String> converter

    FetchingConsumer(String bootstrapServer, String consumerGroupId) {
        Properties properties = ConsumerProperties.init(bootstrapServer, consumerGroupId)
        consumer = new KafkaConsumer<>(properties)
        converter = new MessageDtoConverter<>()
    }

    List<MessageDto<String>> fetchMessages(String topic, Long timeout) {
        List<MessageDto<String>> result = new ArrayList<>()
        ConsumerRecords<String, String> records = fetchData(topic, timeout)
        records.forEach(
                record -> result.add(converter.fromRecord(record))
        )
        return result
    }

    private ConsumerRecords<String, String> fetchData(String topic, Long timeout) {
        consumer.subscribe(Collections.singletonList(topic))
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(timeout))
        consumer.close()
        return records
    }
}
