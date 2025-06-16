package exceptions;

/**
 * Exceção personalizada para erros relacionados a requisições HTTP.
 * Esta classe estende a classe {@link Exception} e é usada para indicar
 * problemas ao fazer requisições para APIs, como erros de conexão ou respostas
 * com códigos de status HTTP indicando falhas.
 * 
 * @author Daniel Soares Franco - 259083
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
