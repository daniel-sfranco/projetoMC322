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
import fileManager.PlaylistFileManager;
import user.User;

/**
 * Representa uma playlist no Spotify.
 * Uma playlist é uma coleção de faixas (músicas), com informações como o número
 * total de faixas, um ID único, nome, o usuário proprietário e a lista de
 * faixas contidas nela.
 * Implementa a classe abstrata {@link MusicSource} para indicar que é uma
 * fonte de música.
 * 
 * @author Vinícius de Oliveira - 251527
 * @author Daniel Soares Franco - 259083
 */
public class Playlist extends MusicSource {
    private String ownerId;

    /**
     * Construtor para criar uma nova instância de Playlist a partir de um ID.
     * Este construtor faz uma requisição à API do Spotify para obter os dados da
     * playlist.
     *
     * @param id O ID único da playlist no Spotify.
     * @throws RequestException Se ocorrer um erro ao fazer a requisição à API.
     */
    public Playlist(String id) throws RequestException {
        super(id);
        Json playlistData = this.request
                .sendGetRequest("playlists/" + this.id + "?market=" + User.getInstance().getCountry()
                        + "&fields=name%2C+owner.id%2C+tracks.total%2C");
        this.name = playlistData.get("name").toString();
        this.ownerId = playlistData.get("owner.id").toString();
        int numTracks = Integer.parseInt(playlistData.get("tracks.total").toString());

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
            if (this.tracks.size() < numTracks) {
                page = page + 50;
                urlRequest = urlRequest.replace("offset=" + (page - 50), "offset=" + page);
                playlistData = User.getInstance().getRequest()
                        .sendGetRequest(urlRequest);
            }
        } while (this.tracks.size() < numTracks);
    }

    /**
     * Construtor para criar uma nova instância de Playlist.
     *
     * @param id      O ID único da playlist no Spotify.
     * @param name    O nome da playlist.
     * @param ownerId O id do usuário proprietário da playlist.
     * @param tracks  Uma lista de faixas contidas na playlist.
     */
    public Playlist(String id, String name, String ownerId, ArrayList<Track> tracks) {
        super(id, name, tracks);
        this.ownerId = ownerId;
    }

    /**
     * Construtor para construir uma playlist usando o builder
     * Ele define as características da playlist, manda uma requisição criando
     * a mesma no spotify, obtém as músicas definidas pelo builder e
     * adiciona elas na playlist no spotify através das uris
     *
     * @param builder o construtor da playlist, já com as músicas definidas
     * @throws JsonProcessingException caso haja algum erro de processamento de json
     * @throws RequestException        caso haja algum erro de requisição
     */
    private Playlist(PlaylistBuilder builder) throws JsonProcessingException, RequestException {
        this.name = "Sua nova melhor playlist";
        HashMap<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("name", this.name);
        bodyMap.put("description", "Essa playlist foi gerada com o nosso gerador de playlists");
        bodyMap.put("public", true);
        bodyMap.put("collaborative", false);
        Json bodyJson = new Json(bodyMap);
        String url = String.format("users/%s/playlists", User.getInstance().getId());
        Json response = this.request.sendPostRequest(url, bodyJson);
        this.id = response.get("id").toString().replaceAll("\"", "");
        this.ownerId = response.get("owner.id").toString().replaceAll("\"", "");
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
                this.request.sendPostRequest("playlists/" + this.id + "/tracks", bodyJson);
                uris = new ArrayList<>();
                page++;
            }
            uris.add("spotify:track:" + track.getId().toString().replaceAll("\"", ""));
        }
        bodyMap.put("position", page * 100);
        bodyMap.put("uris", uris);
        bodyJson = new Json(bodyMap);
        this.request.sendPostRequest("playlists/" + this.id + "/tracks", bodyJson);
        PlaylistFileManager.addPlaylistId(this.id);
    }

    /**
     * Cria uma nova instância do builder, para gerar uma nova playlist
     * 
     * @param numTracks número de músicas que a nova playlist terá, necessário
     *                  para os cálculos do builder
     * @return um novo builder de playlist para a geração de playlists
     */
    public static PlaylistBuilder builder(int numTracks) {
        return new PlaylistBuilder(numTracks);
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
     * Retorna a lista de faixas contidas na playlist.
     *
     * @return Uma ArrayList de objetos Track.
     */
    @Override
    public ArrayList<Track> getTracks() {
        return tracks;
    }

    /**
     * Retorna representação de uma playlist em string
     * Implementa método abstrato de {@code MusicSource} para representação em
     * string
     *
     * @return representação de uma playlist em string
     */
    public String toString() {
        return "\n    Playlist [id=" + id + ", name=" + name + ", ownerId=" + ownerId
                + ", tracks=" + tracks + "]";
    }

    /**
     * Redireciona para página do spotify de uma playlist especificada no navegador
     * É usado na aba "acessar" do aplicativo, ao clicar em alguma playlist
     * 
     * @param playlistId o id da playlist para a qual haverá o redirecionamento
     */
    public static void editPlaylist(String playlistId) {
        User user = User.getInstance();
        String uri = "https://open.spotify.com/playlist/" + playlistId + "?market=" + user.getCountry();
        try {
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Desktop.getDesktop().browse(new URI(uri));
            } else {
                System.out.println("Não foi possível abrir o navegador. Por favor, acesse a URL manualmente: \n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * PlaylistBuilder
     * Classe construtora para geração de novas playlists no Spotify
     * Usa o design pattern Builder para construção da playlist, com um foco maior
     * na escolha das músicas da playlist, o foco do nosso projeto.
     */
    public static class PlaylistBuilder {
        private int numTracks;
        private int minTracks;
        private String genreId;
        private Playlist basePlaylist;
        private ArrayList<Artist> artists;
        private ArrayList<Album> albums;
        private ArrayList<Track> tracks;
        private ArrayList<String> tracksIds;

        /**
         * Construtor da classe PlaylistBuilder
         * Inicializa os ArrayLists, define o número mínimo inicial e o número total de
         * músicas
         * 
         * @param numTracks o número total de músicas da playlist
         */
        public PlaylistBuilder(int numTracks) {
            this.numTracks = numTracks;
            this.minTracks = 0;
            this.genreId = null;
            this.basePlaylist = null;
            this.artists = new ArrayList<>();
            this.albums = new ArrayList<>();
            this.tracks = new ArrayList<>();
            this.tracksIds = new ArrayList<>();
        }

        /**
         * Define o gênero musical da playlist. O gênero pode ser nulo
         * Aumenta em 1 o número mínimo de músicas da playlist
         *
         * @param genreId o ID do gênero musical escolhido
         * @return o mesmo PlaylistBuilder, para permitir mais adições
         */
        public PlaylistBuilder addGenre(String genreId) {
            this.minTracks++;
            this.genreId = genreId;
            return this;
        }

        /**
         * Define a playlist base da playlist e copia todas as músicas dela.
         * Também aumenta o número mínimo de músicas proporcionalmente ao
         * tamanho da playlist base, para que a nova playlist possa incorporar todas as
         * músicas da playlist base.
         *
         * @param basePlaylistId o ID da playlist base escolhida.
         * @return o mesmo PlaylistBuilder, para permitir mais adições
         * @throws RequestException caso haja algum erro de requisição
         */
        public PlaylistBuilder addPlaylist(String basePlaylistId) throws RequestException {
            Boolean explicit = User.getInstance().getExplicitContentFilter();
            this.basePlaylist = new Playlist(basePlaylistId.replaceAll("\"", ""));
            for (Track track : basePlaylist.getTracks()) {
                if (!this.tracksIds.contains(track.getId()) && track.getExplicit() == explicit) {
                    this.tracks.add(track);
                    this.tracksIds.add(track.getId().toString().replaceAll("\"", ""));
                    this.minTracks++;
                } else {
                    System.out.println(track);
                }
            }
            return this;
        }

        /**
         * Define os artistas a serem incluídos na playlist.
         * Caso sejam escolhidos artistas, serão adicionados a lista de artistas, e
         * serão incluídas músicas de cada artista proporcionalmente ao tamanho da nova
         * playlist, levando-se em consideração as outras categorias a serem passadas.
         * 
         * @param artistId um {@code ArrayList} com os ids dos artistas a serem
         *                 incluídos
         * @return o mesmo PlaylistBuilder, para permitir mais adições
         * @throws RequestException caso haja algum erro de requisição
         */
        public PlaylistBuilder addArtist(ArrayList<String> artistId) throws RequestException {
            this.minTracks += artistId.size();
            for (String id : artistId) {
                this.artists.add(new Artist(id));
            }
            return this;
        }

        /**
         * Define os álbuns a serem incluídos na playlist.
         * Caso sejam escolhidos álbuns, serão adicionados a lista de álbuns, e
         * serão incluídas músicas de cada álbum proporcionalmente ao tamanho da
         * playlist, levando-se em consideração as outras categorias a serem passadas.
         * 
         * @param albumId um {@code ArrayList} com os ids dos artistas a serem
         *                incluídos
         * @return o mesmo PlaylistBuilder, para permitir mais adições
         * @throws RequestException caso haja algum erro de requisição
         */
        public PlaylistBuilder addAlbum(ArrayList<String> albumId) throws RequestException {
            this.minTracks += albumId.size();
            for (String id : albumId) {
                this.albums.add(new Album(id));
            }
            return this;
        }

        /**
         * Adiciona uma lista de músicas automaticamente à playlist
         * Essas músicas são músicas que foram explicitamente especificadas pelo
         * usuário, que devem estar na playlist. O número de músicas mínimas também
         * aumenta conforme o especificado.
         * 
         * @param trackId os ids das músicas a serem adicionadas.
         * @return o mesmo builder, para permitir a adição de outras características
         * @throws RequestException caso haja algum erro de requisição
         */
        public PlaylistBuilder addTrack(ArrayList<String> trackId) throws RequestException {
            this.minTracks += trackId.size();
            for (String id : trackId) {
                this.tracks.add(new Track(id));
                this.tracksIds.add(id);
            }
            return this;
        }

        /**
         * Adiciona as músicas dos álbuns especificados, evitando repetições,
         * Com base no número calculado ideal de músicas por categoria.
         *
         * @param numTracksPerCategory número máximo de músicas de cada álbum
         * @throws RequestException caso haja algum erro de requisição
         */
        private void resolveAlbum(int numTracksPerCategory) throws RequestException {
            Boolean explicit = User.getInstance().getExplicitContentFilter();
            for (Album album : albums) {
                ArrayList<Track> tracks = album.getTracks();
                int counter = 0;
                for (Track track : tracks) {
                    if (counter < numTracksPerCategory && track.getExplicit() == explicit
                            && !this.tracksIds.contains(track.getId())) {
                        this.tracks.add(track);
                        this.tracksIds.add(track.getId());
                        counter++;
                    }
                }
            }
        }

        /**
         * Adiciona as músicas dos artistas especificados, evitando repetições,
         * Com base no número calculado ideal de músicas por categoria.
         *
         * @param numTracksPerCategory número máximo de músicas de cada artista
         * @throws RequestException caso haja algum erro de requisição
         */
        private void resolveArtist(int numTracksPerCategory) throws RequestException {
            Boolean explicit = User.getInstance().getExplicitContentFilter();
            for (Artist artist : artists) {
                ArrayList<Track> tracks = artist.getTracks();
                int counter = 0;
                for (Track track : tracks) {
                    if (counter < numTracksPerCategory && track.getExplicit() == explicit
                            && !this.tracksIds.contains(track.getId())) {
                        this.tracks.add(track);
                        this.tracksIds.add(track.getId());
                        counter++;
                    }
                }
            }
            ;
        }

        /**
         * Adiciona as músicas do gênero musical, se especificado, evitando repetições,
         * com base no número calculado ideal de músicas por categoria.
         *
         * @param numTracksPerCategory número máximo de músicas de cada álbum
         * @throws RequestException caso haja algum erro de requisição
         */
        private void resolveGenre() throws RequestException {
            Boolean explicit = User.getInstance().getExplicitContentFilter();
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
                        } else if (this.tracksIds.contains(trackData.get("id").toString().replaceAll("\"", ""))
                                || trackData.get("explicit").parseJson(Boolean.class) != explicit) {
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

        /**
         * Constrói a playlist, resolvendo as músicas de artistas, álbuns e gênero
         * musical especificados.
         * Primeiro verifica-se se o número de músicas especificado é
         * menor do que o necessário, lançando uma exceção caso isso aconteça.
         * Depois, verifica quantas categorias faltam ser verificadas, retornando a
         * nova playlist caso o valor seja 0.
         * Em seguida, define quantas categorias faltam ser verificadas e o número de
         * músicas por categoria restante, resolvendo as músicas de álbum, e repetindo
         * o processo para artista.
         * As músicas faltantes para completar o número total são definidas pelo gênero
         * musical especificado.
         *
         * @return uma nova playlist, sendo passado o próprio construtor como parâmetro
         * @throws RequestException          caso haja um erro de requisição
         * @throws JsonProcessingException   caso haja um problema de processamento de
         *                                   json
         * @throws InvalidNumTracksException caso o número mínimo de músicas necessárias
         *                                   seja maior
         *                                   que o número de músicas da playlist passado
         *                                   pelo usuário
         */
        public Playlist build() throws RequestException, JsonProcessingException, InvalidNumTracksException {
            if (this.numTracks < this.minTracks) {
                throw new InvalidNumTracksException(this.minTracks);
            }
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
}
