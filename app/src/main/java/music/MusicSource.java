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
 * A classe {@code MusicSource} define um contrato para qualquer entidade que
 * possa ser considerada uma fonte de música no sistema.
 * Isso inclui classes como {@code Artist}, {@code Album} e {@code Playlist},
 * garantindo que todas as fontes de música possuam um ID único e um nome para
 * identificação.
 * 
 * @author Vinícius de Oliveira - 251527
 * @author Daniel Soares Franco - 259083
 */
public abstract class MusicSource {
    protected String id;
    protected String name;
    protected ArrayList<Track> tracks;
    protected ArrayList<String> tracksIds;
    protected Request request;

    /**
     * Construtor padrão de MusicSource, sem incluir informações inicialmente
     * É usado na criação da playlist usando o builder
     */
    public MusicSource() {
        this.request = User.getInstance().getRequest();
        this.tracks = new ArrayList<>();
        this.tracksIds = new ArrayList<>();
    }

    /**
     * Construtor de MusicSource para um objeto que já contém uma id
     * Atribui o valor de id para o atributo Id, busca o objeto request de User e
     * inicializa os ArrayLists
     * 
     * @param id o id do MusicSource a ser adicionado
     */
    public MusicSource(String id) {
        this.id = id.replaceAll("\"", "");
        this.tracks = new ArrayList<>();
        this.tracksIds = new ArrayList<>();
        this.request = User.getInstance().getRequest();
    }

    /**
     * Construtor de MusicSource caso haja um valor definido para id e name
     * Atribui id e name, obtém a request do User e inicializa os ArrayLists
     * 
     * @param id   o id do MusicSource a ser criado
     * @param name o nome do MusicSource a ser criado
     */
    public MusicSource(String id, String name) {
        this.id = id.replaceAll("\"", "");
        this.name = name;
        this.tracks = new ArrayList<>();
        this.tracksIds = new ArrayList<>();
        this.request = User.getInstance().getRequest();
    }

    /**
     * Construtor para um MusicSource que já tem id, nome e músicas definidos
     * Atribui id, nome e tracks e define o ArrayList de tracksIds
     *
     * @param id     o id do MusicSource a ser adicionado
     * @param name   o nome do MusicSource a ser adicionado
     * @param tracks as músicas do MusicSource a serem adicionadas
     */
    public MusicSource(String id, String name, ArrayList<Track> tracks) {
        this.request = User.getInstance().getRequest();
        this.id = id.replaceAll("\"", "");
        this.name = name;
        this.tracks = tracks;
        this.tracksIds = new ArrayList<>();
        for (Track track : tracks) {
            this.tracksIds.add(track.getId());
        }
    }

    /**
     * Retorna os ids das músicas que estão no MusicSource
     *
     * @return os ids das músicas que estão no MusicSource
     */
    public ArrayList<String> getTracksIds() {
        return tracksIds;
    }

    /**
     * Retorna a request associada ao MusicSource
     * 
     * @return a request associada ao MusicSource
     */
    public Request getRequest() {
        return request;
    }

    /**
     * Método abstrato para retornar as músicas de um MusicSource
     * Deve ser implementado por cada MusicSource, já que cada um possui
     * uma forma diferente de buscar músicas
     * 
     * @return um {@code ArrayList)} com as músicas relacionadas ao MusicSource
     */
    public abstract ArrayList<Track> getTracks();

    /**
     * Retorna o ID único da fonte de música.
     * Este ID é utilizado para identificar a fonte de forma exclusiva dentro do
     * sistema ou da API integrada (e.g., Spotify).
     *
     * @return Uma {@code String} contendo o ID da fonte de música.
     */
    public String getId() {
        return this.id;
    }

    /**
     * Retorna o nome legível da fonte de música.
     * Este nome é tipicamente exibido ao usuário e descreve a fonte de música
     * (e.g., o nome de uma playlist).
     *
     * @return Uma {@code String} contendo o nome da fonte de música.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Método abstrato para retornar uma representação em string de um MusicSource
     * Cada MusicSource deve implementar o método, já que cada um possui
     * representações em string diferentes.
     */
    public abstract String toString();
}
