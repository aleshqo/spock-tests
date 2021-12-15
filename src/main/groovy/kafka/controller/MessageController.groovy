package kafka.controller

import kafka.dto.request.PublishMessageDto
import kafka.dto.response.MessageDto
import kafka.service.MessageService

import javax.validation.Valid

class MessageController {

    private final MessageService messageService

    MessageController(MessageService messageService) {
        this.messageService = messageService
    }

    void publishMessage(@Valid PublishMessageDto dto) {
        messageService.publishMessage(dto.getMessage(), dto.getTopic())
    }

    List<MessageDto<String>> getMessageList(String topic, Long timeout) {
        return messageService.listenTo(topic, timeout)
    }

    List<MessageDto<String>> getMessageListAsBillingConsumer(String topic, Long timeout) {
        return messageService.listenToAsBillingConsumer(topic, timeout)
    }
}
