package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class GenreResearcher implements Researcher {
    public static String GENRE_FILE_LOCATION = "src" + File.separator +
            "main" + File.separator + "resources" + File.separator +
            "savedFiles" + File.separator + "genres.che";

    public ArrayList<SearchResult> search(String query) {
        ArrayList<SearchResult> results = new ArrayList<>();
        query = URLDecoder.decode(query, StandardCharsets.UTF_8);
        try {
            File file = new File(GenreResearcher.GENRE_FILE_LOCATION);
            Scanner reader = new Scanner(file);
            do {
                String[] line = reader.nextLine().split(";");
                if (line[1].toLowerCase().contains(query.toLowerCase())) {
                    results.add(new SearchResult(line[1], line[0]));
                }
            } while (reader.hasNextLine());
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return results;
    }

    public static void main(String[] args) {
        ArrayList<SearchResult> genreResults = SearchManager.search("pop", "genre");

        for (SearchResult result : genreResults) {
            System.out.println(result);
        }
    }
}
