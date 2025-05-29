package api;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import exceptions.RequestException;

/**
 * SpotifyToken
 * Classe que representa o token de autenticação retornado pela API do Spotify.
 * Contém os campos access_token, token_type e expires_in.
 */
class SpotifyToken {
    private String access_token;
    private String token_type;
    private int expires_in;
    private LocalDateTime updatedAt;
    private String clientId = "9afeb5fec9854592994aa191f842b529";
    private String clientSecret = "0e4def4ee8924cb68daba80833c8a5c2"; // Eu juro que vou fazer isso ser mais seguro

    /**
     * Construtor da classe SpotifyToken.
     * Inicializa o token de autenticação chamando o método refreshToken().
     * 
     * @throws RequestException se ocorrer um erro ao atualizar o token.
     */
    public SpotifyToken() throws RequestException {
        this.refreshToken();
    }

    /**
     * Retorna o token de acesso.
     * 
     * @return O token de acesso.
     */
    public String getAccess_token() {
        LocalDateTime now = LocalDateTime.now();
        if (updatedAt == null || updatedAt.plusSeconds(expires_in).isBefore(now)) {
            try {
                return this.refreshToken();
            } catch (Exception e) {
                System.out.println("Erro ao atualizar o token de autenticação: " + e.getMessage());
                return null;
            }
        }
        return access_token;
    }

    /**
     * Retorna o tipo de token.
     * 
     * @return O tipo de token.
     */
    public String getToken_type() {
        return token_type;
    }

    /**
     * Retorna o tempo de expiração do token em segundos.
     * 
     * @return O tempo de expiração do token.
     */
    public int getExpires_in() {
        return expires_in;
    }

    /**
     * Atualiza o token de autenticação.
     * Faz uma requisição para a API do Spotify para obter um novo token.
     * 
     * @return O novo token de acesso ou null em caso de falha.
     */
    public String refreshToken() throws RequestException {
        String body = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s", clientId,
                clientSecret);
        Map<String, String> headers = Map.of(
                "Content-Type", "application/x-www-form-urlencoded");
        String response;
        response = HttpClientUtil.sendPostFormRequest(
                "https://accounts.spotify.com/api/token",
                headers,
                body);
        Map<String, String> params = (Map<String, String>) JsonUtil.parseJson(response,
                new TypeReference<HashMap<String, String>>() {
                });
        this.access_token = params.get("access_token");
        this.token_type = params.get("token_type");
        this.expires_in = Integer.parseInt(params.get("expires_in"));
        this.updatedAt = LocalDateTime.now();
        return access_token;
    }
}

/**
 * Request
 * Classe que representa uma requisição para a API do Spotify.
 * Contém informações básicas como URL base, clientId e clientSecret, e gerencia
 * as conexões com a API.
 */
public class Request {
    private static String baseUrl = "https://api.spotify.com/v1/";
    private SpotifyToken token;

    /**
     * Construtor da classe Request.
     * Inicializa o token de autenticação.
     * 
     * @throws RequestException se ocorrer um erro ao obter o token.
     */
    public Request() throws RequestException {
        this.token = new SpotifyToken();
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
     * Envia uma requisição POST para a URL especificada com o corpo do objeto
     * fornecido.
     * 
     * @param url        A URL para a qual a requisição será enviada.
     * @param bodyObject O objeto que será convertido em JSON e enviado como corpo
     *                   da requisição.
     * @return A resposta da requisição como uma string.
     * @throws RequestException se ocorrer um erro ao enviar a requisição.
     */
    public String sendPostRequest(String url, Object bodyObject) throws RequestException {
        String accessToken = this.token.getAccess_token();
        Map<String, String> headers = Map.of(
                "Authorization", "Bearer " + accessToken,
                "Content-Type", "application/json");
        return HttpClientUtil.sendPostRequest(url, headers, bodyObject);
    }

    /**
     * Envia uma requisição GET para a URL especificada.
     * 
     * @param url A URL para a qual a requisição será enviada.
     * @return A resposta da requisição como uma string.
     * @throws RequestException se ocorrer um erro ao enviar a requisição.
     */
    public String sendGetRequest(String url) throws RequestException {
        String accessToken = this.token.getAccess_token();
        Map<String, String> headers = Map.of(
                "Authorization", "Bearer " + accessToken,
                "Content-Type", "application/json");
        return HttpClientUtil.sendGetRequest(url, headers);
    }

    /**
     * Método principal para testar a obtenção do token de autenticação.
     * 
     * @param args Argumentos da linha de comando (não utilizados).
     * @throws RequestException
     */
    public static void main(String[] args) throws RequestException {
        Request request = new Request();
        try {
            String requestUrl = request.getBaseUrl() + "tracks/6nH5L0CDejkvcMxlO1NLWf";
            System.out.println(requestUrl);
            String response = request.sendGetRequest(requestUrl);
            System.out.println(response);
        } catch (RequestException e) {
            System.out.println("Erro ao obter a música: " + e.getMessage());
        }
    }
}
