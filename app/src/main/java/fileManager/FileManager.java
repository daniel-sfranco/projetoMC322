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
    protected static String FILES_LOCATION = "src" + File.separator +
            "main" + File.separator + "resources" + File.separator +
            "savedFiles" + File.separator;
    protected static String SPECIFIC_LOCATION = FILES_LOCATION;

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

    public static void deleteLines(String line) {
        ArrayList<String> allLines = readFile();
        ArrayList<String> newLines = new ArrayList<String>();

        for (String currentLine : allLines) {
            if (!currentLine.equals(line))
                newLines.add(currentLine);
        }

        writeFile(newLines);
    }

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
