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
 * Classe utilitária para enviar requisições HTTP GET e POST usando HttpClient
 * do Java.
 * Fornece métodos para enviar requisições com cabeçalhos personalizados e
 * tratar
 * respostas, incluindo tratamento de erros.
 */
public class HttpClientUtil {
    private static final HttpClient client = HttpClient.newBuilder().build();

    /**
     * Envia uma requisição HTTP GET para a URL especificada com os cabeçalhos
     * fornecidos.
     *
     * @param url     a URL para a qual a requisição será enviada
     * @param headers um mapa de cabeçalhos a serem incluídos na requisição
     * @return o corpo da resposta como uma string
     * @throws IOException             se ocorrer um erro de entrada/saída
     * @throws JsonProcessingException se ocorrer um erro ao processar JSON
     * @throws RequestException        se a resposta tiver um código de status >=
     *                                 400
     * @throws InterruptedException    se a thread for interrompida durante a
     *                                 requisição
     */
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

    /**
     * Envia uma requisição HTTP POST para a URL especificada com os cabeçalhos e
     * corpo fornecidos.
     *
     * @param url        a URL para a qual a requisição será enviada
     * @param headers    um mapa de cabeçalhos a serem incluídos na requisição
     * @param bodyObject o objeto que será convertido em JSON e enviado como corpo
     *                   da requisição
     * @return o corpo da resposta como uma string
     * @throws IOException          se ocorrer um erro de entrada/saída
     * @throws InterruptedException se a thread for interrompida durante a
     *                              requisição
     * @throws RequestException     se a resposta tiver um código de status >= 400
     */
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

    /**
     * Método principal para testar o envio de requisições GET e POST.
     *
     * @param args argumentos da linha de comando (não utilizados)
     */
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
