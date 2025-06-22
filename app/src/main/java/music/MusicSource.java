/*
* MusicSource.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.util.ArrayList;

import api.Request;
import user.User;

/**
 * A interface {@code MusicSource} define um contrato para qualquer entidade que
 * possa ser considerada uma fonte de música no sistema.
 * Isso inclui classes como {@code Artist}, {@code Album}, {@code Playlist} e
 * {@code Category} garantindo que todas as fontes de música
 * possuam um ID único e um nome para identificação.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public abstract class MusicSource {
    protected String id;
    protected String name;
    protected ArrayList<Track> tracks;
    protected ArrayList<String> tracksIds;
    protected Request request;

    public MusicSource(){
        this.request = User.getInstance().getRequest();
        this.tracks = new ArrayList<>();
        this.tracksIds = new ArrayList<>();
    }

    public MusicSource(String id){
        this.id = id.replaceAll("\"", "");
        this.tracks = new ArrayList<>();
        this.tracksIds = new ArrayList<>();
        this.request = User.getInstance().getRequest();
    }

    public MusicSource(String id, String name){
        this.id = id.replaceAll("\"", "");
        this.name = name;
        this.tracks = new ArrayList<>();
        this.tracksIds = new ArrayList<>();
        this.request = User.getInstance().getRequest();
    }

    public MusicSource(String id, String name, ArrayList<Track> tracks){
        this.id = id.replaceAll("\"", "");
        this.name = name;
        this.tracks = tracks;
        this.tracksIds = new ArrayList<>();
        for(Track track : tracks){
            this.tracksIds.add(track.getId());
        }
    }

    public abstract ArrayList<Track> getTracks();

    /**
     * Retorna o ID único da fonte de música.
     * Este ID é utilizado para identificar a fonte de forma exclusiva dentro do
     * sistema ou da API integrada (e.g., Spotify).
     *
     * @return Uma {@code String} contendo o ID da fonte de música.
     */
    public String getId(){
        return this.id;
    }

    /**
     * Retorna o nome legível da fonte de música.
     * Este nome é tipicamente exibido ao usuário e descreve a fonte de música
     * (e.g., o nome de uma playlist).
     *
     * @return Uma {@code String} contendo o nome da fonte de música.
     */
    public String getName(){
        return this.name;
    }

    public abstract String toString();
}
