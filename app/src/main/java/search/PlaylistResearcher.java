package search;

import java.util.ArrayList;
import user.User;
import api.Request;
import api.Json;
import exceptions.RequestException;

public class PlaylistResearcher implements Researcher {
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

    public static void main(String[] args) {
        PlaylistResearcher playlistResearcher = new PlaylistResearcher();
        SearchManager searchManagerPlaylist = new SearchManager();
        ArrayList<SearchResult> playlistResults = searchManagerPlaylist.search("", "playlist");

        for (SearchResult result : playlistResults) {
            System.out.println(result);
        }
    }
}
