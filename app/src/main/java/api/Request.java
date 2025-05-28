package api;

import java.util.Map;

/**
 * SpotifyToken
 * Classe que representa o token de autenticação retornado pela API do Spotify.
 * Contém os campos access_token, token_type e expires_in.
 */
class SpotifyToken {
    private String access_token;
    private String token_type;
    private int expires_in;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }
}

/**
 * Request
 * Classe que representa uma requisição para a API do Spotify.
 * Contém informações básicas como URL base, clientId e clientSecret, e gerencia
 * as conexões com a API.
 */
public class Request {
    private String baseUrl = "https://api.spotify.com/";
    // Eu juro que vou fazer isso ser mais seguro
    private String clientId = "9afeb5fec9854592994aa191f842b529";
    private String clientSecret = "0e4def4ee8924cb68daba80833c8a5c2";
    private SpotifyToken token;

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
     * Solicita um token de autenticação da API do Spotify.
     * Utiliza o clientId e clientSecret para obter o token.
     * 
     * @return O token de autenticação ou null em caso de falha.
     */
    public String requestToken() {
        try {
            String body = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s", clientId,
                    clientSecret);
            Map<String, String> headers = Map.of(
                    "Content-Type", "application/x-www-form-urlencoded");

            String response = HttpClientUtil.sendPostFormRequest(
                    "https://accounts.spotify.com/api/token",
                    headers,
                    body);
            SpotifyToken token = JsonUtil.parseJson(response, SpotifyToken.class);
            this.token = token;
            return token.getAccess_token();
        } catch (Exception e) {
            System.out.println("Erro ao obter token de autenticação: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        Request request = new Request();
        String token = request.requestToken();
        if (token != null) {
            System.out.println("Token de autenticação obtido com sucesso: " + request.getToken().getAccess_token());
        } else {
            System.out.println("Falha ao obter o token de autenticação.");
        }
    }
}
