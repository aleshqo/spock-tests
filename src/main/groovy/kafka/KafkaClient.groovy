package kafka

import groovy.util.logging.Slf4j
import kafka.controller.MessageController
import kafka.core.Broker
import kafka.dto.request.BrokerDto
import kafka.dto.request.PublishMessageDto
import kafka.dto.response.MessageDto
import kafka.service.MessageService

@Slf4j
class KafkaClient {

    final Broker broker
    final MessageController messageController

    KafkaClient(String host, String port) {
        BrokerDto brokerDto = new BrokerDto(host, port)
        broker = new Broker(brokerDto)
        messageController = new MessageController(new MessageService(broker))
    }

    void sendMessage(String topicName, String message) {
        PublishMessageDto publishMessageDto = new PublishMessageDto(topicName, message)
        messageController.publishMessage(publishMessageDto)
    }

    List<MessageDto<String>> getMessages(String topicName, Long timeout) {
        messageController.getMessageList(topicName, timeout)
    }

    List<MessageDto<String>> getMessagesASBillingConsumer(String topicName, Long timeout) {
        messageController.getMessageListAsBillingConsumer(topicName, timeout)
    }
}
