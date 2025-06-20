package search;

import java.util.ArrayList;

import api.Json;
import api.Request;
import exceptions.RequestException;
import user.User;

public class TrackResearcher implements Researcher {

    public ArrayList<SearchResult> search(String query) {
        Request request = User.getInstance().getRequest();
        ArrayList<SearchResult> results = new ArrayList<>();

        try {
            Json searchData = request.sendGetRequest("search?q=" + query + "&type=track");
            ArrayList<Json> tracksData = searchData.get("tracks.items").parseJsonArray();

            for (Json trackData : tracksData) {
                SearchResult currenResult = new SearchResult(
                        trackData.get("name").toString(),
                        trackData.get("id").toString());

                results.add(currenResult);
            }
        } catch (RequestException e) {
            System.err.println("Ocorreu um erro ao pesquisar pela música (faixa): " + e.getMessage());
        }

        return results;
    }

    public static void main(String[] args) {
        ArrayList<SearchResult> trackResults = SearchManager.search("Lá(r)", "track");

        for (SearchResult result : trackResults) {
            System.out.println(result);
        }
    }
}
