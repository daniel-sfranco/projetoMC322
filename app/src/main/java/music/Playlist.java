/*
* Playlist.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.awt.Desktop;
import java.util.ArrayList;
import java.util.HashMap;
import java.net.URI;

import com.fasterxml.jackson.core.JsonProcessingException;

import api.HttpClientUtil;
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
        this.id = id.toString().replaceAll("\"", "");
        Json playlistData = User.getInstance().getRequest()
                .sendGetRequest("playlists/" + id + "?market=" + User.getInstance().getCountry()
                        + "&fields=name%2C+owner.id%2C+tracks.total%2C");
        this.name = playlistData.get("name").toString();
        this.ownerId = playlistData.get("owner.id").toString();
        this.numTracks = Integer.parseInt(playlistData.get("tracks.total").toString());
        this.tracks = new ArrayList<>();
        this.tracksIds = new ArrayList<>();

        String urlRequest = "playlists/" + id + "/tracks?market=" + User.getInstance().getCountry()
                + "&fields=items.track%28duration_ms%2C+name%2C+id%2C+explicit%29&limit=50&offset=0";
        int page = 0;
        playlistData = User.getInstance().getRequest()
                .sendGetRequest(urlRequest);
        do {
            for (Json trackObject : playlistData.get("items").parseJsonArray()) {
                Json trackData = trackObject.get("track");
                if (trackData == null || trackData.toString().equals("null")) {
                    continue;
                }
                Track track = new Track(
                        trackData.get("duration_ms").parseJson(Integer.class),
                        trackData.get("name").toString(),
                        trackData.get("id").toString().replaceAll("\"", ""),
                        trackData.get("explicit").parseJson(Boolean.class));
                this.tracks.add(track);
                this.tracksIds.add(trackData.get("id").toString().replaceAll("\"", ""));

            }
            if (this.tracks.size() < this.numTracks) {
                page = page + 50;
                urlRequest = urlRequest.replace("offset=" + (page - 50), "offset=" + page);
                playlistData = User.getInstance().getRequest()
                        .sendGetRequest(urlRequest);
            }
        } while (this.tracks.size() < this.numTracks);
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
        this.tracksIds = new ArrayList<>();
        for (Track track : tracks) {
            tracksIds.add(track.getId());
        }
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
        this.tracks = new ArrayList<>(builder.tracks);
        this.tracksIds = new ArrayList<>(builder.tracksIds);
        ArrayList<String> uris = new ArrayList<>();
        bodyMap = new HashMap<>();
        int page = 0;
        for (Track track : this.tracks) {
            if (uris.size() == 100) {
                bodyMap.put("position", page * 100);
                bodyMap.put("uris", uris);
                bodyJson = new Json(bodyMap);
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

    public static void editPlaylist(String playlistId) {
        String uri = "https://open.spotify.com/playlist/" + playlistId;
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(uri));
            } else {
                System.out.println(
                        "Não foi possível abrir o navegador. Por favor, acesse a URL manualmente: \n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class PlaylistBuilder {
        private int numTracks;
        private int minTracks;
        private String genreId;
        private Playlist basePlaylist;
        private ArrayList<Artist> artists;
        private ArrayList<Album> albums;
        private ArrayList<Track> addedTracks;
        private ArrayList<Track> tracks;
        private ArrayList<String> tracksIds;

        public PlaylistBuilder(int numTracks) {
            this.numTracks = numTracks;
            this.minTracks = 0;
            this.genreId = null;
            this.basePlaylist = null;
            this.artists = new ArrayList<>();
            this.albums = new ArrayList<>();
            this.addedTracks = new ArrayList<>();
            this.tracks = new ArrayList<>();
            this.tracksIds = new ArrayList<>();
        }

        public PlaylistBuilder addGenre(String genreId) {
            this.minTracks++;
            this.genreId = genreId;
            return this;
        }

        public PlaylistBuilder addPlaylist(String basePlaylistId) throws RequestException {
            Playlist actPlaylist = new Playlist(basePlaylistId.replaceAll("\"", ""));
            this.minTracks += actPlaylist.getNumTracks();
            this.basePlaylist = new Playlist(basePlaylistId);
            return this;
        }

        public PlaylistBuilder addArtist(ArrayList<String> artistId) throws RequestException {
            this.minTracks += artistId.size();
            for (String id : artistId) {
                this.artists.add(new Artist(id));
            }
            return this;
        }

        public PlaylistBuilder addAlbum(ArrayList<String> albumId) throws RequestException {
            this.minTracks += albumId.size();
            for (String id : albumId) {
                this.albums.add(new Album(id));
            }
            return this;
        }

        public PlaylistBuilder addTrack(ArrayList<String> trackId) {
            this.minTracks += trackId.size();
            for (String id : trackId) {
                this.addedTracks.add(new Track(id));
                this.tracksIds.add(id);
            }
            return this;
        }

        private void resolvePlaylist() throws RequestException {
            if (basePlaylist != null) {
                for (Track track : basePlaylist.getTracks()) {
                    if (!this.tracksIds.contains(track.getId())) {
                        this.tracks.add(track);
                        this.tracksIds.add(track.getId().toString().replaceAll("\"", ""));
                    } else {
                        System.out.println(track);
                    }
                }
            }
        }

        private void resolveTrack() {
            if (addedTracks != null) {
                for (Track track : addedTracks) {
                    tracks.add(track);
                    tracksIds.add(track.getId());
                }
            }
        }

        private void resolveAlbum(int numTracksPerCategory) throws RequestException {
            ArrayList<Track> tracks;
            int counter;
            for (Album album : albums) {
                tracks = album.getTracks();
                counter = 0;
                for (Track track : tracks) {
                    if (counter < numTracksPerCategory && !this.tracksIds.contains(track.getId())) {
                        this.tracks.add(track);
                        this.tracksIds.add(track.getId());
                        counter++;
                    }
                }
            }
        }

        private void resolveArtist(int numTracksPerCategory) throws RequestException {
            ArrayList<Track> tracks;
            for (Artist artist : artists) {
                tracks = artist.getTracks();
                int counter = 0;
                for (Track track : tracks) {
                    if (counter < numTracksPerCategory && !this.tracksIds.contains(track.getId())) {
                        this.tracks.add(track);
                        this.tracksIds.add(track.getId());
                        counter++;
                    }
                }
            }
            ;
        }

        private void resolveGenre() throws RequestException {
            if (genreId != null) {
                String idEncoded = HttpClientUtil.QueryURLEncode("genre:" + genreId);
                String urlRequest = "search?q=" + idEncoded
                        + "&type=track&market=" + User.getInstance().getCountry() + "&limit=50&offset=0";
                int page = 0;
                Json genreTracks = User.getInstance().getRequest()
                        .sendGetRequest(urlRequest);
                do {
                    for (Json trackData : genreTracks.get("tracks.items").parseJsonArray()) {
                        if (trackData == null || trackData.toString().equals("null")) {
                            continue;
                        } else if (this.tracksIds.contains(trackData.get("id").toString().replaceAll("\"", ""))) {
                            continue;
                        }
                        Track track = new Track(
                                trackData.get("duration_ms").parseJson(Integer.class),
                                trackData.get("name").toString(),
                                trackData.get("id").toString().replaceAll("\"", ""),
                                trackData.get("explicit").parseJson(Boolean.class));
                        this.tracks.add(track);
                        this.tracksIds.add(trackData.get("id").toString().replaceAll("\"", ""));
                        if (tracks.size() == numTracks) {
                            break;
                        }
                    }
                    if (tracks.size() < numTracks) {
                        page += 50;
                        urlRequest = urlRequest.replace("offset=" + (page - 50), "offset=" + page);
                        genreTracks = User.getInstance().getRequest()
                                .sendGetRequest(urlRequest);
                    }
                } while (tracks.size() < numTracks);
            }
        }

        public Playlist build() throws RequestException, JsonProcessingException, InvalidNumTracksException {
            if (this.numTracks < this.minTracks) {
                throw new InvalidNumTracksException(this.minTracks);
            }
            resolvePlaylist();
            resolveTrack();
            int numCategoriesLeft = albums.size() + artists.size();
            if (genreId != null) {
                numCategoriesLeft = numCategoriesLeft + 1;
            }
            if (numCategoriesLeft == 0) {
                return new Playlist(this);
            }
            int leftTracks = numTracks - tracks.size();
            int numTracksPerCategory = leftTracks / numCategoriesLeft;
            resolveAlbum(numTracksPerCategory);
            numCategoriesLeft = numCategoriesLeft - albums.size();
            leftTracks = numTracks - tracks.size();
            numTracksPerCategory = leftTracks / numCategoriesLeft;
            resolveArtist(numTracksPerCategory);
            resolveGenre();
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
        ArrayList<String> artists = new ArrayList<>();
        ArrayList<String> albums = new ArrayList<>();
        tracks.add("0Mp5NW8pLD0EjL8JBM0FGD"); // Se foi - Marco Telles
        tracks.add("2obR9E84AXLzWv0gFbTu37"); // Lá(r) - Marco Telles
        artists.add("2fBxIdkeMcxcjtBlPuWZl7"); // Pedro Valença
        artists.add("1Ja8qReIBoi7Z6ik0AQ6zS"); // Os Arrais
        albums.add("2wHwSfo4COWRXocywNnlWN"); // Estima - Stenio Marcius
        Playlist.PlaylistBuilder builder = Playlist.builder(100);
        builder = builder.addTrack(tracks);
        builder = builder.addPlaylist("7H4HBQGgj3ZvvdITHJHbhi"); // Minha playlist de música gospel, não vai funcionar
                                                                 // para
                                                                 // outros usuários
        builder = builder.addGenre("Worship");
        builder = builder.addArtist(artists);
        builder = builder.addAlbum(albums);
        Playlist newPlaylist = builder.build();
        System.out.println(newPlaylist);
    }
}
