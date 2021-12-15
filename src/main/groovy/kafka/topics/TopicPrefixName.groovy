package kafka.topics

enum TopicPrefixName {

    INDIA("india"),
    LANKA("slk"),
    DOPOLUCHKINO("dpl"),
    ROBOCREDIT("robocredit"),
    ZAYMER("zaymer"),
    KZ("kz"),
    RU_SALARY("slru")

    String prefix

    TopicPrefixName(String prefix) {
        this.prefix = prefix
    }
}
