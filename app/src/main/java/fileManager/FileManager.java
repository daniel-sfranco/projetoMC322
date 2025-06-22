package fileManager;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class FileManager {
    /**
     * Atributos estáticos para locais de arquivos
     * FILES_LOCATION tem o lugar base para os arquivos
     * SPECIFIC_LOCATION tem o caminho relativo a FILES_LOCATION onde
     * o arquivo atual será armazenado
     */
    protected static String FILES_LOCATION = "src" + File.separator +
            "main" + File.separator + "resources" + File.separator +
            "savedFiles" + File.separator;
    protected static String SPECIFIC_LOCATION = FILES_LOCATION;

    /**
     * Método que cria um arquivo em SPECIFIC_LOCATION, se não existir, e escreve
     * informações nele
     * 
     * @param lines as linhas a serem escritas no arquivo
     */
    public static void writeFile(ArrayList<String> lines) {
        try {
            Path fullPath = Paths.get(SPECIFIC_LOCATION);
            Path parentDir = fullPath.getParent();

            // Se os diretórios não existir, cria todos os diretórios necessários.
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            Files.write(fullPath, lines, StandardCharsets.UTF_8);

        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao salvar o arquivo.");
            System.err.println("Mensagem: " + e.getMessage());
        }
    }

    /**
     * Lê um arquivo e retorna as linhas dele
     * 
     * @return as linhas do arquivo em SPECIFIC_LOCATION
     */
    public static ArrayList<String> readFile() {
        Path fullPath = Paths.get(SPECIFIC_LOCATION);

        if (!Files.exists(fullPath)) {
            System.err.println("Erro: Arquivo não encontrado em " + fullPath);
            return new ArrayList<String>();
        }

        try {
            List<String> lines = Files.readAllLines(fullPath, StandardCharsets.UTF_8);
            return (ArrayList<String>) lines;
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao ler o arquivo.");
            System.err.println("Mensagem: " + e.getMessage());
            return new ArrayList<String>();
        }
    }

    /**
     * Deleta uma linha no arquivo em SPECIFIC_LOCATION se a linha existir
     * 
     * @param line a linha a ser excluída
     */
    public static void deleteLine(String line) {
        ArrayList<String> allLines = readFile();
        ArrayList<String> newLines = new ArrayList<String>();

        for (String currentLine : allLines) {
            if (!currentLine.equals(line))
                newLines.add(currentLine);
        }

        writeFile(newLines);
    }

    /**
     * Adiciona um conjunto de novas linhas a um arquivo em SPECIFIC_LOCATION
     *
     * @param newLines as novas linhas a serem adicionadas
     */
    public static void addData(ArrayList<String> newLines) {
        Path fullPath = Paths.get(SPECIFIC_LOCATION);

        try {
            Files.write(fullPath, newLines, StandardCharsets.UTF_8,
                    StandardOpenOption.APPEND, StandardOpenOption.CREATE);
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao adicionar dados no arquivo.");
            System.err.println("Mensagem: " + e.getMessage());
        }
    }

    /**
     * Deleta o arquivo em SPECIFIC_LOCATION, se existir
     *
     * @return {@code true} caso o arquivo tenha sido deletado, {@code false} caso
     *         ele já não existia antes
     */
    public static boolean deleteFile() {
        try {
            Path fullPath = Paths.get(SPECIFIC_LOCATION);
            return Files.deleteIfExists(fullPath);
        } catch (IOException e) {
            System.err.println("Ocorreu um erro ao tentar deletar o arquivo.");
            System.err.println("Mensagem: " + e.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        ArrayList<String> lines = new ArrayList<String>(
                Arrays.asList("hello", "there", "world"));

        String relativePath = "test" + File.separator +
                "text.che";
        FileManager.writeFile(lines);
        System.out.println(FileManager.readFile());
    }
}
