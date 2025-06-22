package fileManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Classe responsável por gerenciar o arquivo de refresh token do usuário.
 * Permite escrever, ler e deletar o refresh token em um arquivo específico.
 * O arquivo é armazenado em um diretório pré-definido dentro da estrutura do
 * projeto.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class RefreshTokenFileManager extends FileManager {
    static {
        FileManager.SPECIFIC_LOCATION = FileManager.FILES_LOCATION + "User" + File.separator + "RefreshToken.che";
    }

    /**
     * Método para escrever o refresh token em um arquivo.
     * O arquivo será criado se não existir, e o diretório pai será criado se
     * necessário.
     * 
     * @param refreshToken O refresh token a ser salvo no arquivo.
     */
    public static void writeRefreshToken(String refreshToken) {
        ArrayList<String> lines = new ArrayList<String>(
            Arrays.asList(refreshToken));

        writeFile(lines);
    }

    /**
     * Método para ler o refresh token de um arquivo.
     * O método tenta ler a primeira linha do arquivo e retorna o conteúdo como uma
     * String.
     * 
     * @return O refresh token lido do arquivo, ou null se ocorrer um erro.
     */
    public static String readRefreshToken() {
        ArrayList<String> readLines = readFile();
        
        if (readLines.isEmpty()) {
            return null;
        } else {
            return readLines.get(0);
        }
    }
}
