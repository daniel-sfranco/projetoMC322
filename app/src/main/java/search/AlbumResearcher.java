/*
 * AlbumResearcher.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */

package search;

import java.util.ArrayList;

import api.Json;
import api.Request;
import user.User;
import exceptions.RequestException;

/**
 * Realiza buscas por álbuns utilizando a API do Spotify.
 * Implementa a interface {@code Researcher}, permitindo que este pesquisador
 * seja usado de forma polimórfica no sistema de busca.
 * 
 * A busca retorna uma lista de {@code SearchResult} com nome e ID de cada
 * álbum.
 * 
 * @author -
 */
public class AlbumResearcher implements Researcher {

    /**
     * Executa uma busca por álbuns com base na string de consulta fornecida.
     * 
     * @param query A string de pesquisa a ser enviada à API do Spotify.
     * @return Uma lista de resultados de pesquisa contendo os nomes e IDs dos
     *         álbuns.
     */
    public ArrayList<SearchResult> search(String query) {
        Request request = User.getInstance().getRequest();
        ArrayList<SearchResult> results = new ArrayList<>();

        try {
            Json searchData = request.sendGetRequest("search?q=" + query + "&type=album");
            ArrayList<Json> albumsData = searchData.get("albums.items").parseJsonArray();

            for (Json albumData : albumsData) {
                SearchResult currentResult = new SearchResult(
                        albumData.get("name").toString().replaceAll("\"", ""),
                        albumData.get("id").toString().replaceAll("\"", ""));

                results.add(currentResult);
            }
        } catch (RequestException e) {
            System.err.println("Ocorreu um erro ao pesquisar pela música (álbum): " + e.getMessage());
        }

        return results;
    }

    /**
     * Método de teste para a funcionalidade de busca de álbuns.
     * Realiza uma busca pela string "guerra e paz" e imprime os resultados no
     * console.
     *
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        ArrayList<SearchResult> albumResults = SearchManager.search("guerra e paz", "album");

        for (SearchResult result : albumResults) {
            System.out.println(result);
        }
    }
}
