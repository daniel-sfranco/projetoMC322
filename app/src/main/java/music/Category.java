/*
* Category.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.util.ArrayList;

import exceptions.RequestException;

/**
 * Representa uma categoria de música no Spotify, como "Pop" ou "Rock".
 * Uma categoria é identificada por um ID e um nome, e agrupa várias playlists.
 * Implementa a interface {@link MusicSource} por ser uma fonte de conteúdo
 * musical.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class Category implements MusicSource {
    private String id;
    private String name;
    private ArrayList<String> playlistsIds;

    /**
     * Construtor para criar uma nova instância de Category.
     *
     * @param id           O ID único da categoria no Spotify.
     * @param name         O nome da categoria (e.g., "Pop", "Rock").
     * @param playlistsIds Uma {@code ArrayList} de ids de playlists que fazem parte
     *                     desta categoria.
     */
    public Category(String id, String name, ArrayList<String> playlistsIds) {
        this.id = id;
        this.name = name;
        this.playlistsIds = playlistsIds;
    }

    /**
     * Retorna o ID único da categoria.
     * Implementação do método {@code getId()} da interface {@link MusicSource}.
     *
     * @return O ID da categoria.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome da categoria.
     * Implementação do método {@code getName()} da interface {@link MusicSource}.
     *
     * @return O nome da categoria.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retorna a lista de playlists associadas a esta categoria.
     *
     * @return Uma {@code ArrayList} de ids de playlists.
     */
    public ArrayList<String> getPlaylistsIds() {
        return playlistsIds;
    }

    /**
     * Retorna uma representação em string da categoria, incluindo seu ID, nome e
     * os IDs das playlists associadas.
     *
     * @return Uma string representando a categoria.
     */
    @Override
    public ArrayList<String> getTracksIds() {
        ArrayList<String> tracksIds = new ArrayList<String>();

        for (String playlistId : playlistsIds) {
            try {
                Playlist currentPlaylist = new Playlist(playlistId);
                tracksIds.addAll(currentPlaylist.getTracksIds());
            } catch (RequestException e) {
                System.out.println("Eu ao adicionar faixas da playlist com id " + playlistId);
                System.out.println(e.getMessage());
            }
        }

        return tracksIds;
    }
}
