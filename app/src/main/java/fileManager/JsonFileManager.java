package fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import api.Json;

/**
 * Classe responsável por gerenciar arquivos JSON dentro da aplicação.
 * Permite salvar objetos que implementam a interface {@link Storable} em
 * arquivos
 * JSON e ler esses arquivos de volta, convertendo-os para o tipo especificado.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class JsonFileManager extends FileManager {
    /**
     * Método auxiliar para escrever um JSON em um arquivo.
     * O arquivo será criado se não existir, e o diretório pai será criado se
     * necessário.
     * 
     * @param json             O conteúdo JSON a ser salvo no arquivo.
     * @param relativeFilePath O caminho relativo do arquivo onde o JSON será salvo.
     */
    private static void writeFile(String json, String relativeFilePath) {
        try {
            Path fullPath = Paths.get(FileManager.FILES_LOCATION, relativeFilePath);
            Path parentDir = fullPath.getParent();

            // Se os diretórios não existir, cria todos os diretórios necessários.
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            Files.write(fullPath, json.getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o arquivo.");
            System.out.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Método para ler um arquivo JSON e convertê-lo em um objeto do tipo
     * especificado.
     * O arquivo deve estar localizado no caminho relativo fornecido.
     * 
     * @param relativeFilePath O caminho relativo do arquivo JSON a ser lido.
     * @param targetClass      A classe do tipo para o qual o JSON será convertido.
     * @param <T>              O tipo do objeto a ser retornado.
     * @return Um objeto do tipo especificado, ou null se ocorrer um erro ao ler o
     *         arquivo.
     */
    public static <T> T readFile(String relativeFilePath, Class<T> targetClass) {
        File file = new File(FileManager.FILES_LOCATION + relativeFilePath);
        String jsonText = null;

        try {
            Scanner reader = new Scanner(file);
            jsonText = reader.nextLine();
            reader.close();

            Json json = new Json(jsonText);
            return json.parseJson(targetClass);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            e.printStackTrace();
        }

        // Quando há excessão, retorna null
        return null;
    }

    /**
     * Método para salvar um objeto que implementa a interface {@link Storable} em
     * um
     * arquivo JSON.
     * O arquivo será criado se não existir, e o diretório pai será criado se
     * necessário.
     * 
     * @param storable         O objeto a ser salvo no arquivo JSON.
     * @param relativeFilePath O caminho relativo do arquivo onde o JSON será salvo.
     */
    public static void saveJsonFile(Object storeObject, String relativeFilePath) {
        Json json = null;
        try {
            json = new Json(storeObject);
            writeFile(json.toString(), relativeFilePath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
