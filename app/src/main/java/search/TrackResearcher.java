/*
 * TrackResearcher.java
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
import exceptions.RequestException;
import user.User;

/**
 * Realiza buscas por faixas musicais (tracks) utilizando a API do Spotify.
 * Implementa a interface {@code Researcher}, sendo usada no sistema de busca
 * para encontrar músicas a partir de uma string de consulta.
 * 
 * Cada resultado contém o nome da faixa e seu identificador único.
 * 
 * @author -
 */
public class TrackResearcher implements Researcher {

    /**
     * Executa uma busca por faixas (músicas) com base na string de consulta
     * fornecida.
     * 
     * @param query A string de pesquisa a ser enviada à API do Spotify.
     * @return Uma lista de {@code SearchResult} contendo os resultados encontrados.
     */
    public ArrayList<SearchResult> search(String query) {
        Request request = User.getInstance().getRequest();
        ArrayList<SearchResult> results = new ArrayList<>();

        try {
            Json searchData = request.sendGetRequest("search?q=" + query + "&type=track");
            ArrayList<Json> tracksData = searchData.get("tracks.items").parseJsonArray();

            for (Json trackData : tracksData) {
                SearchResult currenResult = new SearchResult(
                        trackData.get("name").toString().replaceAll("\"", ""),
                        trackData.get("id").toString().replaceAll("\"", ""));

                results.add(currenResult);
            }
        } catch (RequestException e) {
            System.err.println("Ocorreu um erro ao pesquisar pela música (faixa): " + e.getMessage());
        }

        return results;
    }

    /**
     * Método de teste para a funcionalidade de busca de faixas.
     * Realiza uma busca pela faixa "Lá(r)" e imprime os resultados no console.
     * 
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        ArrayList<SearchResult> trackResults = SearchManager.search("Lá(r)", "track");

        for (SearchResult result : trackResults) {
            System.out.println(result);
        }
    }
}
