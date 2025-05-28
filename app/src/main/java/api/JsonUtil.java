package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonUtil
 */
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static <T> T parseJson(String json, Class<T> valueType)
            throws JsonMappingException, JsonProcessingException {
        return mapper.readValue(json, valueType);
    }

    public static String parseObjectToJson(Object object) throws JsonProcessingException {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            System.out.println("Erro ao converter o objeto para JSON: " + e.getMessage());
            return null;
        }
    }
}
