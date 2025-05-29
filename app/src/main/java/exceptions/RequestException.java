package exceptions;

/**
 * requestException
 */
public class RequestException extends Exception {
    public RequestException(int statusCode, String body){
        super("A requisição retornou um erro. Código do erro: " + statusCode + "\nResposta da requisição: \n" + body);
    }

    public RequestException(String message) {
        super("Houve um erro ao estabelecer a conexão com o servidor: \n" + message);
    }
}
