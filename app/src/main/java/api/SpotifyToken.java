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
import spark.Spark;

/**
 * SpotifyToken
 * Classe que representa o token de autenticação retornado pela API do Spotify.
 * Contém os campos access_token, token_type e expires_in.
 */
public class SpotifyToken {
    private Request request;
    private String access_token;
    private String refresh_token;
    private int expires_in;
    private LocalDateTime updatedAt;
    private String clientId = "9afeb5fec9854592994aa191f842b529";
    private String clientSecret = "0e4def4ee8924cb68daba80833c8a5c2"; // Eu juro que vou fazer isso ser mais seguro
    private authUtils utils = new authUtils(this);

    /**
     * Construtor da classe SpotifyToken.
     * Inicializa o token de autenticação chamando o método refreshToken().
     * 
     * @throws RequestException se ocorrer um erro ao atualizar o token.
     */
    public SpotifyToken(Request request) throws RequestException {
        this.request = request;
        this.refresh_token = null;
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
     * Retorna o ID do cliente.
     * 
     * @return O ID do cliente.
     */
    public String getClient_id() {
        return this.clientId;
    }

    /**
     * Retorna o segredo do cliente.
     * 
     * @param state O estado esperado para validação.
     * @return O segredo do cliente se o estado for válido, ou null caso contrário.
     */
    public String getClientSecret(String state) {
        String expectedState = utils.getExpectedState();
        if (state == expectedState) {
            return this.clientSecret;
        } else {
            return null;
        }
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
                System.out
                        .println("Erro ao atualizar o token de autenticação: " + e.getClass() + ": " + e.getMessage());
                e.printStackTrace();
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
     * Retorna o tempo de expiração do token em segundos.
     * 
     * @return O tempo de expiração do token.
     */
    public int getExpires_in() {
        return expires_in;
    }

    /**
     * Define o token de acesso.
     * 
     * @param access_token O token de acesso a ser definido.
     */
    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    /**
     * Define o token para recuperação do token de acesso.
     * 
     * @param refresh_token O token para recuperação do token de acesso
     */
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    /**
     * Define o tempo de expiração do token.
     * 
     * @param expires_in O tempo de expiração do token em segundos.
     */
    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
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
     * Define o objeto utilitário de autenticação.
     * 
     * @param utils O objeto utilitário de autenticação a ser definido.
     */
    public void setUtils(authUtils utils) {
        this.utils = utils;
    }

    /**
     * Atualiza o token de autenticação.
     * Se o token de atualização não estiver definido, inicia o processo de login
     * para obter um novo token.
     * 
     * @return O token de acesso atualizado.
     * @throws RequestException se ocorrer um erro ao atualizar o token.
     */
    public String refreshToken() throws RequestException {
        if (this.refresh_token == null || this.refresh_token.isEmpty()) {
            try {
                utils.firstLogin(this);
            } catch (UnsupportedEncodingException e) {
                throw new RequestException("Erro ao redirecionar para a autenticação: " + e.getMessage());
            } catch (InterruptedException e) {
                throw new RequestException("A autenticação foi interrompida: " + e.getMessage());
            } catch (ExecutionException e) {
                throw new RequestException("Erro ao obter o código de autorização: " + e.getMessage());
            }
            return this.access_token;
        } else {
            String tokenUrl = "https://accounts.spotify.com/api/token";
            String credentials = Base64.getEncoder()
                    .encodeToString((this.clientId + ":" + this.clientSecret).getBytes());
            Map<String, String> body = new HashMap<>();
            body.put("grant_type", "refresh_token");
            body.put("refresh_token", this.refresh_token);
            body.put("client_id", this.clientId);
            String bodyForm = body.entrySet().stream()
                    .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .collect(Collectors.joining("&"));
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
                    throw new RequestException("Erro ao obter o token: " + response.statusCode() + " - "
                            + response.body());
                }
                Json responseJson = new Json(response.body());
                String tempToken = responseJson.readProperty("access_token").toString();
                this.access_token = "Bearer " + tempToken.substring(1, tempToken.length() - 1);
                this.expires_in = Integer.parseInt(responseJson.readProperty("expires_in").toString());
                this.updatedAt = LocalDateTime.now();
                return this.access_token;
            } catch (Exception e) {
                System.err.println("Erro ao atualizar o token: " + e.getMessage());
                return null;
            }
        }
    }
}

/**
 * authUtils
 * Classe utilitária para gerenciar a autenticação com o Spotify.
 * Inicia um servidor local para receber o código de autorização após a
 * autenticação do usuário.
 */
class authUtils {
    private String expectedState = null;
    private SpotifyToken token;
    private CompletableFuture<String> authCodeFuture = new CompletableFuture<>();
    private int port;

    /**
     * Construtor da classe authUtils.
     * Inicializa o token de autenticação do Spotify.
     * 
     * @param token O token de autenticação do Spotify.
     */
    public authUtils(SpotifyToken token) {
        this.token = token;
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
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void firstLogin(SpotifyToken token)
            throws UnsupportedEncodingException, InterruptedException, ExecutionException {
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
            throw new ExecutionException("Erro ao obter o código de autorização", e);
        }
    }

    /**
     * Imprime uma mensagem de erro e finaliza a execução do servidor.
     * 
     * @param message A mensagem de erro a ser impressa.
     * @param res     A resposta HTTP para definir o status e o corpo.
     * @return Uma string HTML com a mensagem de erro.
     */
    private String printError(String message, spark.Response res) {
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
        String clientId = token.getClient_id();
        String clientSecret = token.getClientSecret(this.expectedState);
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
            String tempToken = responseBody.readProperty("access_token").toString();
            String tempRefreshToken = responseBody.readProperty("refresh_token").toString();
            token.setAccess_token("Bearer " + tempToken.substring(1, tempToken.length() - 1));
            token.setRefresh_token(tempRefreshToken.substring(1, tempRefreshToken.length() - 1));
            token.setExpires_in(Integer.parseInt(responseBody.readProperty("expires_in").toString()));
            token.setUpdatedAt(LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("Erro ao trocar código por token: " + e.getMessage());
            return;
        }
    }
}
