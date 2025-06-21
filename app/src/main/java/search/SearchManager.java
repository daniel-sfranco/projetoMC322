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
 * @author -
 */
public class SearchManager {

    /** Construtor padrão. Não realiza nenhuma operação. */
    public SearchManager() {}

    /**
     * Executa uma busca com base na string de consulta e no tipo de entidade desejada.
     * Direciona a chamada para a implementação de {@code Researcher} apropriada.
     * 
     * @param query A string a ser buscada (ex: nome do artista, álbum, etc.).
     * @param type O tipo da busca: {@code "album"}, {@code "artist"}, {@code "genre"}, {@code "playlist"} ou outro (padrão: faixa).
     * @return Uma lista de {@code SearchResult} com os resultados da busca.
     */
    public static ArrayList<SearchResult> search(String query, String type) {
        ArrayList<SearchResult> results = new ArrayList<>();
        Researcher researcher;

        switch (type) {
            case "genre":
                researcher = new GenreResearcher();
                break;
            case "album":
                researcher = new AlbumResearcher();
                break;
            case "artist":
                researcher = new ArtistResearcher();
                break;
            case "playlist":
                researcher = new PlaylistResearcher();
                break;
            default:
                researcher = new TrackResearcher(); // assume busca por faixa como padrão
                break;
        }
        query = HttpClientUtil.QueryURLEncode(query);
        results = researcher.search(query);
        return results;
    }

    /**
     * Método de teste para a funcionalidade de busca.
     * Realiza uma busca por playlists com a palavra "músicas" e imprime os resultados no console.
     * 
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        ArrayList<SearchResult> results = SearchManager.search("músicas", "playlist");
        for (SearchResult result : results) {
            System.out.println(result);
        }
    }
}
