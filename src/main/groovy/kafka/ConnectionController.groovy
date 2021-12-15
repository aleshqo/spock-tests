package kafka

class ConnectionController {


    private static Properties properties = initProperties()
    private static final String HOST = properties.kafka_host
    private static final String PORT = properties.kafka_port
    static final KafkaClient KAFKA_CLIENT = new KafkaClient(HOST, PORT)

    private static Properties initProperties() {
        Properties properties = new Properties()
        properties.load(ConnectionController.class.getResourceAsStream("/kafka.properties"))
        properties
    }


}
