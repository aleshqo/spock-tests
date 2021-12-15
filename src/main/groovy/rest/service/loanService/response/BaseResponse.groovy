package rest.service.loanService.response

class BaseResponse {

    Integer statusCode

    ErrorResponse errorResponse


    @Override
    String toString() {
        return "BaseResponse{" +
                "statusCode=" + statusCode +
                ", errorMessage=" + errorResponse +
                '}'
    }
}
