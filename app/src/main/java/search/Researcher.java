/*
 * Researcher.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */

package search;

import java.util.ArrayList;

/**
 * Interface que define o contrato para classes capazes de realizar pesquisas
 * com base em uma string de consulta.
 * 
 * Cada implementação da interface deve fornecer a lógica específica para
 * pesquisar entidades como álbuns, artistas, gêneros ou playlists.
 * 
 * O resultado da pesquisa é uma lista de objetos {@code SearchResult}.
 * 
 * @author Daniel Soares Franco - 259083
 */
public interface Researcher {
    /**
     * Realiza uma busca com base na string fornecida.
     *
     * @param query A string de pesquisa.
     * @return Uma lista de {@code SearchResult} contendo os resultados encontrados.
     */
    public ArrayList<SearchResult> search(String query);
}
