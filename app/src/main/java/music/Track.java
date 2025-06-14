/*
* Track.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.util.ArrayList;

/**
 * Representa uma faixa (música) do Spotify.
 * Contém informações como duração, nome, ID, artistas, álbum e se é explícita.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class Track {
    private int duration; // Duração em milissegundos
    private String name;
    private String id;
    private ArrayList<Artist> artists;
    private Album album;
    private Boolean explicit;

    /**
     * Construtor para criar uma nova instância de Track.
     *
     * @param duration A duração da faixa em milissegundos.
     * @param name O nome da faixa.
     * @param id O ID único da faixa no Spotify.
     * @param artists Uma lista de objetos Artist associados à faixa.
     * @param album O objeto Album ao qual a faixa pertence.
     * @param explicit Um booleano indicando se a faixa é explícita.
     */
    public Track(int duration, String name, String id, ArrayList<Artist> artists, Album album, Boolean explicit) {
        this.duration = duration;
        this.name = name;
        this.id = id;
        this.artists = artists;
        this.album = album;
        this.explicit = explicit;
    }

    /**
     * Retorna a duração da faixa em milissegundos.
     *
     * @return A duração da faixa.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Retorna o nome da faixa.
     *
     * @return O nome da faixa.
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna o ID único da faixa no Spotify.
     *
     * @return O ID da faixa.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna a lista de artistas associados à faixa.
     *
     * @return Uma ArrayList de objetos Artist.
     */
    public ArrayList<Artist> getArtists() {
        return artists;
    }

    /**
     * Retorna o álbum ao qual a faixa pertence.
     *
     * @return O objeto Album.
     */
    public Album getAlbum() {
        return album;
    }

    /**
     * Retorna um booleano indicando se a faixa é explícita.
     *
     * @return True se a faixa for explícita, false caso contrário.
     */
    public Boolean getExplicit() {
        return explicit;
    }
}
