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

import api.Json;
import exceptions.RequestException;

/**
 * Representa um álbum de música no Spotify.
 * Um álbum contém um número de faixas, um ID único, um nome,
 * os artistas associados e a lista de faixas que compõem o álbum.
 * Implementa a interface {@link MusicSource} por ser uma fonte de conteúdo
 * musical.
 * 
 * @author Vinícius de Oliveira - 251527
 * @author Daniel Soares Franco - 259083
 */
public class Album extends MusicSource {
    /**
     * Construtor para criar uma nova instância de Album a partir de um ID.
     * Este construtor faz uma requisição à API do Spotify para obter os dados do
     * álbum.
     *
     * @param id O ID único do álbum no Spotify.
     * @throws RequestException Se ocorrer um erro ao fazer a requisição à API.
     */
    public Album(String id) throws RequestException {
        super(id);
        Json albumData = this.request.sendGetRequest("albums/" + this.id);
        this.name = albumData.get("name").toString();

        for (Json trackData : albumData.get("tracks.items").parseJsonArray()) {
            Track track = new Track(
                trackData.get("duration_ms").parseJson(Integer.class),
                trackData.get("name").toString(),
                trackData.get("id").toString(),
                trackData.get("explicit").parseJson(Boolean.class)
            );
            this.tracks.add(track);
        }
    }

    /**
     * Construtor para criar uma nova instância de Album.
     *
     * @param numTracks  O número total de faixas presentes no álbum.
     * @param id         O ID único do álbum no Spotify.
     * @param name       O nome do álbum.
     * @param tracks  Uma {@code ArrayList} de ids de músicas que fazem parte
     *                   deste álbum.
     */
    public Album(String id, String name, ArrayList<Track> tracks) {
        super(id, name, tracks);
    }

    public Album(String id, String name){
        super(id, name);
    }

    /**
     * Retorna a lista de faixas contidas neste álbum.
     *
     * @return Uma {@code ArrayList} de objetos {@link Track}.
     */
    public ArrayList<Track> getTracks() {
        return tracks;
    }

    /**
     * Retorna uma representação em string do objeto Album.
     *
     * @return Uma string representando o álbum.
     */
    public String toString() {
        return "\n    Album [id=" + id + ", name=" + name + ", tracks=" + tracks + "]";
    }

    public static void main(String[] args) throws RequestException {
        Album album = new Album("5qRQ53QZuqnpjDedQmfsLJ");
        System.out.println(album);
    }
}
