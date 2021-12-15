package rest.service

import groovy.util.logging.Slf4j
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import rest.service.loanService.response.ErrorResponse

import static io.restassured.RestAssured.given
import static utils.JsonParserHelper.getValueFromJson

@Slf4j
class HttpService {

    private String uri

    HttpService(String uri) {
        this.uri = uri
    }

    RequestSpecification getRequest() {
        RequestSpecification request = given()
                .baseUri(uri)
                .contentType(ContentType.JSON)
        request
    }

    def get(String path, Class<?> clazz) {
        RequestSpecification request = getRequest()
        log.info("GET: {}", uri.concat(path))
        Response response = request.get(path)

        if (response.statusCode() != 200) {
            return handleError(response)
        }

        if (!response.getBody().asString().isEmpty()) {
            return handleSuccess(response, clazz)
        }

        if (response.getStatusCode() == 200 && clazz == null) {
            return handleSuccess(response, clazz)
        }
    }

    def post(String path, Map body, Class clazz) {
        RequestSpecification request = getRequest()
        request.body(body)
        log.info("POST: {}", uri.concat(path))
        Response response = request.post(path)

        if (!response.statusCode().toString().startsWith('2')) {
            return handleError(response)
        }
        if (!response.getBody().asString().isEmpty()) {
            return handleSuccess(response, clazz)
        }
    }

    def put(String path, def body = [:], Class clazz = null) {
        RequestSpecification request = getRequest()
        request.body(body)
        log.info("PUT: {}", uri.concat(path))
        Response response = request.put(path)

        if (response.statusCode() != 200) {
            return handleError(response)
        }
        if (!response.getBody().asString().isEmpty()) {
            return handleSuccess(response, clazz)
        }
    }

    private static def handleSuccess(Response response, Class clazz) {
        log.info("SUCCESS: {}", response.getStatusCode())
        if (clazz == null) return
        return getValueFromJson(response.getBody().asString(), clazz)
    }

    private static ErrorResponse handleError(Response response) {
        ErrorResponse errorResponse = response.getBody().as(ErrorResponse)
        errorResponse.setStatusCode(response.getStatusCode())
        log.warn("ERROR: {}", errorResponse.statusCode)
        return errorResponse
    }
}
