/*
* Track.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import api.Json;
import api.Request;
import exceptions.RequestException;
import user.User;

/**
 * Representa uma faixa (música) do Spotify.
 * Contém informações como duração, nome, ID, artistas, álbum e se é explícita.
 * 
 * @author Vinícius de Oliveira - 251527
 * @author Daniel Soares Franco - 259083
 */
public class Track {
    private String id;
    private int duration; // Duração em milissegundos
    private String name;
    private Boolean explicit;
    private Request request;

    /**
     * Construtor para criar uma nova instância de Track a partir de um ID.
     * Este construtor faz uma requisição à API do Spotify para obter os dados da
     * faixa.
     *
     * @param id O ID único da faixa no Spotify.
     * @throws RequestException Se ocorrer um erro ao fazer a requisição à API.
     */
    public Track(String id) throws RequestException {
        this.request = User.getInstance().getRequest();
        Json trackData = this.request.sendGetRequest("tracks/" + id);
        System.out.println(trackData.toString());
        this.id = id;
        this.name = trackData.get("name").toString();
        this.explicit = trackData.get("explicit").parseJson(Boolean.class);
    }

    /**
     * Construtor para criar uma nova instância de Track.
     *
     * @param duration   A duração da faixa em milissegundos.
     * @param name       O nome da faixa.
     * @param id         O ID único da faixa no Spotify.
     * @param artistsIds Uma lista de ids de artistas associados à faixa.
     * @param albumId    O id do álbum ao qual a faixa pertence.
     * @param explicit   Um booleano indicando se a faixa é explícita.
     */
    public Track(int duration, String name, String id, Boolean explicit) {
        this.request = User.getInstance().getRequest();
        this.duration = duration;
        this.name = name;
        this.id = id;
        this.explicit = explicit;
    }

    /**
     * Retorna a duração da faixa em milissegundos.
     *
     * @return A duração da faixa.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Retorna o nome da faixa.
     *
     * @return O nome da faixa.
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna o ID único da faixa no Spotify.
     *
     * @return O ID da faixa.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna um booleano indicando se a faixa é explícita.
     *
     * @return True se a faixa for explícita, false caso contrário.
     */
    public Boolean getExplicit() {
        return explicit;
    }
}
