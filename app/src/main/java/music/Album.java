/*
* Album.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.util.ArrayList;

/**
 * Representa um álbum de música no Spotify.
 * Um álbum contém um número de faixas, um ID único, um nome,
 * os artistas associados e a lista de faixas que compõem o álbum.
 * Implementa a interface {@link MusicSource} por ser uma fonte de conteúdo musical.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class Album implements MusicSource{
    private int numTracks;
    private String id;
    private String name;
    private ArrayList<Artist> artists;
    private ArrayList<Track> tracks;
    
    /**
     * Construtor para criar uma nova instância de Album.
     *
     * @param numTracks O número total de faixas presentes no álbum.
     * @param id O ID único do álbum no Spotify.
     * @param name O nome do álbum.
     * @param artists Uma {@code ArrayList} de objetos {@link Artist} que contribuíram para este álbum.
     * @param tracks Uma {@code ArrayList} de objetos {@link Track} que fazem parte deste álbum.
     */
    public Album(int numTracks, String id, String name, ArrayList<Artist> artists, ArrayList<Track> tracks) {
        this.numTracks = numTracks;
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.tracks = tracks;
    }

    /**
     * Retorna o número total de faixas no álbum.
     *
     * @return O número de faixas.
     */
    public int getNumTracks() {
        return numTracks;
    }

    /**
     * Retorna o ID único do álbum no Spotify.
     * Implementação do método {@code getId()} da interface {@link MusicSource}.
     *
     * @return O ID do álbum.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome do álbum.
     * Implementação do método {@code getName()} da interface {@link MusicSource}.
     *
     * @return O nome do álbum.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retorna a lista de artistas associados a este álbum.
     *
     * @return Uma {@code ArrayList} de objetos {@link Artist}.
     */
    public ArrayList<Artist> getArtists() {
        return artists;
    }

    /**
     * Retorna a lista de faixas contidas neste álbum.
     *
     * @return Uma {@code ArrayList} de objetos {@link Track}.
     */
    public ArrayList<Track> getTracks() {
        return tracks;
    }
}
