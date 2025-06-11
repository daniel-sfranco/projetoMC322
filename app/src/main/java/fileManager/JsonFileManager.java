package fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import api.JsonUtil;

public class JsonFileManager {
     private static String FILES_lOCATION = "src" + File.separator + 
                                     "main" + File.separator + "resources" + File.separator + 
                                     "savedFiles" + File.separator;

    private static void writeFile(String json, String fileName){
        try {
            PrintWriter writer = new PrintWriter(
                JsonFileManager.FILES_lOCATION + fileName, "UTF-8");
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            System.out.println("mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void saveJsonFile(Storable storable, String fileName){
        String json = null;
        try {
            json = JsonUtil.parseObjectToJson(storable);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        writeFile(json, fileName);
    }

        public static <T> T readJsonFile(String fileName, Class<T> targetClass) {
            File file = new File(JsonFileManager.FILES_lOCATION + fileName);
            String json = null;

            try{
                Scanner reader = new Scanner(file);    
                json = reader.nextLine();
                reader.close();
        
                return JsonUtil.parseJson(json, targetClass);
            } catch (FileNotFoundException e) {
                System.out.println("Arquivo não encontrado.");
                e.printStackTrace();
            }

            // Quando há excessão, retorna null
            return null;
        }
}
