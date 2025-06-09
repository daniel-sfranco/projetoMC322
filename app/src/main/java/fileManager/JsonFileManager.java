package fileManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

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
            System.out.println(json);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        writeFile(json, fileName);
    }
}
