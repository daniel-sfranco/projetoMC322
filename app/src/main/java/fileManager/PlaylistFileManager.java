package fileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * PlaylistFileManager
 * Classe que gerencia o arquivo User/PlaylistsIds.che, que armazena
 * os ids das playlists geradas pelo aplicativo, para poder direcionar para
 * o spotify para edições na aba "acessar"
 */
public class PlaylistFileManager extends FileManager {
    /**
     * Função que será executada na primeira chamada a um método estático da classe
     * Define qual é o local específico da classe onde está o arquivo
     */
    public static void setLocation() {
        FileManager.SPECIFIC_LOCATION = FileManager.FILES_LOCATION + "User" + File.separator + "PlaylistsIds.che";
    }

    /**
     * Adiciona o id de uma playlist ao arquivo PlaylistsIds.che
     *
     * @param playlistId o id da playlist a ser adicionado no arquivo
     */
    public static void addPlaylistId(String playlistId) {
        setLocation();
        ArrayList<String> lines = new ArrayList<String>(
                Arrays.asList(playlistId));

        FileManager.addData(lines);
    }

    /**
     * Retorna os ids das playlists que foram criadas pelo app, que estão
     * armazenadas no arquivo PlaylistsIds.che
     *
     * @return os ids das playlists armazenadas
     */
    public static ArrayList<String> getUserPlaylists() {
        setLocation();
        return FileManager.readFile();
    }

    /**
     * Deleta uma playlist do arquivo PlaylistsIds.che. A playlist não será
     * deletada do spotify, apenas do registro local.
     *
     * @param playlistId o id da playlist a ser deletada
     */
    public static void deletePlaylistId(String playlistId) {
        setLocation();
        FileManager.deleteLine(playlistId);
    }
}
