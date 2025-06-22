package api;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import exceptions.RequestException;
import fileManager.RefreshTokenFileManager;

/**
 * SpotifyToken
 * Classe que representa o token de autenticação retornado pela API do Spotify.
 * Contém os campos access_token, token_type e expiresIn.
 *
 * @author Daniel Soares Franco - 259083
 */
public class SpotifyToken {
    private Request request;
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
    private LocalDateTime updatedAt;
    private AuthUtils utils = new AuthUtils();

    /**
     * Construtor da classe SpotifyToken.
     * Inicializa o token de autenticação chamando o método refreshToken().
     * 
     * @param request A instância da classe Request que contém informações sobre a
     *                requisição.
     * @throws RequestException se ocorrer um erro de requisição ao atualizar o
     *                          token.
     */
    public SpotifyToken(Request request) throws RequestException {
        this.request = request;
        this.refreshToken = RefreshTokenFileManager.readRefreshToken();
        this.refreshToken();
    }

    /**
     * Retorna a requisição associada a este token.
     * 
     * @return A instância da classe Request que contém informações sobre a
     *         requisição.
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Retorna o token de acesso.
     * 
     * @return O token de acesso.
     */
    public String getAccessToken() {
        LocalDateTime now = LocalDateTime.now();
        if (updatedAt == null || updatedAt.plusSeconds(expiresIn).isBefore(now)) {
            try {
                return this.refreshToken();
            } catch (Exception e) {
                System.out
                        .println("Erro ao atualizar o token de autenticação: " + e.getClass() + ": " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
        return accessToken;
    }

    /**
     * Retorna o token de atualização.
     * 
     * @return O token de atualização.
     */
    public String getRefreshToken() {
        return this.refreshToken;
    }

    /**
     * Define o token de acesso.
     * 
     * @param access_token O token de acesso a ser definido.
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Define o token para recuperação do token de acesso.
     * 
     * @param refresh_token O token para recuperação do token de acesso
     */
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    /**
     * Define o tempo de expiração do token.
     * 
     * @param expiresIn O tempo de expiração do token em segundos.
     */
    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    /**
     * Define a data e hora da última atualização do token.
     * 
     * @param updatedAt A data e hora da última atualização do token.
     */
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Atualiza o token de autenticação, fazendo uma requisição post com form.
     * Se o token de atualização não estiver definido, inicia o processo de login
     * para obter um novo token.
     * 
     * @return O token de acesso atualizado.
     * @throws RequestException se ocorrer um erro ao atualizar o token.
     */
    public String refreshToken() throws RequestException {
        if (this.refreshToken == null || this.refreshToken.isEmpty()) {
            try {
                utils.firstLogin(this);
            } catch (UnsupportedEncodingException e) {
                throw new RequestException("Erro ao redirecionar para a autenticação: " + e.getMessage());
            } catch (ExecutionException e) {
                throw new RequestException("Erro ao obter o código de autorização: " + e.getMessage());
            }
            return this.accessToken;
        } else {
            String tokenUrl = "https://accounts.spotify.com/api/token";
            String state = this.utils.getExpectedState();
            String clientId = this.utils.getClientId(state);
            Map<String, String> body = Map.of(
                    "grant_type", "refresh_token",
                    "refresh_token", this.refreshToken,
                    "client_id", clientId);
            Map<String, String> headers = Map.of(
                    "Authorization", "Basic " + Base64.getEncoder()
                            .encodeToString((clientId + ":" + this.utils.getClientSecret(state)).getBytes()),
                    "Content-Type", "application/x-www-form-urlencoded");
            try {
                Json responseJson = new Json(HttpClientUtil.sendPostFormRequest(tokenUrl, headers, body));
                this.accessToken = "Bearer " + responseJson.get("access_token").toString().replaceAll("\"", "");
                this.expiresIn = Integer.parseInt(responseJson.get("expires_in").toString().replaceAll("\"", ""));
                this.updatedAt = LocalDateTime.now();
                return this.accessToken;
            } catch (Exception e) {
                System.err.println("Erro ao atualizar o token: " + e.getMessage());
                e.printStackTrace();
                return null;
            }
        }
    }
}
