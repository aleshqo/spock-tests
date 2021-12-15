package kafka.service

import kafka.ConnectionController
import kafka.topics.TopicName
import kafka.topics.TopicPrefixName
import utils.StringUtils

class CleanTopicService {

    static void cleanTopics(List<TopicName> topics, TopicPrefixName topicPrefixName) {
        topics.forEach({
            String topicName = topicPrefixName.getPrefix()
                    .concat(StringUtils.HYPHEN)
                    .concat(it.toCamelCase())
             ConnectionController.KAFKA_CLIENT.getMessages(topicName, 1000)
        })

    }
}
