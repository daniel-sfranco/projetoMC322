package api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

import exceptions.RequestException;

/**
 * SpotifyToken
 * Classe que representa o token de autenticação retornado pela API do Spotify.
 * Contém os campos access_token, token_type e expires_in.
 */
public class SpotifyToken {
    private String access_token;
    private String refresh_token;
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
        this.refresh_token = null;
        this.refreshToken();
    }

    /**
     * Retorna o ID do cliente.
     * 
     * @return O ID do cliente.
     */
    public String getClient_id() {
        return this.clientId;
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
     * Retorna o token de atualização.
     * 
     * @return O token de atualização.
     */
    public String getRefresh_token() {
        return this.refresh_token;
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
        throw new UnsupportedOperationException("Método refreshToken não implementado.");
    }
}
