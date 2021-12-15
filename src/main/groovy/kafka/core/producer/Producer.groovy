package kafka.core.producer

import kafka.exception.MessageSendingFailedException
import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord

class Producer {

    final KafkaProducer<String, String> producer

    Producer(String serverName) {
        Properties properties = ProducerProperties.init(serverName)
        producer = new KafkaProducer<>(properties)
    }

    void send(String topic, String message) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, message)
        Callback callback = (metadata, exception) -> {
            if (exception != null) {
                throw new MessageSendingFailedException(message, topic, exception)
            }
        }
        producer.send(producerRecord, callback)
    }

    void close() {
        producer.close()
    }
}
