package kafka.dto.request


import javax.validation.constraints.NotBlank


class TopicDto {

    TopicDto(String name) {
        this.name = name
    }

    @NotBlank
    String name
}
