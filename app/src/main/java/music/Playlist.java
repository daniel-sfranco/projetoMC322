/*
* Playlist.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.util.ArrayList;

import user.User;

/**
 * Representa uma playlist no Spotify.
 * Uma playlist é uma coleção de faixas (músicas), com informações como o número total de faixas,
 * um ID único, nome, o usuário proprietário e a lista de faixas contidas nela.
 * Implementa a interface {@link MusicSource} para indicar que pode ser uma fonte de música.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class Playlist implements MusicSource {
    private int numTracks;
    private String id;
    private String name;
    private User owner;
    private ArrayList<Track> tracks;
   
    /**
     * Construtor para criar uma nova instância de Playlist.
     *
     * @param numTracks O número total de faixas na playlist.
     * @param id O ID único da playlist no Spotify.
     * @param name O nome da playlist.
     * @param owner O objeto User que representa o proprietário da playlist.
     * @param tracks Uma lista de objetos Track contidos na playlist.
     */
    public Playlist(int numTracks, String id, String name, User owner, ArrayList<Track> tracks) {
        this.numTracks = numTracks;
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.tracks = tracks;
    }

    /**
     * Retorna o número total de faixas na playlist.
     *
     * @return O número de faixas.
     */
    public int getNumTracks() {
        return numTracks;
    }

    /**
     * Retorna o ID único da playlist no Spotify.
     * Implementação do método {@code getId()} da interface {@link MusicSource}.
     *
     * @return O ID da playlist.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome da playlist.
     * Implementação do método {@code getName()} da interface {@link MusicSource}.
     *
     * @return O nome da playlist.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retorna o usuário proprietário da playlist.
     *
     * @return O objeto User que é o proprietário.
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Retorna a lista de faixas contidas na playlist.
     *
     * @return Uma ArrayList de objetos Track.
     */
    public ArrayList<Track> getTracks() {
        return tracks;
    }
}
