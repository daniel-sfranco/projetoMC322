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
 * @author Daniel Soares Franco - 259083
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
     * @param name O nome do artista.
     * @param id   O ID único do artista no Spotify.
     */
    public Artist(String name, String id) {
        super(id, name);
    }

    /**
     * Construtor para criar uma nova instância de Artist, já adicionando as músicas
     * 
     * @param name   O nome do artista
     * @param id     O ID único do artista no Spotify
     * @param tracks A lista de músicas associadas a esse artista
     */
    public Artist(String name, String id, ArrayList<Track> tracks) {
        super(id, name, tracks);
    }

    /**
     * Retorna uma lista de ids de faixas associadas a este artista.
     * Implementação do método {@code getTracks()} da classe abstrata
     * {@link MusicSource}.
     * 
     * Esse método faz uma pesquisa no endpoint do spotify para buscas, filtrando
     * músicas pelo artista selecionado
     *
     * @return Uma {@code ArrayList} de {@code Track}
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
}
