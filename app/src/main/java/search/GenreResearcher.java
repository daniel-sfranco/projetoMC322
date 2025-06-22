/*
 * GenreResearcher.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */

package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Realiza buscas por gêneros musicais a partir de um arquivo local contendo os dados.
 * Implementa a interface {@code Researcher}, integrando-se ao sistema de busca do projeto.
 * 
 * Cada linha do arquivo deve conter o ID e o nome do gênero separados por ponto e vírgula.
 * Exemplo: {@code pop123;Pop}
 * 
 * @author 
 */
public class GenreResearcher implements Researcher {

    /**
     * Caminho para o arquivo que contém os gêneros musicais disponíveis.
     * Espera-se que esteja localizado em:
     * {@code src/main/resources/savedFiles/genres.che}
     */
    public static String GENRE_FILE_LOCATION = "src" + File.separator +
            "main" + File.separator + "resources" + File.separator +
            "savedFiles" + File.separator + "genres.che";

    /**
     * Realiza uma busca textual por gêneros musicais no arquivo local.
     * A busca não diferencia maiúsculas de minúsculas.
     *
     * @param query Termo de busca a ser comparado com os nomes dos gêneros.
     * @return Lista de {@code SearchResult} com os gêneros correspondentes.
     */
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
}
