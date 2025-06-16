package fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Classe responsável por gerenciar o arquivo de refresh token do usuário.
 * Permite escrever, ler e deletar o refresh token em um arquivo específico.
 * O arquivo é armazenado em um diretório pré-definido dentro da estrutura do
 * projeto.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class RefreshTokenFileManager {
    private static String FILES_lOCATION = "src" + File.separator +
            "main" + File.separator + "resources" + File.separator +
            "savedFiles" + File.separator + "User" + File.separator;

    /**
     * Método para escrever o refresh token em um arquivo.
     * O arquivo será criado se não existir, e o diretório pai será criado se
     * necessário.
     * 
     * @param refreshToken O refresh token a ser salvo no arquivo.
     */
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

    /**
     * Método para ler o refresh token de um arquivo.
     * O método tenta ler a primeira linha do arquivo e retorna o conteúdo como uma
     * String.
     * 
     * @return O refresh token lido do arquivo, ou null se ocorrer um erro.
     */
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

    /**
     * Método para deletar o arquivo de refresh token.
     * Este método tenta excluir o arquivo e retorna um booleano indicando se a
     * operação foi bem-sucedida.
     * 
     * @return true se o arquivo foi deletado com sucesso, false caso contrário.
     */
    public static Boolean deleteRefreshToken() {
        File file = new File(FILES_lOCATION + "RefreshToken.che");
        // Retorna True se o arquivo foi deletado e false caso contrário.
        return file.delete();
    }
}
