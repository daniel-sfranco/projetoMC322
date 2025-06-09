package fileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import exceptions.IncorrectClientFileDataException;

/**
 * Gerencia a leitura e escrita de dados de cliente (client ID e client secret) em um arquivo local.
 * Esta classe é responsável por persistir e recuperar as credenciais necessárias para a integração
 * com a API do Spotify.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class ClientDataFileManager {
    /**
     * O caminho para o arquivo onde os dados do cliente são armazenados.
     * O arquivo está localizado em `src/main/resources/savedFiles/ClientData.che`.
     */
    private static String ClientDataFileLocation = "src" + File.separator + 
                                     "main" + File.separator + "resources" + File.separator + 
                                     "savedFiles" + File.separator + "ClientData.che";

    /**
     * Escreve o Client ID e o Client Secret no arquivo de dados do cliente.
     * Este método sobrescreve o conteúdo existente do arquivo.
     *
     * @param clientId O ID do cliente a ser salvo.
     * @param clientSecret O segredo do cliente a ser salvo.
     */
    public void writeFile(String clientId, String clientSecret) {
        try {
            PrintWriter writer = new PrintWriter(
                ClientDataFileManager.ClientDataFileLocation, "UTF-8");
            writer.write("clientId:" + clientId);
            writer.write("\nclientSecret:" + clientSecret);
            writer.close();
        } catch (IOException e) {
            System.out.println("mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Atualiza o Client ID no arquivo de dados do cliente.
     * Este método lê o arquivo, encontra a linha correspondente ao Client ID e a atualiza,
     * mantendo as outras linhas intactas.
     *
     * @param newClientId O novo Client ID a ser salvo.
     * @throws IncorrectClientFileDataException Se o Client ID não puder ser encontrado no arquivo ou se houver um problema de formatação.
     */
    public void writeClientID(String newClientId)
        throws IncorrectClientFileDataException {

        List<String> lines = new ArrayList<String>();
        Boolean updatedClientId = false;

        try {
            File file = new File(ClientDataFileManager.ClientDataFileLocation);
            Scanner reader = new Scanner(file);
            do {
                String line = reader.nextLine();
                if (line.contains("clientId:")) { 
                    lines.add("clientId:" + newClientId);
                    updatedClientId = true;
                } else {
                    lines.add(line);
                }
            } while (reader.hasNextLine());
            reader.close();

            if (!updatedClientId)
                throw new IncorrectClientFileDataException("Não foi possível alterar o clientId");

            PrintWriter writer = new PrintWriter(
                ClientDataFileManager.ClientDataFileLocation, "UTF-8");
            for (int i = 0; i < lines.size(); i++) {
                if (i != 0) writer.write("\n");
                writer.write(lines.get(i));
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Atualiza o Client Secret no arquivo de dados do cliente.
     * Este método lê o arquivo, encontra a linha correspondente ao Client Secret e a atualiza,
     * mantendo as outras linhas intactas.
     *
     * @param newClientSecret O novo Client Secret a ser salvo.
     * @throws IncorrectClientFileDataException Se o Client Secret não puder ser encontrado no arquivo ou se houver um problema de formatação.
     */
    public void writeClientSecret(String newClientSecret)
        throws IncorrectClientFileDataException {

        List<String> lines = new ArrayList<String>();
        Boolean updatedClientSecret = false;

        try {
            File file = new File(ClientDataFileManager.ClientDataFileLocation);
            Scanner reader = new Scanner(file);
            do {
                String line = reader.nextLine();
                if (line.contains("clientSecret:")) { 
                    lines.add("clientSecret:" + newClientSecret);
                    updatedClientSecret = true;
                } else {
                    lines.add(line);
                }
            } while (reader.hasNextLine());
            reader.close();

            if (!updatedClientSecret)
                throw new IncorrectClientFileDataException("Não foi possível alterar o clientSecret");

            PrintWriter writer = new PrintWriter(
                ClientDataFileManager.ClientDataFileLocation, "UTF-8");
            for (int i = 0; i < lines.size(); i++) {
                if (i != 0) writer.write("\n");
                writer.write(lines.get(i));
            }
            writer.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("mensagem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Lê o Client ID e o Client Secret do arquivo de dados do cliente.
     *
     * @return Um {@code Map} contendo o "clientId" e o "clientSecret" lidos do arquivo.
     * @throws IncorrectClientFileDataException Se o arquivo estiver incompleto ou com formatação incorreta.
     */
    public Map<String, String> readFile()
        throws IncorrectClientFileDataException {
            
        Map<String, String> clientData = new HashMap<String, String>();
        clientData.put("clientId", null);
        clientData.put("clientSecret", null);

        try {
            File file = new File(ClientDataFileManager.ClientDataFileLocation);
            Scanner reader = new Scanner(file);
            do {
                String line = reader.nextLine();
                if (line.contains("clientId:")) { 
                    int startIndex = line.indexOf(":") + 1;
                    String value = line.substring(startIndex);
                    clientData.put("clientId", value);
                } else if (line.contains("clientSecret:")) { 
                    int startIndex = line.indexOf(":") + 1;
                    String value = line.substring(startIndex);
                    clientData.put("clientSecret", value);
                }
            } while (reader.hasNextLine());
            reader.close();

            // É necessário que todas as informações estejam no arquivo.
            if (clientData.get("clientId") == null ||
                clientData.get("clientSecret") == null) {
                    throw new IncorrectClientFileDataException(
                        "Arquivo incompleto ou com formatação incorreta.");
            }

            return clientData;
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        ClientDataFileManager fileManager = new ClientDataFileManager();
        String clientId = "9afeb5fec9854592994aa191f842b529";
        String clientSecret = "0e4def4ee8924cb68daba80833c8a5c2";
        fileManager.writeFile(clientId, clientSecret);


        try {
            // fileManager.writeClientID("HAHAHAHAAHAHA");
            // fileManager.writeClientSecret("fununcia he he");

            Map<String, String> clientData = fileManager.readFile();
            System.out.println("id: " + clientData.get("clientId"));
            System.out.println("segredo: " + clientData.get("clientSecret"));
        } catch (IncorrectClientFileDataException e){
            System.out.println(e.getMessage());
        }

    }
}
