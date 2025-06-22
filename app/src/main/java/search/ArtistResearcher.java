/*
 * ArtistResearcher.java
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
 * Realiza buscas por artistas utilizando a API do Spotify.
 * Implementa a interface {@code Researcher}, permitindo integração com o
 * sistema
 * de buscas do projeto.
 * 
 * Cada resultado contém o nome e o ID do artista.
 * 
 * @author -
 */
public class ArtistResearcher implements Researcher {

    /**
     * Executa uma busca por artistas com base na string de consulta fornecida.
     * 
     * @param query A string de pesquisa a ser enviada à API do Spotify.
     * @return Uma lista de {@code SearchResult} contendo os artistas encontrados.
     */
    public ArrayList<SearchResult> search(String query) {
        Request request = User.getInstance().getRequest();
        ArrayList<SearchResult> results = new ArrayList<>();

        try {
            Json searchData = request.sendGetRequest("search?q=" + query + "&type=artist");
            ArrayList<Json> artistsData = searchData.get("artists.items").parseJsonArray();

            for (Json artistData : artistsData) {
                SearchResult currenResult = new SearchResult(
                        artistData.get("name").toString().replaceAll("\"", ""),
                        artistData.get("id").toString().replaceAll("\"", ""));

                results.add(currenResult);
            }
        } catch (RequestException e) {
            System.err.println("Ocorreu um erro ao pesquisar pela música (artista): " + e.getMessage());
        }

        return results;
    }

    /**
     * Método de teste para a funcionalidade de busca de artistas.
     * Realiza uma busca pelo artista "Os Arrais" e imprime os resultados no
     * console.
     * 
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        ArrayList<SearchResult> artistResults = SearchManager.search("Os Arrais", "artist");

        for (SearchResult result : artistResults) {
            System.out.println(result);
        }
    }
}
