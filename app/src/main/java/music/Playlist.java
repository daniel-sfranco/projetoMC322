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
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import api.Json;
import exceptions.RequestException;
import user.User;

/**
 * Representa uma playlist no Spotify.
 * Uma playlist é uma coleção de faixas (músicas), com informações como o número
 * total de faixas,
 * um ID único, nome, o usuário proprietário e a lista de faixas contidas nela.
 * Implementa a interface {@link MusicSource} para indicar que pode ser uma
 * fonte de música.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class Playlist implements MusicSource {
    private int numTracks;
    private String id;
    private String name;
    private String ownerId;
    private ArrayList<String> tracksIds;

    /**
     * Construtor para criar uma nova instância de Playlist a partir de um ID.
     * Este construtor faz uma requisição à API do Spotify para obter os dados da
     * playlist.
     *
     * @param id O ID único da playlist no Spotify.
     * @throws RequestException Se ocorrer um erro ao fazer a requisição à API.
     */
    public Playlist(String id) throws RequestException {
        this.id = id;
        Json playlistData = User.getInstance().getRequest()
                .sendGetRequest("playlists/" + id);
        this.name = playlistData.get("name").toString();
        this.ownerId = playlistData.get("owner.id").toString();
        this.numTracks = Integer.parseInt(playlistData.get("tracks.total").toString());
        this.tracksIds = new ArrayList<>();

        ArrayList<HashMap<String, Object>> tracksData = playlistData.get("tracks.items").parseJson(new TypeReference<ArrayList<HashMap<String, Object>>>() {
        });
        ArrayList<Json> tracks = playlistData.get("tracks.items").parseJsonArray();
        for (Json trackData : tracks) {
            String trackId = trackData.get("track.id").toString();
            if (trackId != null) {
                this.tracksIds.add(trackId);
            }
        }
    }

    /**
     * Construtor para criar uma nova instância de Playlist.
     *
     * @param numTracks O número total de faixas na playlist.
     * @param id        O ID único da playlist no Spotify.
     * @param name      O nome da playlist.
     * @param ownerId     O id do usuário proprietário da playlist.
     * @param tracksIds Uma lista de ids de faixas contidas na playlist.
     */
    public Playlist(int numTracks, String id, String name, String ownerId, ArrayList<String> tracksIds) {
        this.numTracks = numTracks;
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.tracksIds = tracksIds;
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
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * Retorna a lista de ids de faixas contidas na playlist.
     *
     * @return Uma ArrayList de objetos Track.
     */
    public ArrayList<String> getTracksIds() {
        return tracksIds;
    }

    public static void main(String[] args) throws RequestException {
        String playlistId = "29RMt61ETYJG3k6okGJdi2";
        Playlist rock = new Playlist(playlistId);
        System.out.println(rock.getName());
        System.out.println(rock.getNumTracks());
        System.out.println(rock.getTracksIds());
    }
}
