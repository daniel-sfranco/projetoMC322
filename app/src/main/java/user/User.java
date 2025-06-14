/*
* User.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package user;

import java.io.File;
import java.util.Map;

import api.Json;
import api.Request;
import exceptions.RequestException;
import fileManager.JsonFileManager;
import fileManager.RefreshTokenFileManager;
import fileManager.Storable;


/**
 * Representa um usuário dentro do contexto da aplicação, contendo informações
 * como país, nome, email, ID, filtro de conteúdo explícito e número de seguidores.
 * Esta classe implementa a interface {@link Storable}, permitindo que suas instâncias
 * sejam salvas e lidas de um arquivo JSON.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class User implements Storable {
    private Request request;
    private String country;
    private String name;
    private String email;
    private String id;
    private Boolean explicitContentFilter;
    private int followers;

    /**
     * Construtor vazio para a classe {@code User}.
     * Este construtor é necessário para o correto funcionamento do parser JSON,
     * permitindo a desserialização de objetos {@code User} a partir de dados JSON.
     */
    public User() {}

    /**
     * Construtor completo para a classe {@code User}.
     *
     * @param country O país do usuário (ex: "BR").
     * @param name O nome de exibição do usuário.
     * @param email O endereço de email do usuário.
     * @param id O ID único do usuário na plataforma.
     * @param explicitContentFilter Um booleano indicando se o filtro de conteúdo explícito está ativo para o usuário.
     * @param followers O número de seguidores do usuário.
     */
    public User(String country, String name, String email, String id,
    Boolean explicitContentFilter, int followers) throws RequestException {
        this.request = new Request();
        Json userData = this.request.sendGetRequest("me");
        System.out.println(userData.toString());
        this.id = userData.get("id").toString();
        this.country = userData.get("country").toString();
        this.name = userData.get("display_name").toString();
        this.email = userData.get("email").toString();
        this.explicitContentFilter = userData.get("explicit_content.filter_enabled").toString() == "true" ? true : false;
        this.followers = userData.get("followers.total").toString() == "null" ? 0 : Integer.parseInt(userData.get("followers.total").toString());
        this.setRefreshToken(request.getToken().getRefresh_token());
    }
    
    /**
     * Salva os dados desta instância de usuário em um arquivo JSON.
     * O arquivo será nomeado como "User.json" e gerenciado pelo {@link JsonFileManager}.
     * Este método é uma implementação da interface {@link Storable}.
     */
    @Override
    public void saveData() {
        String userSpecificFolder = "User" + File.separator;
        JsonFileManager.saveJsonFile(this, userSpecificFolder + "User.json");
    }

    public void setRefreshToken(String refreshToken) {
        RefreshTokenFileManager.writeRefreshtoken(refreshToken);
    }

    public String getRefreshToken() {
        return RefreshTokenFileManager.readRefreshToken();
    }
    
    public Boolean deleteRefreshToken() {
        return RefreshTokenFileManager.deleteRefreshToken(this.id);
    }

    /**
     * Lê os dados de um usuário a partir do arquivo "User.json".
     * Este é um método estático que permite carregar os dados do usuário persistidos
     * sem a necessidade de uma instância de {@code User} preexistente.
     *
     * @return Um objeto {@code User} populado com os dados lidos do arquivo, ou {@code null} se o arquivo não puder ser lido ou estiver corrompido.
     */
    public static User readDataFile(String userId) {
        return JsonFileManager.readJsonFile("User" + File.separator + "User.json", User.class);
    }

    /**
     * Método principal para demonstração do salvamento e leitura de dados do usuário.
     * Cria uma nova instância de {@code User}, salva-a em um arquivo e, em seguida,
     * lê os dados de volta do arquivo, imprimindo o resultado no console.
     *
     * @param args Argumentos da linha de comando (não utilizados neste método).
     */
    public static void main(String[] args) throws RequestException {
        /*String userJson = null;
        try {
            Request request = new Request();
            userJson = request.sendGetRequest("https://api.spotify.com/v1/me");
            System.out.println(userJson);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        // User user = new User("BR", "batata", "email", "12312", false, 12);
        // User user2 = new User("BR", "BatGamer", "batcomputador", "jdfhlsdkjfa", false, 200);

        // user.saveData();
        // user2.setRefreshToken("pofijeslakçfd");
        // user2.saveData();

        // Ver tempo de execução
        // System.out.println("user1 refreshToken: " + user.getRefreshToken());
        // System.out.println("user2 refreshToken: " + user2.getRefreshToken());

        // System.out.println(user2.deleteRefreshToken());
        // System.out.println(user2.deleteRefreshToken());


        // Ler arquivo:
        // System.out.println(readDataFile(user.getId()));

        System.out.println(new User());
        
    }

    /**
     * Retorna uma representação em String do objeto {@code User}.
     *
     * @return Uma String contendo os valores de todas as propriedades do usuário.
     */
    @Override
    public String toString() {
        return "User {" +
                "country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                ", explicitContentFilter=" + explicitContentFilter +
                ", followers=" + followers +
                '}';
    }

    /**
     * Retorna o país do usuário.
     * @return O país do usuário.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Retorna o nome de exibição do usuário.
     * @return O nome do usuário.
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna o endereço de email do usuário.
     * @return O email do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retorna o ID único do usuário.
     * @return O ID do usuário.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o status do filtro de conteúdo explícito do usuário.
     * @return {@code true} se o filtro de conteúdo explícito estiver ativo, {@code false} caso contrário.
     */
    public Boolean getExplicitContentFilter() {
        return explicitContentFilter;
    }

    /**
     * Retorna o número de seguidores do usuário.
     * @return O número de seguidores.
     */
    public int getFollowers() {
        return followers;
    }
}
