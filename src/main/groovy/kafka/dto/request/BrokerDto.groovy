package kafka.dto.request


import javax.validation.constraints.NotBlank


class BrokerDto {

    BrokerDto(String host, String port) {
        this.host = host
        this.port = port
    }

    @NotBlank
    String host

    @NotBlank
    String port
}
