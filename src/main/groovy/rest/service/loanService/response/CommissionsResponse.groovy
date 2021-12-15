package rest.service.loanService.response


import com.fasterxml.jackson.annotation.JsonUnwrapped

class CommissionsResponse {

    @JsonUnwrapped()
    Commissions commissions


    @Override
    String toString() {
        return "CreateCommissionsResponse{" +
                "commissions=" + commissions +
                '}'
    }
}
