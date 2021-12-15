package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class ErrorResponse {

    Integer statusCode

    @JsonProperty("error")
    String message


    @Override
    String toString() {
        return "ErrorResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                '}'
    }
}
