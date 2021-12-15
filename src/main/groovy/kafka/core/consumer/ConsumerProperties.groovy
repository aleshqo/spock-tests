package kafka.core.consumer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import utils.StringUtils

class ConsumerProperties {

    static String testGroupId = "test-consumer-group"
    static String billingGroupId = "billing-consumer-group"

    static Properties init(String bootstrapServer, String consumerGroupId) {
        String deserializer = StringDeserializer.class.getName()

        Properties properties = new Properties()
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId)
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, deserializer)
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer)
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
        properties.setProperty(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "false")
        return properties
    }
}
