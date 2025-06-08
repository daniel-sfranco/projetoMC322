package fileManager;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientDataFileManager {

    private void saveFile(String clientId, String clientSecret) {
        try {
            PrintWriter writer = new PrintWriter("src" + File.separator + 
                                     "main" + File.separator + "resources" + File.separator + 
                                     "savedFiles" + File.separator + "ClientData.che", "UTF-8");
            writer.write(clientId);
            writer.write("\n"+clientSecret);
            writer.close();
        } catch (IOException e) {
            System.out.println("menasgem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        ClientDataFileManager fileManager = new ClientDataFileManager();
        String clientId = "9afeb5fec9854592994aa191f842b529";
        String clientSecret = "0e4def4ee8924cb68daba80833c8a5c2";
        fileManager.saveFile(clientId, clientSecret);
    }
}
