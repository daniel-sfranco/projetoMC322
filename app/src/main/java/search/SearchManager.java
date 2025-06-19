package search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SearchManager {
    public SearchManager() {}

    private String QueryURLEncode(String query) {
        try {
            return URLEncoder.encode(query, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            System.err.println("Erro ao codificar URl da query: " + e.getMessage());
        }
        return null;
    }

    public ArrayList<SearchResult> search(String query, String type) {
        ArrayList<SearchResult> results = new ArrayList<>();
        String encodedQuery = QueryURLEncode(query);
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
        results = researcher.search(encodedQuery);
        return results;
    }

    public static void main(String[] args) {
        SearchManager searchManager = new SearchManager();
        ArrayList<SearchResult> results = searchManager.search("pop", "playlist");
        for (SearchResult result : results) {
            System.out.println(result);
        }
    }
}
