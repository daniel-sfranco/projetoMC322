/*
* Track.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.util.ArrayList;

import com.fasterxml.jackson.databind.JsonNode;

import api.Json;
import api.Request;
import exceptions.RequestException;

/**
 * Representa uma faixa (música) do Spotify.
 * Contém informações como duração, nome, ID, artistas, álbum e se é explícita.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class Track {
    private int duration; // Duração em milissegundos
    private String name;
    private String id;
    private ArrayList<String> artistsIds;
    private String albumId;
    private Boolean explicit;
    private Request request;

    public Track (String id) throws RequestException {
        this.request = new Request();
        Json trackData = this.request.sendGetRequest("tracks/" + id);
        System.out.println(trackData.toString());
        this.id = id;
        this.name = trackData.get("name").toString();
        
        JsonNode artistsData = (JsonNode) trackData.get("artists");
        ArrayList<String> artistsIds = new ArrayList<String>();
        
        for (JsonNode artistData : artistsData) {
            artistsIds.add(artistData.get("id").toString());
        }
        
        this.artistsIds = artistsIds;
        this.albumId = trackData.get("album.id").toString();

        this.explicit = (Boolean) trackData.get("explicit");
    }

    /**
     * Construtor para criar uma nova instância de Track.
     *
     * @param duration A duração da faixa em milissegundos.
     * @param name O nome da faixa.
     * @param id O ID único da faixa no Spotify.
     * @param artistsIds Uma lista de ids de artistas associados à faixa.
     * @param albumId O id do álbum ao qual a faixa pertence.
     * @param explicit Um booleano indicando se a faixa é explícita.
     */
    public Track(int duration, String name, String id, ArrayList<String> artistsIds, String albumId, Boolean explicit) {
        this.duration = duration;
        this.name = name;
        this.id = id;
        this.artistsIds = artistsIds;
        this.albumId = albumId;
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
     * Retorna a lista de ids de artistas associados à faixa.
     *
     * @return Uma ArrayList de ids de artistas.
     */
    public ArrayList<String> getArtistsIds() {
        return artistsIds;
    }

    /**
     * Retorna o id do álbum ao qual a faixa pertence.
     *
     * @return O id do álbum.
     */
    public String getAlbumId() {
        return albumId;
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
