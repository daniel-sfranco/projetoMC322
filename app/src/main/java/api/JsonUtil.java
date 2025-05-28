package api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JsonUtil
 */
public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String parseObjectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            System.out.println("Erro ao converter o objeto para JSON: " + e.getMessage());
            return null;
        }
    }

    public static <T> T parseJson(String json, Class<T> valueType) {
        try {
            return mapper.readValue(json, valueType);
        } catch (JsonMappingException e) {
            System.out.println("Erro de mapeamento JSON: " + e.getMessage());
        } catch (JsonProcessingException e) {
            System.out.println("Erro ao processar JSON: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro desconhecido: " + e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) {
        String jsonRequest = parseObjectToJson(new Request());
        System.out.println(jsonRequest);
		Request parsedRequest = parseJson(jsonRequest, Request.class);
        System.out.println(parsedRequest.toString());
    }
}
