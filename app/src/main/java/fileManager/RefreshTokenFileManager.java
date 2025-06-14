package fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class RefreshTokenFileManager {
    private static String FILES_lOCATION = "src" + File.separator + 
                                     "main" + File.separator + "resources" + File.separator + 
                                     "savedFiles" + File.separator + "User" + File.separator;

    public static void writeRefreshtoken(String refreshToken) {
        try {
            Path fullPath = Paths.get(FILES_lOCATION, "RefreshToken.che");
            Path parentDir = fullPath.getParent();

            // Se os diretórios não existir, cria todos os diretórios necessários.
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            
            Files.write(fullPath, refreshToken.getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            System.out.println("Ocorreu um erro ao salvar o arquivo.");
            System.out.println("Mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static String readRefreshToken() {
        File file = new File(FILES_lOCATION + "RefreshToken.che");
        String refreshToken = null;

        try {
            Scanner reader = new Scanner(file);
            refreshToken = reader.nextLine();
            reader.close();
            return refreshToken;
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            e.printStackTrace();
        }
        
        // Caso tenha ocorrido uma excessão, retorna null
        return null;
    }

    public static Boolean deleteRefreshToken(String userId) {
        File file = new File(FILES_lOCATION + "RefreshToken.che");
        // Retorna True se o arquivo foi deletado e false caso contrário.
        return file.delete();
    }
}
