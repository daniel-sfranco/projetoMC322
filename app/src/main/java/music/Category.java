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

/**
 * Representa uma categoria de música no Spotify, como "Pop" ou "Rock".
 * Uma categoria é identificada por um ID e um nome, e agrupa várias playlists.
 * Implementa a interface {@link MusicSource} por ser uma fonte de conteúdo musical.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class Category implements MusicSource {
    private String id;
    private String name;
    private ArrayList<Playlist> playlists;
    
    /**
     * Construtor para criar uma nova instância de Category.
     *
     * @param id O ID único da categoria no Spotify.
     * @param name O nome da categoria (e.g., "Pop", "Rock").
     * @param playlists Uma {@code ArrayList} de objetos {@link Playlist} que fazem parte desta categoria.
     */
    public Category(String id, String name, ArrayList<Playlist> playlists) {
        this.id = id;
        this.name = name;
        this.playlists = playlists;
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
     * @return Uma {@code ArrayList} de objetos {@link Playlist}.
     */
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    /**
     * Retorna todas as faixas contidas nas playlists desta categoria.
     * Este método itera sobre todas as playlists associadas à categoria e coleta todas as faixas de cada playlist.
     * Implementação do método {@code getTracks()} da interface {@link MusicSource}.
     *
     * @return Uma {@code ArrayList} de objetos {@link Track} que representam todas as faixas encontradas nas playlists da categoria.
     * Retorna uma lista vazia se a categoria não tiver playlists ou se as playlists não contiverem faixas.
     */
    public ArrayList<Track> getTracks() {
        ArrayList<Track> tracks = new ArrayList<Track>();
        for (Playlist playlist : playlists) {
            tracks.addAll(playlist.getTracks());
        }

        return tracks;
    }
}
