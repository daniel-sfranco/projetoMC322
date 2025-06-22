package api;

import java.awt.Desktop;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import exceptions.RequestException;
import fileManager.ClientDataFileManager;
import fileManager.RefreshTokenFileManager;
import spark.Spark;

/**
 * AuthUtils
 * Classe utilitária para gerenciar a autenticação com o Spotify.
 * Inicia um servidor local para receber o código de autorização após a
 * autenticação do usuário.
 *
 * @author Daniel Soares Franco - 259083
 */
public class AuthUtils {
    private String clientId;
    private String clientSecret;
    private String expectedState = null;
    private CompletableFuture<String> authCodeFuture = new CompletableFuture<>();
    private int port;

    /**
     * Construtor da classe authUtils.
     * Gera um estado esperado aleatório para validação
     */
    public AuthUtils() {
        Map<String, String> credentials = ClientDataFileManager.readClientData();
        this.clientId = credentials.get("clientId");
        this.clientSecret = credentials.get("clientSecret");
        this.expectedState = UUID.randomUUID().toString();
    }

    /**
     * Retorna o ID do cliente.
     * 
     * @param state o estado esperado para validação
     * @return O ID do cliente se o estado for válido, ou null caso contrário.
     */
    public String getClientId(String state) {
        String expectedState = this.expectedState;
        if (state == expectedState) {
            return this.clientId;
        }
        return null;
    }

    /**
     * Retorna o segredo do cliente.
     * 
     * @param state O estado esperado para validação.
     * @return O segredo do cliente se o estado for válido, ou null caso contrário.
     */
    public String getClientSecret(String state) {
        String expectedState = this.expectedState;
        if (state == expectedState) {
            return this.clientSecret;
        }
        return null;
    }

    /**
     * Retorna o estado esperado para validação.
     * 
     * @return O estado esperado.
     */
    public String getExpectedState() {
        return expectedState;
    }

    /**
     * Redireciona o usuário para a página de autenticação do Spotify.
     * 
     * @param token O token de autenticação do Spotify.
     * @throws UnsupportedEncodingException se ocorrer um erro ao codificar a URL.
     * @throws ExecutionException           caso aconteça algum problema ao abrir o
     *                                      navegador ou ao obter o código de acesso
     */
    public void firstLogin(SpotifyToken token)
            throws UnsupportedEncodingException, ExecutionException {
        String redirectUri = "http://localhost:8000/callback";
        this.port = 8000;
        String scopes = "playlist-modify-public playlist-modify-private user-read-private user-read-email user-follow-read";
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
            System.err.println("Erro ao abrir o navegador: " + e.getMessage());
            throw new ExecutionException(e);
        }

        String code = null;
        try {
            code = authCodeFuture.get();
            if (code != null && !code.isEmpty()) {
                exchangeCodeForToken(token, code);
            } else {
                System.err.println("Nenhum código de autorização recebido.");
            }
        } catch (Exception e) {
            System.err.println("Erro ao obter o código de autorização: " + e.getMessage());
            throw new ExecutionException(e);
        }
    }

    /**
     * Imprime uma mensagem de erro e finaliza a execução do servidor.
     * 
     * @param message A mensagem de erro a ser impressa.
     * @param res     A resposta HTTP para definir o status e o corpo.
     * @return Uma string HTML com a mensagem de erro.
     */
    public String printError(String message, spark.Response res) {
        System.err.println(message);
        authCodeFuture.completeExceptionally(new RequestException(message));
        res.status(400);
        return "<html><body><h1>Erro: " + message + "</h1></body></html>";
    }

    /**
     * Inicia um servidor Spark na porta especificada para receber o código de
     * autorização do Spotify.
     * 
     * @return Um CompletableFuture que será completado com o código de autorização
     *         ou uma exceção em caso de erro.
     */
    private CompletableFuture<String> startServer() {
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

    /**
     * Troca o código de autorização recebido por um token de acesso.
     * 
     * @param token O token de autenticação do Spotify.
     * @param code  O código de autorização recebido.
     */
    private void exchangeCodeForToken(SpotifyToken token, String code) {
        String redirectUri = "http://localhost:8000/callback";
        String tokenUrl = "https://accounts.spotify.com/api/token";

        String credentials = Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());

        Map<String, String> body = new HashMap<>();
        body.put("grant_type", "authorization_code");
        body.put("code", code);
        body.put("redirect_uri", redirectUri);

        String bodyForm = body.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Basic " + credentials);
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Authorization", "Basic " + credentials)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(bodyForm))
                    .build();
            HttpResponse<String> response = HttpClientUtil.getClient().send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                throw new RequestException("Erro ao obter o token: " + response.statusCode() + " - " + response.body());
            }
            Json responseBody = new Json(response.body());
            String tempToken = responseBody.get("access_token").toString();
            String tempRefreshToken = responseBody.get("refresh_token").toString();
            token.setAccessToken("Bearer " + tempToken.replaceAll("\"", ""));
            token.setRefreshToken(tempRefreshToken.replaceAll("\"", ""));
            token.setExpiresIn(Integer.parseInt(responseBody.get("expires_in").toString()));
            token.setUpdatedAt(LocalDateTime.now());
            RefreshTokenFileManager.writeRefreshToken(token.getRefreshToken());
        } catch (Exception e) {
            System.err.println("Erro ao trocar código por token: " + e.getMessage());
            return;
        }
    }
}
