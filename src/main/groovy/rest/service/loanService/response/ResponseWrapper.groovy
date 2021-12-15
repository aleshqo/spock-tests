package rest.service.loanService.response

class ResponseWrapper {

    Object response
    ErrorResponse error

    boolean isError() {
        return error != null
    }


    @Override
    String toString() {
        return "ResponseWrapper{" +
                "response=" + response +
                ", error=" + error +
                '}'
    }
}
