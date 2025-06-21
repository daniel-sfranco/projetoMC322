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
import api.Request;
import exceptions.RequestException;
import user.User;

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
public class Album implements MusicSource {
    private int numTracks;
    private String id;
    private String name;
    private ArrayList<Track> tracks;
    private Request request;

    /**
     * Construtor para criar uma nova instância de Album a partir de um ID.
     * Este construtor faz uma requisição à API do Spotify para obter os dados do
     * álbum.
     *
     * @param id O ID único do álbum no Spotify.
     * @throws RequestException Se ocorrer um erro ao fazer a requisição à API.
     */
    public Album(String id) throws RequestException {
        this.request = User.getInstance().getRequest();
        this.id = id.replaceAll("\"", "");
        Json albumData = this.request.sendGetRequest("albums/" + this.id);
        this.numTracks = albumData.get("total_tracks").parseJson(Integer.class);
        this.name = albumData.get("name").toString();
        this.tracks = new ArrayList<>();
        ArrayList<Json> tracksData = albumData.get("tracks.items").parseJsonArray();
        ArrayList<Track> tracks = new ArrayList<Track>();

        for (Json trackData : tracksData) {
            Track track = new Track(
                trackData.get("duration_ms").parseJson(Integer.class),
                trackData.get("name").toString(),
                trackData.get("id").toString(),
                trackData.get("explicit").parseJson(Boolean.class)
            );
            tracks.add(track);
        }

        this.tracks = tracks;
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
    public Album(int numTracks, String id, String name, ArrayList<Track> tracks) {
        this.request = User.getInstance().getRequest();
        this.numTracks = numTracks;
        this.id = id.replaceAll("\"", "");
        this.name = name;
        this.tracks = tracks;
    }

    public Album(int numTracks, String id, String name){
        this.request = User.getInstance().getRequest();
        this.numTracks = numTracks;
        this.id = id.replaceAll("\"", "");
        this.name = name;
        this.tracks = new ArrayList<>();
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
     * Retorna a lista de faixas contidas neste álbum.
     *
     * @return Uma {@code ArrayList} de objetos {@link Track}.
     */
    @Override
    public ArrayList<Track> getTracks() {
        return tracks;
    }

    /**
     * Retorna uma representação em string do objeto Album.
     *
     * @return Uma string representando o álbum.
     */
    public String toString() {
        return "\n    Album [numTracks=" + numTracks + ", id=" + id + ", name=" + name + ", tracks=" + tracks + "]";
    }

    public static void main(String[] args) throws RequestException {
        Album album = new Album("5qRQ53QZuqnpjDedQmfsLJ");
        System.out.println(album);
    }
}
