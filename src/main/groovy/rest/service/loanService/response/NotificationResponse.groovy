package rest.service.loanService.response


import com.fasterxml.jackson.annotation.JsonProperty

class NotificationResponse {

    @JsonProperty("action")
    String action

    @JsonProperty("when")
    String when

    @JsonProperty("data")
    NotificationData data

    @JsonProperty("notificationType")
    String notificationType


    @Override
    String toString() {
        return "NotificationResponse{" +
                "action='" + action + '\'' +
                ", when='" + when + '\'' +
                ", data=" + data +
                ", notificationType=" + notificationType +
                '}'
    }
}
