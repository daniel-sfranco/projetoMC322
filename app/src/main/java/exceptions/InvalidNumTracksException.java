package exceptions;

/**
 * Exceção personalizada para um número de músicas definido pelo usuário menor
 * do que o necessário para a criação da playlist com os parâmetros informados
 *
 * @author Daniel Soares Franco - 259083
 */
public class InvalidNumTracksException extends Exception {
    /**
     * Construtor que cria o erro para um número de músicas inválido
     * 
     * @param minTracks o número mínimo de músicas necessário
     */
    public InvalidNumTracksException(int minTracks) {
        super("O número de músicas deve ser no mínimo " + minTracks);
    }
}
