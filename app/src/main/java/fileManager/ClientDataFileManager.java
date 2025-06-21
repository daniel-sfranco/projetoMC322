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
 */
public class ClientDataFileManager {
    /**
     * O caminho para o arquivo onde os dados do cliente são armazenados.
     * O arquivo está localizado em `src/main/resources/savedFiles/ClientData.che`.
     */
    private static String CLIENTDATA_FILE_LOCATION = "ClientData.che";

    /**
     * Escreve o Client ID e o Client Secret no arquivo de dados do cliente.
     * Este método sobrescreve o conteúdo existente do arquivo.
     *
     * @param clientId     O ID do cliente a ser salvo.
     * @param clientSecret O segredo do cliente a ser salvo.
     */
    public void writeFile(String clientId, String clientSecret) {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("clientId:" + clientId);
        lines.add("clientSecret:" + clientSecret);

        FileCheManager.writeCheFile(lines, CLIENTDATA_FILE_LOCATION);
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
    public void writeClientId(String newClientId)
            throws IncorrectClientFileDataException {

        Boolean updatedClientId = false;
        ArrayList<String> newLines = new ArrayList<String>();
        ArrayList<String> readLines =
            FileCheManager.readCheFile(CLIENTDATA_FILE_LOCATION);

        for (String line : readLines) {
            if (line.contains("clientId:")) {
                newLines.add("clientId:" + newClientId);
                updatedClientId = true;
            } else {
                newLines.add(line);
            }
        }

        FileCheManager.writeCheFile(newLines, CLIENTDATA_FILE_LOCATION);
        
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
    public void writeClientSecret(String newClientSecret)
            throws IncorrectClientFileDataException {

        Boolean updatedClientSecret = false;
        ArrayList<String> newLines = new ArrayList<String>();
        ArrayList<String> readLines =
            FileCheManager.readCheFile(CLIENTDATA_FILE_LOCATION);

        for (String line : readLines) {
            if (line.contains("clientSecret:")) {
                newLines.add("clientSecret:" + newClientSecret);
                updatedClientSecret = true;
            } else {
                newLines.add(line);
            }
        }

        FileCheManager.writeCheFile(newLines, CLIENTDATA_FILE_LOCATION);
        
        if (!updatedClientSecret)
            throw new IncorrectClientFileDataException("Não foi possível alterar o clientSecret");
    }

    /**
     * Lê o Client ID e o Client Secret do arquivo de dados do cliente.
     *
     * @return Um {@code Map} contendo o "clientId" e o "clientSecret" lidos do
     *         arquivo.
     *      */
    public Map<String, String> readFile() {
        Map<String, String> clientData = new HashMap<String, String>();
        clientData.put("clientId", null);
        clientData.put("clientSecret", null);

        ArrayList<String> readLines =
            FileCheManager.readCheFile(CLIENTDATA_FILE_LOCATION);

        for (String line : readLines) {
            if (line.contains("clientId:")) {
                int startIndex = line.indexOf(":") + 1;
                String value = line.substring(startIndex);
                clientData.put("clientId", value);
            } else if (line.contains("clientSecret:")) {
                int startIndex = line.indexOf(":") + 1;
                String value = line.substring(startIndex);
                clientData.put("clientSecret", value);
            }
        }
        
        return clientData;
    }

    /**
     * Método principal para testar a funcionalidade de leitura e escrita de dados
     * do cliente.
     * Este método cria uma instância de {@code ClientDataFileManager}, escreve um
     * Client ID e um Client Secret,
     * e depois lê os dados do arquivo, exibindo-os no console.
     *
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        String clientId = "9afeb5fec9854592994aa191f842b529";
        String clientSecret = "0e4def4ee8924cb68daba80833c8a5c2";

        ClientDataFileManager fileManager = new ClientDataFileManager();
        fileManager.writeFile(clientId, clientSecret);
        
        /*try {        
            fileManager.writeClientId("Idnovo");
            fileManager.writeClientSecret("segredonovo");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }*/

        Map<String, String> clientData = fileManager.readFile();
        System.out.println("id: " + clientData.get("clientId"));
        System.out.println("segredo: " + clientData.get("clientSecret"));
    }
}
