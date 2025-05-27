package api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import exceptions.RequestException;

/**
 * HttpClientUtil
 */
public class HttpClientUtil {
    private static final HttpClient client = HttpClient.newBuilder().build();
    private static final ObjectMapper mapper = new ObjectMapper();

    public String sendGetRequest(String url, Map<String, String> headers) throws IOException, JsonProcessingException, RequestException, InterruptedException{
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url)).GET();
        if(headers != null) {
            headers.forEach(requestBuilder::header);
        }
        HttpRequest request = requestBuilder.build();
        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
        String jsonBody = mapper.writeValueAsString(response.body());
        if(response.statusCode() >= 400){
            throw new RequestException((int) response.statusCode(), jsonBody);
        }
        return jsonBody;
    }
}

