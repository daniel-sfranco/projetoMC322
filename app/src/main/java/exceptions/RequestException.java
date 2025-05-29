package exceptions;

/**
 * requestException
 */
public class RequestException extends Exception {
    /**
     * Construtor da classe RequestException para erros na requisição.
     * Inicializa a exceção com uma mensagem de erro personalizada.
     *
     * @param statusCode o código de status HTTP retornado pela requisição
     * @param body       o corpo da resposta da requisição
     */
    public RequestException(int statusCode, String body) {
        super("A requisição retornou um erro. Código do erro: " + statusCode + "\nResposta da requisição: \n" + body);
    }

    /**
     * Construtor da classe RequestException para erros na conexão com o endpoint.
     * Inicializa a exceção com uma mensagem de erro personalizada.
     *
     * @param message a mensagem de erro a ser exibida
     */
    public RequestException(String message) {
        super("Houve um erro ao estabelecer a conexão com o servidor: \n" + message);
    }
}
