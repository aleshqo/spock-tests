package kafka.dto.request


import javax.validation.constraints.NotBlank


class PublishMessageDto {

    PublishMessageDto(String topic, String message) {
        this.topic = topic
        this.message = message
    }

    @NotBlank
    String topic

    @NotBlank
    String message
}
