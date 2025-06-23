/*
 * SearchManager.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */

package search;

import java.util.ArrayList;
import api.HttpClientUtil;

/**
 * Responsável por gerenciar o processo de pesquisa no sistema.
 * Encapsula a escolha do tipo adequado de {@code Researcher} com base no tipo solicitado
 * e delega a execução da busca para a implementação correta.
 * 
 * Essa classe serve como ponto de entrada centralizado para as funcionalidades de busca.
 * 
 * @author Daniel Soares Franco - 259083
 */
public class SearchManager {

    /** Construtor padrão. Não realiza nenhuma operação. */
    public SearchManager() {}

    /**
     * Executa uma busca com base na string de consulta e no tipo de entidade desejada.
     * Direciona a chamada para a implementação de {@code Researcher} apropriada.
     * 
     * @param query A string a ser buscada (ex: nome do artista, álbum, etc.).
     * @param researcher um objeto que implemetna a interface {@code Researcher}, que indica
     * qual tipo de filtro deve ser implementado
     * Opções: {@code GenreResearcher}, {@code AlbumResearcher}, {@code ArtistResearcher},
     * {@code PlaylistResearcher} e {@code TrackResearcher}
     * @return Uma lista de {@code SearchResult} com os resultados da busca.
     */
    public static ArrayList<SearchResult> search(String query, Researcher researcher) {
        ArrayList<SearchResult> results = new ArrayList<>();
        query = HttpClientUtil.QueryURLEncode(query);
        results = researcher.search(query);
        return results;
    }
}
