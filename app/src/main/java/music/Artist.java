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

import api.HttpClientUtil;
import api.Json;
import exceptions.RequestException;
import user.User;

/**
 * Representa um artista no Spotify.
 * Um artista possui um nome, um ID único e uma lista de álbuns associados.
 * Implementa a interface {@link MusicSource} para indicar que pode ser uma
 * fonte de música.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class Artist extends MusicSource {
    /**
     * Construtor para criar uma nova instância de Artist a partir de um ID.
     * Este construtor faz uma requisição à API do Spotify para obter os dados do
     * artista.
     *
     * @param id O ID único do artista no Spotify.
     * @throws RequestException Se ocorrer um erro ao fazer a requisição à API.
     */
    public Artist(String id) throws RequestException {
        super(id);
        Json artistData = this.request.sendGetRequest("artists/" + this.id);
        this.name = artistData.get("name").toString();
    }

    /**
     * Construtor para criar uma nova instância de Artist.
     *
     * @param name   O nome do artista.
     * @param id     O ID único do artista no Spotify.
     * @param albums Uma lista de álbuns associados a este artista.
     */
    public Artist(String name, String id) {
        super(id, name);
    }

    /**
     * Retorna uma lista de ids de faixas associadas a este artista.
     * Implementação do método {@code getTracksIds()} da interface
     * {@link MusicSource}.
     * 
     * Este método itera sobre os álbuns do artista e coleta os ids das faixas de
     * cada álbum.
     *
     * @return Uma {@code ArrayList} de ids de faixas.
     */
    public ArrayList<Track> getTracks() {
        if (tracks.size() == 0) {
            String query = HttpClientUtil.QueryURLEncode("artist:" + this.name);
            String urlRequest = ("search?q=" + query + "&type=track&offset=0&limit=50&market="
                    + User.getInstance().getCountry());
            int page = 0;
            try {
                Json artistTracks = User.getInstance().getRequest().sendGetRequest(urlRequest);
                ArrayList<Json> tracksObjects = artistTracks.get("tracks.items").parseJsonArray();
                do {
                    for (Json trackData : tracksObjects) {
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
                    }
                    if (tracksObjects.size() == 50) {
                        page += 50;
                        urlRequest = urlRequest.replace("offset=" + (page - 50), "offset=" + page);
                        artistTracks = User.getInstance().getRequest()
                                .sendGetRequest(urlRequest);
                        tracksObjects = artistTracks.get("tracks.items").parseJsonArray();
                    }
                } while (tracksObjects.size() == 50);
            } catch (RequestException e) {
                e.printStackTrace();
            }
        }
        return tracks;
    }

    /**
     * Método para converter o objeto Artist em uma representação de string.
     * 
     * @return Uma string representando o artista, incluindo seu nome, ID e os IDs
     *         dos álbuns associados.
     */
    public String toString() {
        return "\nArtist [name=" + name + ", Id=" + id + ", Tracks=" + this.getTracks() + "]";
    }

    /**
     * Método principal para testar a classe Artist.
     * Tenta criar um artista com um ID específico e imprime suas informações.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) throws RequestException {
        Json followed = User.getInstance().getRequest().sendGetRequest("me/following?type=artist");
        ArrayList<Json> artistList = followed.get("artists.items").parseJsonArray();
        ArrayList<Artist> artists = new ArrayList<>();
        for (Json artist : artistList) {
            Artist newArtist = new Artist(
                    artist.get("name").toString().replaceAll("\"", ""),
                    artist.get("id").toString().replaceAll("\"", ""));
            artists.add(newArtist);
            System.out.print(newArtist);
        }
        Artist example = artists.get(0);
        example.getTracks();
        System.out.println(example);
    }
}
