/*
 * ClientDataFileManager.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */

package fileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import exceptions.IncorrectClientFileDataException;

/**
 * Gerencia a leitura e escrita de dados de cliente (client ID e client secret)
 * em um arquivo local.
 * Esta classe é responsável por persistir e recuperar as credenciais
 * necessárias para a integração
 * com a API do Spotify.
 * 
 * @author Vinícius de Oliveira - 251527
 * @author Daniel Soares Franco - 259083
 */
public class ClientDataFileManager extends FileManager {
    /**
     * Função que será executada na primeira chamada a um método estático da classe
     * Define qual é o local específico da classe onde está o arquivo
     */
    static {
        String clientId = "9afeb5fec9854592994aa191f842b529";
        String clientSecret = "0e4def4ee8924cb68daba80833c8a5c2";
        writeClientData(clientId, clientSecret);
    }
    
    public static void setLocation(){
        FileManager.SPECIFIC_LOCATION = FileManager.FILES_LOCATION + "ClientData.che";
    }

    /**
     * Escreve o Client ID e o Client Secret no arquivo de dados do cliente.
     * Este método sobrescreve o conteúdo existente do arquivo.
     *
     * @param clientId     O ID do cliente a ser salvo.
     * @param clientSecret O segredo do cliente a ser salvo.
     */
    public static void writeClientData(String clientId, String clientSecret) {
        setLocation();
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("clientId:" + clientId);
        lines.add("clientSecret:" + clientSecret);

        FileManager.writeFile(lines);
    }

    /**
     * Lê o Client ID e o Client Secret do arquivo de dados do cliente.
     *
     * @return Um {@code Map} contendo o "clientId" e o "clientSecret" lidos do
     *         arquivo.
     */
    public static Map<String, String> readClientData() {
        setLocation();
        Map<String, String> clientData = new HashMap<String, String>();
        clientData.put("clientId", null);
        clientData.put("clientSecret", null);

        ArrayList<String> readLines = FileManager.readFile();

        for (String line : readLines) {
            String[] dividedLine = line.split(":");
            clientData.put(dividedLine[0], dividedLine[1]);
        }

        return clientData;
    }

    /**
     * Atualiza o Client ID no arquivo de dados do cliente.
     * Este método lê o arquivo, encontra a linha correspondente ao Client ID e a
     * atualiza,
     * mantendo as outras linhas intactas.
     *
     * @param newClientId O novo Client ID a ser salvo.
     * @throws IncorrectClientFileDataException Se o Client ID não puder ser
     *                                          encontrado no arquivo ou se houver
     *                                          um problema de formatação.
     */
    public static void writeClientId(String newClientId)
            throws IncorrectClientFileDataException {
        setLocation();
        Boolean updatedClientId = false;
        ArrayList<String> newLines = new ArrayList<String>();
        ArrayList<String> readLines = FileManager.readFile();

        for (String line : readLines) {
            if (line.contains("clientId:")) {
                newLines.add("clientId:" + newClientId);
                updatedClientId = true;
            } else {
                newLines.add(line);
            }
        }

        FileManager.writeFile(newLines);

        if (!updatedClientId)
            throw new IncorrectClientFileDataException("Não foi possível alterar o clientId");
    }

    /**
     * Atualiza o Client Secret no arquivo de dados do cliente.
     * Este método lê o arquivo, encontra a linha correspondente ao Client Secret e
     * a atualiza,
     * mantendo as outras linhas intactas.
     *
     * @param newClientSecret O novo Client Secret a ser salvo.
     * @throws IncorrectClientFileDataException Se o Client Secret não puder ser
     *                                          encontrado no arquivo ou se houver
     *                                          um problema de formatação.
     */
    public static void writeClientSecret(String newClientSecret)
            throws IncorrectClientFileDataException {
        setLocation();
        Boolean updatedClientSecret = false;
        ArrayList<String> newLines = new ArrayList<String>();
        ArrayList<String> readLines = FileManager.readFile();

        for (String line : readLines) {
            if (line.contains("clientSecret:")) {
                newLines.add("clientSecret:" + newClientSecret);
                updatedClientSecret = true;
            } else {
                newLines.add(line);
            }
        }

        FileManager.writeFile(newLines);

        if (!updatedClientSecret)
            throw new IncorrectClientFileDataException("Não foi possível alterar o clientSecret");
    }
}
