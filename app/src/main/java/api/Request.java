package api;

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

    public String getBaseUrl() {
        return baseUrl;
    }
}
