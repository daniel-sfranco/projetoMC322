package search;

import java.util.ArrayList;

import api.Json;
import api.Request;
import user.User;
import exceptions.RequestException;

public class AlbumResearcher implements Researcher {
    public ArrayList<SearchResult> search(String query) {
        Request request = User.getInstance().getRequest();
        ArrayList<SearchResult> results = new ArrayList<>();

        try {
            Json searchData = request.sendGetRequest("search?q=" + query + "&type=album");
            ArrayList<Json> albumsData = searchData.get("albums.items").parseJsonArray();

            for (Json albumData : albumsData) {
                SearchResult currentResult = new SearchResult(
                        albumData.get("name").toString(),
                        albumData.get("id").toString());

                results.add(currentResult);
            }
        } catch (RequestException e) {
            System.err.println("Ocorreu um erro ao pesquisar pela música (álbum): " + e.getMessage());
        }

        return results;
    }

    public static void main(String[] args) {
        AlbumResearcher albumResearcher = new AlbumResearcher();
        SearchManager searchManagerAlbum = new SearchManager(albumResearcher);
        ArrayList<SearchResult> albumResults = searchManagerAlbum.search("guerra e paz");

        for (SearchResult result : albumResults) {
            System.out.println(result);
        }
    }
}
