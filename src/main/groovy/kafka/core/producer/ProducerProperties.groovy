package kafka.core.producer

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer

class ProducerProperties {

    static Properties init(String server) {
        String serializer = StringSerializer.class.getName()
        Properties properties = new Properties()
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server)
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializer)
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, serializer)
        return properties
    }
}
