package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.RequestException;

/**
 * HttpClientUtil
 */
public class HttpClientUtil {
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String sendGetRequest(String url, Map<String, String> headers)
            throws IOException, JsonProcessingException, RequestException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url)).GET();
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 400) {
            throw new RequestException((int) response.statusCode(), response.body());
        }
        return response.body();
    }

    public static String sendPostRequest(String url, Map<String, String> headers, Object bodyObject)
            throws IOException, InterruptedException, RequestException {
        String requestBodyJson = mapper.writeValueAsString(bodyObject);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson));
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
        if (response.statusCode() >= 400) {
            throw new RequestException((int) response.statusCode(), response.body());
        }
        return response.body();
    }

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
