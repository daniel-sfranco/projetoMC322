package api;

import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import exceptions.IncorrectClientFileDataException;
import exceptions.RequestException;

/**
 * Request
 * Classe que representa uma requisição para a API do Spotify.
 * Contém informações básicas como URL base, clientId e clientSecret, e gerencia
 * as conexões com a API.
 *
 * @author Daniel Soares Franco - 259083
 */
public class Request {
    private static String baseUrl = "https://api.spotify.com/v1/";
    private SpotifyToken token;

    /**
     * Construtor da classe Request.
     * Inicializa o token de autenticação
     * 
     * @throws RequestException se ocorrer um erro ao inicializar o token.
     * @throws IncorrectClientFileDataException 
     */
    public Request() throws RequestException{
        this.token = new SpotifyToken(this);
    }

    /**
     * Retorna a url base da API do Spotify.
     * 
     * @return A URL base da API do Spotify.
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * Retorna o token de autenticação utilizado para as requisições.
     * 
     * @return o token.
     */
    public SpotifyToken getToken() {
        return token;
    }

    /**
     * Define o token de autenticação a ser utilizado nas requisições.
     * 
     * @param token O token de autenticação a ser definido.
     */
    public void setToken(SpotifyToken token) {
        this.token = token;
    }

    /**
     * Envia uma requisição POST para a URL especificada com o corpo do objeto
     * fornecido.
     * 
     * @param url        A URL relativa para a qual a requisição será enviada.
     * @param bodyObject O objeto que será convertido em JSON e enviado como corpo
     *                   da requisição.
     * @return A resposta da requisição como uma string.
     * @throws RequestException        se ocorrer um erro ao enviar a requisição.
     * @throws JsonProcessingException se ocorrer um erro ao processar o objeto
     */
    public Json sendPostRequest(String url, Json bodyJson) throws RequestException, JsonProcessingException {
        String accessToken = this.token.getAccess_token();
        Map<String, String> headers = Map.of(
                "Authorization", accessToken,
                "Content-Type", "application/json");
        return new Json(HttpClientUtil.sendPostRequest(baseUrl + url, headers, bodyJson));
    }

    /**
     * Envia uma requisição GET para a URL especificada.
     * 
     * @param url A URL relativa para a qual a requisição será enviada.
     * @return A resposta da requisição como uma string.
     * @throws RequestException se ocorrer um erro ao enviar a requisição.
     */
    public Json sendGetRequest(String url) throws RequestException {
        String accessToken = this.token.getAccess_token();
        Map<String, String> headers = Map.of(
                "Authorization", accessToken,
                "Content-Type", "application/json");
        return new Json(HttpClientUtil.sendGetRequest(baseUrl + url, headers));
    }

    /**
     * Método principal para testar a obtenção do token de autenticação.
     * 
     * @param args Argumentos da linha de comando (não utilizados).
     * @throws RequestException se ocorrer um erro ao enviar a requisição.
     * @throws IncorrectClientFileDataException 
     */
    public static void main(String[] args) throws RequestException, IncorrectClientFileDataException {
        Request request = new Request();
        Json response = request.sendGetRequest("me");
        System.out.println(response);
    }
}
