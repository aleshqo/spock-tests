package utils

import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.fasterxml.jackson.databind.type.TypeFactory
import groovy.util.logging.Slf4j

@Slf4j
class JsonParserHelper {

    private static final objectMapper = new ObjectMapper()
            .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)

    static String convertValueToJson(Map messageBody) {
        objectMapper.writeValueAsString(messageBody)
    }

    static def getValueFromJson(String jsonString, Class clazz) {
        try {
            objectMapper.readValue(jsonString, clazz)
        } catch (MismatchedInputException e) {
            getFromJSONCollection(jsonString, clazz)
        }
    }

    static <T> List<T> getFromJSONCollection(String jsonString, final Class<T> type) throws IOException {
        try {
            return objectMapper.readValue(jsonString, TypeFactory.defaultInstance().constructCollectionType(ArrayList.class, type))
        } catch (JsonMappingException e) {
            log.warn(e.message)
            return getFromJSON(jsonString, type)
        }
    }

    static <T> List<T> getFromJSON(final String jsonString, final Class<T> type) throws IOException {
        return new ArrayList<T>() {
            {
                add(objectMapper.readValue(jsonString, type))
            }
        }
    }
}
