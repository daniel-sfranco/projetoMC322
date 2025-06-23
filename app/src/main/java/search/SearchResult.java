/*
 * SearchResult.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */

package search;

/**
 * Representa um resultado de busca genérico contendo um nome e um identificador.
 * 
 * Essa classe é utilizada por diferentes {@code Researcher}s para encapsular
 * os dados retornados de pesquisas por álbuns, artistas, playlists, gêneros ou faixas.
 * 
 * @author Daniel Soares Franco - 259083
 */
public class SearchResult {

    /** Nome do item retornado na busca (ex: nome do álbum, artista, playlist etc.). */
    private String name;

    /** Identificador único do item retornado na busca. */
    private String id;

    /**
     * Construtor da classe {@code SearchResult}.
     * 
     * @param name O nome do item.
     * @param id O identificador do item.
     */
    public SearchResult(String name, String id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Retorna a representação textual do resultado da busca.
     * 
     * @return Uma string com o nome e o ID do item.
     */
    @Override
    public String toString() {
        return "SearchResult [name=" + name + ", id=" + id + "]";
    }

    /**
     * Retorna o nome do item.
     * 
     * @return O nome do item.
     */
    public String getName() {
        return name;
    }

    /**
     * Define ou atualiza o nome do item.
     * 
     * @param name Novo nome a ser atribuído.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retorna o identificador do item.
     * 
     * @return O ID do item.
     */
    public String getId() {
        return id;
    }

    /**
     * Define ou atualiza o identificador do item.
     * 
     * @param id Novo ID a ser atribuído.
     */
    public void setId(String id) {
        this.id = id;
    }
}
