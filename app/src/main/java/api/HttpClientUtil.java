package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;

import exceptions.RequestException;

/**
 * HttpClientUtil
 */
public class HttpClientUtil {
    private static final HttpClient client = HttpClient.newBuilder().build();

    public static String sendGetRequest(String url, Map<String, String> headers)
            throws IOException, JsonProcessingException, RequestException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url));
        requestBuilder.GET();
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
        String requestBodyJson = JsonUtil.parseObjectToJson(bodyObject);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url));
        requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBodyJson));
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

    public static void main(String[] args) {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-type", "application/json; charset=UTF-8");
        try {
            HttpClientUtil.sendGetRequest("https://jsonplaceholder.typicode.com/posts", headers);
            System.out.println("Requisição get enviada com sucesso");
            HttpClientUtil.sendPostRequest("https://jsonplaceholder.typicode.com/posts", headers, headers);
            System.out.println("Requisição post enviada com sucesso");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
