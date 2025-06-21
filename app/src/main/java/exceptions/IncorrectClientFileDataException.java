package exceptions;

/**
 * Exceção personalizada lançada quando ocorre um problema com os dados do
 * cliente lidos ou escritos em um arquivo. Isso pode incluir dados ausentes,
 * formato incorreto ou qualquer inconsistência que impeça o processamento
 * adequado das credenciais do cliente (Client ID e Client Secret).
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class IncorrectClientFileDataException extends Exception {
    /**
     * Construtor que cria uma nova instância de IncorrectClientFileDataException
     * com uma mensagem de erro específica.
     *
     * @param message A mensagem de erro que descreve o problema encontrado.
     */
    public IncorrectClientFileDataException(String message) {
        super(message);
    }
}
