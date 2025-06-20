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

import api.Json;
import api.Request;
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
public class Artist implements MusicSource {
    private String name;
    private String id;
    private ArrayList<Album> albums;
    private ArrayList<Track> tracks;
    private Request request;

    /**
     * Construtor para criar uma nova instância de Artist a partir de um ID.
     * Este construtor faz uma requisição à API do Spotify para obter os dados do
     * artista.
     *
     * @param id O ID único do artista no Spotify.
     * @throws RequestException Se ocorrer um erro ao fazer a requisição à API.
     */
    public Artist(String id) throws RequestException {
        this.request = User.getInstance().getRequest();
        Json artistData = this.request.sendGetRequest("artists/" + id);
        this.id = id;
        this.name = artistData.get("name").toString();
        this.albums = new ArrayList<>();
        this.tracks = new ArrayList<>();
    }

    /**
     * Construtor para criar uma nova instância de Artist.
     *
     * @param name   O nome do artista.
     * @param id     O ID único do artista no Spotify.
     * @param albums Uma lista de álbuns associados a este artista.
     */
    public Artist(String name, String id, ArrayList<Album> albums) {
        this.request = User.getInstance().getRequest();
        this.name = name;
        this.id = id;
        this.albums = albums;
        this.tracks = new ArrayList<>();
    }

    public Artist(String name, String id) {
        User user = User.getInstance();
        this.request = user.getRequest();
        this.name = name;
        this.id = id;
        this.albums = new ArrayList<>();
        this.tracks = new ArrayList<>();

    }

    public void addAlbums() {
        try {
            ArrayList<Json> albumsJson = this.request
                    .sendGetRequest("artists/" + this.id + "/albums?market=" + User.getInstance().getCountry())
                    .get("items").parseJsonArray();
            for (Json albumJson : albumsJson) {
                this.albums.add(new Album(
                        albumJson.get("id").toString().replaceAll("\"", "")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para converter o objeto Artist em uma representação de string.
     * 
     * @return Uma string representando o artista, incluindo seu nome, ID e os IDs
     *         dos álbuns associados.
     */
    @Override
    public String toString() {
        return "\nArtist [name=" + name + ", Id=" + id + ", albuns=" + albums + "]";
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
        return id;
    }

    /**
     * Retorna a lista de ids de álbuns associados a este artista.
     *
     * @return Uma {@code ArrayList} de ids de álbuns.
     */
    public ArrayList<Album> getAlbums() {
        if (this.albums.size() == 0) {
            this.addAlbums();
        }
        return albums;
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
    @Override
    public ArrayList<Track> getTracks() {
        if (tracks.size() == 0) {
            for (Album album : getAlbums()) {
                tracks.addAll(album.getTracks());
            }
        }
        return tracks;
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
        example.addAlbums();
        System.out.println(example);
    }
}
