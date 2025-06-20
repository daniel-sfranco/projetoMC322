/*
* Playlist.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;

import api.Json;
import exceptions.InvalidNumTracksException;
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
 * @author Daniel Soares Franco - 259083
 */
public class Playlist implements MusicSource {
    private int numTracks;
    private String id;
    private String name;
    private String ownerId;
    private ArrayList<Track> tracks;

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
        this.tracks = new ArrayList<>();

        for (Json trackObject : playlistData.get("tracks.items").parseJsonArray()) {
            Json trackData = trackObject.get("track");
            if (trackData == null || trackData.toString().equals("null")) {
                continue;
            }
            Track track = new Track(
                    trackData.get("duration_ms").parseJson(Integer.class),
                    trackData.get("name").toString(),
                    trackData.get("id").toString(),
                    trackData.get("explicit").parseJson(Boolean.class));
            this.tracks.add(track);

        }
    }

    /**
     * Construtor para criar uma nova instância de Playlist.
     *
     * @param numTracks O número total de faixas na playlist.
     * @param id        O ID único da playlist no Spotify.
     * @param name      O nome da playlist.
     * @param ownerId   O id do usuário proprietário da playlist.
     * @param tracksIds Uma lista de ids de faixas contidas na playlist.
     */
    public Playlist(int numTracks, String id, String name, String ownerId, ArrayList<Track> tracks) {
        this.numTracks = numTracks;
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.tracks = tracks;
    }

    public Playlist(int numTracks, String id, String name, String ownerId) {
        this.numTracks = numTracks;
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.tracks = new ArrayList<>();
    }

    private Playlist(PlaylistBuilder builder) throws JsonProcessingException, RequestException {
        User user = User.getInstance();
        this.name = "Sua nova melhor playlist";
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("name", this.name);
        bodyMap.put("description", "Essa playlist foi gerada com o nosso gerador de playlists");
        bodyMap.put("public", true);
        bodyMap.put("collaborative", false);
        Json bodyJson = new Json(bodyMap);
        String url = String.format("users/%s/playlists", user.getId());
        Json response = user.getRequest().sendPostRequest(url, bodyJson);
        this.id = response.get("id").toString().replaceAll("\"", "");
        this.ownerId = response.get("owner.id").toString().replaceAll("\"", "");
        this.numTracks = builder.numTracks;
        this.tracks = new ArrayList<>(builder.tracks); // a api não retorna todas as músicas de uma vez, tenho que fazer ela retornar todas as músicas
        ArrayList<String> uris = new ArrayList<>();
        bodyMap = new HashMap<>();
        int page = 0;
        for (Track track : this.tracks) {
            if (uris.size() == 100) {
                bodyMap.put("position", page * 100);
                bodyMap.put("uris", uris);
                bodyJson = new Json(bodyMap);
                System.out.println(track.getId());
                user.getRequest().sendPostRequest("playlists/" + this.id + "/tracks", bodyJson);
                uris = new ArrayList<>();
                page++;
            }
            uris.add("spotify:track:" + track.getId().toString().replaceAll("\"", ""));
        }
        bodyMap.put("position", page * 100);
        bodyMap.put("uris", uris);
        bodyJson = new Json(bodyMap);
        user.getRequest().sendPostRequest("playlists/" + this.id + "/tracks", bodyJson);
    }

    public static PlaylistBuilder builder(int numTracks) {
        return new PlaylistBuilder(numTracks);
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
    public ArrayList<Track> getTracks() {
        return tracks;
    }

    @Override
    public String toString() {
        return "\n    Playlist [numTracks=" + numTracks + ", id=" + id + ", name=" + name + ", ownerId=" + ownerId
                + ", tracks=" + tracks + "]";
    }

    public static class PlaylistBuilder {
        private int numTracks;
        private int minTracks;
        private String genreId;
        private String basePlaylistId;
        private ArrayList<String> artistId;
        private ArrayList<String> albumId;
        private ArrayList<String> trackId;
        private ArrayList<Track> tracks;

        public PlaylistBuilder(int numTracks) {
            this.numTracks = numTracks;
            this.minTracks = 0;
            this.genreId = null;
            this.basePlaylistId = null;
            this.artistId = null;
            this.albumId = null;
            this.trackId = null;
            this.tracks = new ArrayList<>();
        }

        public PlaylistBuilder addGenre(String genreId) {
            this.minTracks++;
            this.genreId = genreId;
            return this;
        }

        public PlaylistBuilder addPlaylist(String basePlaylistId) throws RequestException {
            Playlist actPlaylist = new Playlist(basePlaylistId);
            this.minTracks += actPlaylist.getNumTracks();
            this.basePlaylistId = basePlaylistId;
            return this;
        }

        public PlaylistBuilder addArtist(ArrayList<String> artistId) {
            this.minTracks += artistId.size();
            this.artistId = artistId;
            return this;
        }

        public PlaylistBuilder addAlbum(ArrayList<String> albumId) {
            this.minTracks += albumId.size();
            this.albumId = albumId;
            return this;
        }

        public PlaylistBuilder addTrack(ArrayList<String> trackId) {
            this.minTracks += trackId.size();
            this.trackId = trackId;
            return this;
        }

        public Playlist build() throws RequestException, JsonProcessingException, InvalidNumTracksException {
            if (this.numTracks < this.minTracks) {
                throw new InvalidNumTracksException(this.minTracks);
            }
            if (trackId != null) {
                for (String track : trackId) {
                    tracks.add(new Track(track));
                }
            }
            if (basePlaylistId != null) {
                Playlist actPlaylist = new Playlist(basePlaylistId);
                tracks.addAll(actPlaylist.getTracks());
            }
            int actTracksSize = tracks.size();
            int numCategoriesLeft = minTracks - actTracksSize;
            return new Playlist(this);
        }
    }

    public static void main(String[] args) throws RequestException, JsonProcessingException, InvalidNumTracksException {
        // testando a busca de uma playlist diretamente com a id dela
        // String playlistId = "29RMt61ETYJG3k6okGJdi2";
        // Playlist rock = new Playlist(playlistId);
        // System.out.println(rock);

        // testando a criação de uma playlist com o builder
        ArrayList<String> tracks = new ArrayList<>();
        tracks.add("3PYdxIDuBIuJSDGwfptFx4");
        Playlist newPlaylist = Playlist.builder(225).addTrack(tracks).addPlaylist("29RMt61ETYJG3k6okGJdi2").build();
        System.out.println(newPlaylist);
    }
}
