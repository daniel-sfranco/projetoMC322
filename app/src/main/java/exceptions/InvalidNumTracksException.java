package exceptions;

public class InvalidNumTracksException extends Exception{
    public InvalidNumTracksException(int minTracks){
        super("O número de músicas deve ser no mínimo %s" + minTracks);
    }
}
