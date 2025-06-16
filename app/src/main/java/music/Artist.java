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
    private String Id;
    private ArrayList<String> albunsIds;
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
        this.request = new Request();
        Json artistData = this.request.sendGetRequest("artists/" + id);
        System.out.println(artistData.toString());
        this.Id = id;
        this.name = artistData.get("name").toString();

        Json artistAlbuns = this.request.sendGetRequest("artists/" + id + "albuns");
        System.out.println(artistAlbuns.toString());
    }

    /**
     * Construtor para criar uma nova instância de Artist.
     *
     * @param name     O nome do artista.
     * @param Id       O ID único do artista no Spotify.
     * @param albunsIds Uma lista de ids de álbuns associados a este artista.
     */
    public Artist(String name, String Id, ArrayList<String> albunsIds) {
        this.name = name;
        this.Id = Id;
        this.albunsIds = albunsIds;
        this.request = null;
    }

    /**
     * Método para converter o objeto Artist em uma representação de string.
     * 
     * @return Uma string representando o artista, incluindo seu nome, ID e os IDs
     *         dos álbuns associados.
     */
    @Override
    public String toString() {
        return "Artist [name=" + name + ", Id=" + Id + ", albuns=" + albunsIds + "]";
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
     * Retorna a lista de ids de álbuns associados a este artista.
     *
     * @return Uma {@code ArrayList} de ids de álbuns.
     */
    public ArrayList<String> getAlbunsIds() {
        return albunsIds;
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
    public ArrayList<String> getTracksIds() {
        ArrayList<String> tracksIds = new ArrayList<String>();

        for (String albumId : albunsIds) {
            try {
                Album currentAlbum = new Album(albumId);
                tracksIds.addAll(currentAlbum.getTracksIds());
            } catch (RequestException e) {
                System.out.println("Eu ao adicionar faixas do álbum com id " + albumId);
                System.out.println(e.getMessage());
            }
        }

        return tracksIds;
    }

    /**
     * Método principal para testar a classe Artist.
     * Tenta criar um artista com um ID específico e imprime suas informações.
     *
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        try {
            System.out.println(new Artist("43ZHCT0cAZBISjO8DG9PnE"));
        } catch (RequestException e) {
            System.out.println("Erro na requisição do artista");
            System.out.println("Mensagem: " + e.getMessage());
        }
    }
}
