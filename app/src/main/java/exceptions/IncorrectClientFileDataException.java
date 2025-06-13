package exceptions;

/**
 * Exceção personalizada lançada quando ocorre um problema com os dados do cliente
 * lidos ou escritos em um arquivo. Isso pode incluir dados ausentes,
 * formato incorreto ou qualquer inconsistência que impeça o processamento adequado
 * das credenciais do cliente (Client ID e Client Secret).
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class IncorrectClientFileDataException extends Exception {
    public IncorrectClientFileDataException(String message) {
        super(message);
    }
}
