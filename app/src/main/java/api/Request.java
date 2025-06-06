package api;

import java.util.Map;

import exceptions.RequestException;

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
                "Authorization", accessToken,
                "Content-Type", "application/json");
        System.out.println(headers);
        return HttpClientUtil.sendGetRequest(url, headers);
    }

    public void setToken(SpotifyToken token) {
        this.token = token;
    }

    public void requestExample() {
        try {
            String requestUrl = Request.baseUrl + "tracks/6nH5L0CDejkvcMxlO1NLWf";
            System.out.println(requestUrl);
            String response = this.sendGetRequest(requestUrl);
            System.out.println(response);
        } catch (RequestException e) {
            System.out.println("Erro ao obter a música: " + e.getMessage());
        }
    }

    /**
     * Método principal para testar a obtenção do token de autenticação.
     * 
     * @param args Argumentos da linha de comando (não utilizados).
     * @throws RequestException
     */
    public static void main(String[] args) throws RequestException {
        Request request = new Request();
        request.setToken(new SpotifyToken(request));
    }
}
