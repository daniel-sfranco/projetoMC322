package search;

import java.util.ArrayList;
import api.HttpClientUtil;

public class SearchManager {
    public SearchManager() {}

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
                researcher = new TrackResearcher();
                break;
        }
        query = HttpClientUtil.QueryURLEncode(query);
        results = researcher.search(query);
        return results;
    }

    public static void main(String[] args) {
        ArrayList<SearchResult> results = SearchManager.search("músicas", "playlist");
        for (SearchResult result : results) {
            System.out.println(result);
        }
    }
}
