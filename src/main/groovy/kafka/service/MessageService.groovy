package kafka.service

import kafka.core.Broker
import kafka.core.consumer.ConsumerProperties
import kafka.core.consumer.FetchingConsumer
import kafka.core.producer.Producer
import kafka.dto.response.MessageDto
import utils.StringUtils

class MessageService {

    private final Broker broker

    MessageService(Broker broker) {
        this.broker = broker
    }

    void publishMessage(String message, String topic) {
        Producer producer = new Producer(broker.getBootstrapServer())
        producer.send(topic, message)
        producer.close()
    }

    List<MessageDto<String>> listenTo(String topicToListen, Long timeout) {
        FetchingConsumer consumer = new FetchingConsumer(broker.getBootstrapServer(), ConsumerProperties.testGroupId)
        return consumer.fetchMessages(topicToListen, timeout)
    }

    List<MessageDto<String>> listenToAsBillingConsumer(String topicToListen, Long timeout) {
        String prefix = topicToListen.split(StringUtils.HYPHEN).first()
        String groupId = prefix.concat(StringUtils.HYPHEN)
                .concat(ConsumerProperties.billingGroupId)
        FetchingConsumer consumer = new FetchingConsumer(broker.getBootstrapServer(), groupId)
        return consumer.fetchMessages(topicToListen, timeout)
    }
}
