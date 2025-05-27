package exceptions;

/**
 * requestException
 */
public class RequestException extends Exception {
    public RequestException(int statusCode, String body){
        super("Houve um erro na requisição. Código do erro: " + statusCode + "\nResposta da requisição: \n" + body);
    }
}
