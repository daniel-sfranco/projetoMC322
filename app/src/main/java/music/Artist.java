/*
* Artist.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.util.ArrayList;

/**
 * Representa um artista no Spotify.
 * Um artista possui um nome, um ID único e uma lista de álbuns associados.
 * Implementa a interface {@link MusicSource} para indicar que pode ser uma fonte de música.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class Artist implements MusicSource {
    private String name;
    private String Id;
    private ArrayList<Album> albuns;

    /**
     * Construtor para criar uma nova instância de Artist.
     *
     * @param name O nome do artista.
     * @param Id O ID único do artista no Spotify.
     * @param albuns Uma lista de objetos {@link Album} associados a este artista.
     */
    public Artist(String name, String Id, ArrayList<Album> albuns) {
        this.name = name;
        this.Id = Id;
        this.albuns = albuns;
    }

    /**
     * Retorna o nome do artista.
     * Implementação do método {@code getName()} da interface {@link MusicSource}.
     *
     * @return O nome do artista.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retorna o ID único do artista no Spotify.
     * Implementação do método {@code getId()} da interface {@link MusicSource}.
     *
     * @return O ID do artista.
     */
    @Override
    public String getId() {
        return Id;
    }

    /**
     * Retorna a lista de álbuns associados a este artista.
     *
     * @return Uma {@code ArrayList} de objetos {@link Album}.
     */
    public ArrayList<Album> getAlbuns() {
        return albuns;
    }

    /**
     * Retorna todas as faixas contidas nos álbuns deste artista.
     * Este método itera sobre todos os álbuns associados ao artista e coleta todas as faixas de cada álbum.
     * Implementação do método {@code getTracks()} da interface {@link MusicSource}.
     *
     * @return Uma {@code ArrayList} de objetos {@link Track} que representam todas as faixas encontradas nos álbuns do artista.
     * Retorna uma lista vazia se o artista não tiver álbuns ou se os álbuns não contiverem faixas.
     */
    public ArrayList<Track> getTracks() {
        ArrayList<Track> tracks = new ArrayList<Track>();
        for (Album album : albuns) {
            tracks.addAll(album.getTracks());
        }

        return tracks;
    }
}
