package api;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.stream.Collectors;

import exceptions.RequestException;

/**
 * Classe utilitária para enviar requisições HTTP GET e POST usando HttpClient
 * do Java.
 * Fornece métodos para enviar requisições com cabeçalhos personalizados e
 * tratar respostas, incluindo tratamento de erros.
 *
 * @author Daniel Soares Franco - 259083
 */
public class HttpClientUtil {
    private static final HttpClient client = HttpClient.newBuilder().build();

    /**
     * Retorna uma instância do HttpClient.
     *
     * @return a instância do HttpClient
     */
    public static HttpClient getClient() {
        return client;
    }

    /**
     * Envia uma requisição HTTP GET para a URL especificada com os cabeçalhos
     * fornecidos.
     *
     * @param url     a URL para a qual a requisição será enviada
     * @param headers um mapa de cabeçalhos a serem incluídos na requisição
     * @return o corpo da resposta como uma string
     * @throws RequestException se a resposta tiver um código de status >= 400
     */
    public static String sendGetRequest(String url, Map<String, String> headers)
            throws RequestException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url));
        requestBuilder.GET();
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RequestException("Erro de entrada/saída: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RequestException("A requisição foi interrompida: " + e.getMessage());
        }
        if (response.statusCode() >= 400) {
            throw new RequestException((int) response.statusCode(), response.body());
        }
        return response.body();
    }

    /**
     * Envia uma requisição HTTP POST para a URL especificada com o corpo do objeto
     * fornecido.
     *
     * @param url             a URL para a qual a requisição será enviada
     * @param headers         um mapa de cabeçalhos a serem incluídos na requisição
     * @param requestBodyJson o objeto em JSON que será enviado como corpo da
     *                        requisição
     * @return o corpo da resposta como uma string
     * @throws RequestException se a resposta tiver um código de status >=
     *                          400
     */
    public static String sendPostRequest(String url, Map<String, String> headers, Json requestBodyJson)
            throws RequestException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder(URI.create(url));
        requestBuilder.POST(HttpRequest.BodyPublishers.ofString(requestBodyJson.toString()));
        if (headers != null) {
            headers.forEach(requestBuilder::header);
        }
        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RequestException("Erro de entrada/saída: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RequestException("A requisição foi interrompida: " + e.getMessage());
        }
        if (response.statusCode() >= 400) {
            throw new RequestException((int) response.statusCode(), response.body());
        }
        return response.body();
    }

    /**
     * Envia uma requisição HTTP POST para a URL especificada com o corpo do
     * formulário fornecido.
     *
     * @param url      a URL para a qual a requisição será enviada
     * @param headers  um mapa de cabeçalhos a serem incluídos na requisição
     * @param body o corpo do formulário a ser enviado como string
     * @return o corpo da resposta como uma string
     * @throws RequestException se a resposta tiver um código de status >= 400
     */
    public static String sendPostFormRequest(String url, Map<String, String> headers, Map<String, String> body)
            throws RequestException {
        String formBody = body.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder().uri(URI.create(url));
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                requestBuilder = requestBuilder.header(entry.getKey(), entry.getValue());
            }
        }
        HttpRequest request = requestBuilder.POST(HttpRequest.BodyPublishers.ofString(formBody)).build();
        HttpResponse<String> response;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RequestException("Erro de entrada/saída: " + e.getMessage());
        } catch (InterruptedException e) {
            throw new RequestException("A requisição foi interrompida: " + e.getMessage());
        }
        if (response.statusCode() >= 400) {
            throw new RequestException((int) response.statusCode(), response.body());
        }
        return response.body();
    }

    /**
     * Transforma uma query passada como parâmetro em uma query que possa ser usada
     * diretamente na URL de uma requisição
     * 
     * @param query a query a ser transformada
     * @return a mesma query, pronta pra ser usada em uma URL
     */
    public static String QueryURLEncode(String query) {
        try {
            return URLEncoder.encode(query, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Erro ao codificar URl da query: " + e.getMessage());
        }
        return null;
    }
}
