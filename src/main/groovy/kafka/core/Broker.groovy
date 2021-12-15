package kafka.core

import kafka.dto.request.BrokerDto
import utils.StringUtils

class Broker {

    final String bootstrapServer

    Broker(BrokerDto brokerDto) {
        bootstrapServer = brokerDto.getHost()
                .concat(StringUtils.COLON)
                .concat(brokerDto.getPort())
    }
}
