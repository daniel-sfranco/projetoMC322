package search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SearchManager {
    private Researcher researcher;

    public SearchManager(Researcher researcher){
        this.researcher = researcher;
    }

	private String QueryURLEncode(String query) {
		try {
			return URLEncoder.encode(query, StandardCharsets.UTF_8.name());
		} catch (UnsupportedEncodingException e) {
			System.err.println("Erro ao codificar URl da query: " + e.getMessage());
		}
		return null;
	}

	public ArrayList<SearchResult> search(String query){
        ArrayList<SearchResult> results = new ArrayList<>();
		String encodedQuery = QueryURLEncode(query);
		results = this.researcher.search(encodedQuery);

		return results;
    }

	public Researcher getResearcher() {
		return researcher;
	}

	public void setResearcher(Researcher researcher) {
		this.researcher = researcher;
	}

	public static void main(String[] args) {
		GenreResearcher  genreResearcher = new GenreResearcher();
		SearchManager searchManagerGenre = new SearchManager(genreResearcher);
		ArrayList<SearchResult> genreResults = searchManagerGenre.search("cristão");

		for (SearchResult result : genreResults) {
			System.out.println(result);
		}
	
		// ArtistResearcher  artistResearcher = new ArtistResearcher();
		// SearchManager searchManagerArtist = new SearchManager(artistResearcher);
		
		// ArrayList<SearchResult> artistResults = searchManagerArtist.search("Frank Si");
		// for (SearchResult result : artistResults) {
			// System.out.println(result);
		// }

	}
}
