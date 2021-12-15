package kafka.exception

 class MessageSendingFailedException extends RuntimeException {

     static final String MESSAGE_PATTERN = "Message '%s' sending to the topic '%s' failed due to '%s'."

     MessageSendingFailedException(String failedMessage, String topic, Exception exception) {
        super(String.format(MESSAGE_PATTERN, failedMessage, topic, exception.getMessage()))
    }
}
