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
 * Implementa a classe abstrata {@link MusicSource} por ser uma fonte de
 * conteúdo
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
                    trackData.get("explicit").parseJson(Boolean.class));
            this.tracks.add(track);
        }
    }

    /**
     * Construtor para criar uma nova instância de Album.
     *
     * @param id     O ID único do álbum no Spotify.
     * @param name   O nome do álbum.
     * @param tracks Uma {@code ArrayList} de {@code Track} que fazem parte
     *               deste álbum.
     */
    public Album(String id, String name, ArrayList<Track> tracks) {
        super(id, name, tracks);
    }

    /**
     * Cria um álbum, obtendo as músicas da API
     * As músicas serão obtidas da API no método addTracks
     * 
     * @param id   o id do álbum a ser adicionado
     * @param name o nome da playlist a ser adicionada
     */
    public Album(String id, String name) {
        super(id, name);
        try {
            this.addTracks();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Método para buscar músicas da API para um álbum já existente
     * Esse método faz uma pesquisa no endpoint do spotify para um álbum específico,
     * obtendo as músicas do endpoint do álbum.
     * 
     * @throws RequestException caso haja algum erro na requisição
     */
    public void addTracks() throws RequestException {
        Json albumData = this.request.sendGetRequest("albums/" + this.id);
        for (Json trackData : albumData.get("tracks.items").parseJsonArray()) {
            Track track = new Track(
                    trackData.get("duration_ms").parseJson(Integer.class),
                    trackData.get("name").toString(),
                    trackData.get("id").toString(),
                    trackData.get("explicit").parseJson(Boolean.class));
            this.tracks.add(track);
            this.tracksIds.add(trackData.get("id").toString().replaceAll("\"", ""));
        }
    }

    /**
     * Retorna a lista de faixas contidas neste álbum.
     * Implementação do método {@code getTracks()} da classe abstrata
     * {@link MusicSource}.
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
}
