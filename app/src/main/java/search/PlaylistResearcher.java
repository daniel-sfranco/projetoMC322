/*
 * PlaylistResearcher.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */

package search;

import java.util.ArrayList;
import user.User;
import api.Request;
import api.Json;
import exceptions.RequestException;

/**
 * Realiza buscas por playlists do usuário autenticado na API do Spotify.
 * Implementa a interface {@code Researcher}, sendo utilizada no sistema de busca
 * para recuperar playlists criadas ou seguidas pelo usuário logado.
 * 
 * A busca é feita com base no nome da playlist, de forma insensível a maiúsculas.
 * 
 * @author -
 */
public class PlaylistResearcher implements Researcher {

    /**
     * Executa uma busca por playlists cujo nome contém a string de consulta fornecida.
     * A busca é feita diretamente na conta do usuário autenticado.
     * 
     * @param query A string de busca a ser comparada com o nome das playlists.
     * @return Uma lista de {@code SearchResult} com nome e ID das playlists encontradas.
     */
    public ArrayList<SearchResult> search(String query) {
        Request request = User.getInstance().getRequest();
        ArrayList<SearchResult> results = new ArrayList<>();

        try {
            Json searchData = request.sendGetRequest("me/playlists");
            ArrayList<Json> playlistsData = searchData.get("items").parseJsonArray();

            for (Json playlistData : playlistsData) {
                String name = playlistData.get("name").toString().replaceAll("\"", "");
                if (name.toLowerCase().contains(query.toLowerCase())) {
                    SearchResult currentResult = new SearchResult(
                            name,
                            playlistData.get("id").toString().replaceAll("\"", ""));
                    results.add(currentResult);
                }
            }
        } catch (RequestException e) {
            System.err.println("Ocorreu um erro ao pesquisar pela música (playlist): " + e.getMessage());
        }

        return results;
    }
}
