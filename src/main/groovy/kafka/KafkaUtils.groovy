package kafka

import kafka.topics.TopicName
import kafka.topics.TopicPrefixName
import utils.StringUtils

class KafkaUtils {

    static buildTopicName(TopicPrefixName topicPrefixName, TopicName topicName) {
        topicPrefixName.getPrefix()
                .concat(StringUtils.HYPHEN)
                .concat(topicName.toCamelCase())
    }
}
