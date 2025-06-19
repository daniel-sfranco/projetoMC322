package search;

import java.util.ArrayList;

import api.Json;
import api.Request;
import exceptions.RequestException;
import user.User;

public class ArtistResearcher implements Researcher {
    public ArrayList<SearchResult> search(String query) {
        Request request = User.getInstance().getRequest();
        ArrayList<SearchResult> results = new ArrayList<>();

        try {
            Json searchData = request.sendGetRequest("search?q=" + query + "&type=artist");
            ArrayList<Json> artistsData = searchData.get("artists.items").parseJsonArray();

            for (Json artistData : artistsData) {
                SearchResult currenResult = new SearchResult(
                        artistData.get("name").toString(),
                        artistData.get("id").toString());

                results.add(currenResult);
            }
        } catch (RequestException e) {
            System.err.println("Ocorreu um erro ao pesquisar pela música (artista): " + e.getMessage());
        }

        return results;
    }

    public static void main(String[] args) {
        ArtistResearcher artistResearcher = new ArtistResearcher();
        SearchManager searchManagerArtist = new SearchManager(artistResearcher);
        ArrayList<SearchResult> artistResults = searchManagerArtist.search("guerra e paz");

        for (SearchResult result : artistResults) {
            System.out.println(result);
        }
    }
}
