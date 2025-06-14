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

public class JsonFileManager {
     private static String FILES_lOCATION = "src" + File.separator + 
                                     "main" + File.separator + "resources" + File.separator + 
                                     "savedFiles" + File.separator;

    private static void writeFile(String json, String relativeFilePath){
        try {
            Path fullPath = Paths.get(FILES_lOCATION, relativeFilePath);
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

    public static void saveJsonFile(Storable storable, String relativeFilePath){
        Json json = null;
        try {
            json = new Json(storable);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        writeFile(json.toString(), relativeFilePath);
    }

    public static <T> T readJsonFile(String relativeFilePath, Class<T> targetClass) {
        File file = new File(FILES_lOCATION + relativeFilePath);
        String jsonText = null;

        try{
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
}
