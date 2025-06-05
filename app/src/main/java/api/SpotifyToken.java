package api;

import java.awt.Desktop;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import spark.Spark;

import exceptions.RequestException;

class authUtils {
    private String expectedState = null;
    private CompletableFuture<String> authCodeFuture = new CompletableFuture<>();
    private int port;

    /**
     * Redireciona o usuário para a página de autenticação do Spotify.
     * 
     * @param token O token de autenticação do Spotify.
     * @throws UnsupportedEncodingException se ocorrer um erro ao codificar a URL.
     */
    public void redirect(SpotifyToken token) throws UnsupportedEncodingException {
        this.expectedState = UUID.randomUUID().toString();
        String clientId = token.getClient_id();
        String redirectUri = "http://localhost:8000/callback";
        this.port = 8000;
        String scopes = "playlist-modify-public playlist-modify-private user-read-private";
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString());
        String encodedScopes = URLEncoder.encode(scopes, StandardCharsets.UTF_8.toString());

        String authURL = String.format(
                "https://accounts.spotify.com/authorize?client_id=%s&response_type=code&redirect_uri=%s&state=%s&scope=%s",
                clientId, encodedRedirectUri, this.expectedState, encodedScopes);

        CompletableFuture<String> authCodeFuture = startServer();

        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(authURL));
            } else {
                System.out.println(
                        "Não foi possível abrir o navegador. Por favor, acesse a URL manualmente: \n" + authURL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        authCodeFuture.thenAccept(code -> {
            if (code != null && !code.isEmpty()) {
                System.out.println("Código de autorização recebido: " + code);
                // exchangeCodeForToken(code);
            } else {
                System.err.println("Nenhum código de autorização recebido.");
            }
        }).exceptionally(ex -> {
            System.err.println("Erro ao obter o código de autorização: " + ex.getMessage());
            return null;
        });
    }

    private String printError(String message, spark.Response res) {
        System.err.println(message);
        authCodeFuture.completeExceptionally(new RequestException(message));
        res.status(400);
        return "<html><body><h1>Erro: " + message + "</h1></body></html>";
    }

    public CompletableFuture<String> startServer() {
        this.authCodeFuture = new CompletableFuture<>();

        Spark.port(this.port);
        Spark.get("/callback", (req, res) -> {
            String code = req.queryParams("code");
            String state = req.queryParams("state");
            String error = req.queryParams("error");

            try {
                if (this.expectedState == null || !this.expectedState.equals(state)) {
                    return printError("Estado inválido ou expirado.", res);
                } else if (error != null) {
                    return printError("Erro na autorização do Spotify: " + error, res);
                } else if (code == null || code.isEmpty()) {
                    return printError("Código de autorização não encontrado.", res);
                } else {
                    System.out.println("Código de autorização recebido");
                    authCodeFuture.complete(code);
                    return "<html><body><h1>Autenticação concluída com sucesso!</h1><p>Pode fechar essa aba.</p></body></html>";
                }
            } finally {
                new Thread(() -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.err.println("Erro ao aguardar o término do processamento: " + e.getMessage());
                    }
                    Spark.stop();
                    Spark.awaitStop();
                }).start();
            }
        });

        try {
            Spark.awaitInitialization();
            System.out.println("Servidor Spark iniciado na porta " + this.port);
        } catch (Exception e) {
            System.err.println("Falha ao iniciar o servidor Spark: " + e.getMessage());
            authCodeFuture.completeExceptionally(e);
        }

        return authCodeFuture;
    }
}

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
    private authUtils authUtils;

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
        if (this.refresh_token == null || this.refresh_token.isEmpty()) {
            authUtils utils = new authUtils();
            try {
                utils.redirect(this);
            } catch (UnsupportedEncodingException e) {
                throw new RequestException("Erro ao redirecionar para a autenticação: " + e.getMessage());
            }
            String code = UUID.randomUUID().toString();
            return code;
        } else {
            throw new UnsupportedOperationException("Método refreshToken não implementado.");
        }
    }
}
