/*
* User.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package user;

import api.Json;
import api.Request;
import exceptions.IncorrectClientFileDataException;
import exceptions.RequestException;
import fileManager.RefreshTokenFileManager;

/**
 * Representa um usuário dentro do contexto da aplicação, contendo informações
 * como país, nome, email, ID, filtro de conteúdo explícito e número de
 * seguidores.
 * 
 * @author Vinícius de Oliveira - 251527
 */
public class User {
    private static User INSTANCE = null;
    private Request request;
    private String country;
    private String name;
    private String email;
    private String id;
    private Boolean explicitContentFilter;
    private int followers;

    /**
     * Construtor completo para a classe {@code User}.
     *
     * @param country               O país do usuário (ex: "BR").
     * @param name                  O nome de exibição do usuário.
     * @param email                 O endereço de email do usuário.
     * @param id                    O ID único do usuário na plataforma.
     * @param explicitContentFilter Um booleano indicando se o filtro de conteúdo
     *                              explícito está ativo para o usuário.
     * @param followers             O número de seguidores do usuário.
     * @throws IncorrectClientFileDataException
     */
    private User() throws RequestException {
        this.request = new Request();
        Json userData = this.request.sendGetRequest("me");
        this.id = userData.get("id").toString().replaceAll("\"", "");
        this.country = userData.get("country").toString().replaceAll("\"", "");
        this.name = userData.get("display_name").toString();
        this.email = userData.get("email").toString();
        this.explicitContentFilter = userData.get("explicit_content.filter_enabled").toString() == "true" ? true
                : false;
        this.followers = userData.get("followers.total").toString() == "null" ? 0
                : Integer.parseInt(userData.get("followers.total").toString());
        this.setRefreshToken(request.getToken().getRefreshToken());
    }

    /**
     * Retorna a instância única de {@code User}. Se a instância ainda não foi
     * criada,
     * ela será inicializada através de uma requisição à API.
     *
     * @return A instância única de {@code User}.
     */
    public static User getInstance() {
        if (INSTANCE == null) {
            try {
                INSTANCE = new User();
            } catch (RequestException e) {
                System.out.println("Ocorreu um erro ao requisitar o usuário");
                System.out.println("Mensagem: " + e.getMessage());
            }
        }
        return INSTANCE;
    }

    /**
     * Define o token de atualização do usuário.
     * Este método armazena o token de atualização no arquivo gerenciado pelo
     * {@link RefreshTokenFileManager}.
     *
     * @param refreshToken O token de atualização a ser armazenado.
     */
    public void setRefreshToken(String refreshToken) {
        RefreshTokenFileManager.writeRefreshToken(refreshToken);
    }

    /**
     * Obtém o token de atualização do usuário.
     * Este método lê o token de atualização armazenado no arquivo gerenciado pelo
     * {@link RefreshTokenFileManager}.
     *
     * @return O token de atualização do usuário, ou {@code null} se não estiver
     *         definido.
     */
    public String getRefreshToken() {
        return RefreshTokenFileManager.readRefreshToken();
    }

    /**
     * Exclui o token de atualização do usuário.
     * Este método remove o token de atualização armazenado no arquivo gerenciado
     * pelo
     * {@link RefreshTokenFileManager}.
     *
     * @return {@code true} se a exclusão foi bem-sucedida, {@code false} caso
     *         contrário.
     */
    public Boolean deleteRefreshToken() {
        return RefreshTokenFileManager.deleteFile();
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
     * 
     * @return O país do usuário.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Retorna o nome de exibição do usuário.
     * 
     * @return O nome do usuário.
     */
    public String getName() {
        return name;
    }

    /**
     * Retorna o endereço de email do usuário.
     * 
     * @return O email do usuário.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Retorna o ID único do usuário.
     * 
     * @return O ID do usuário.
     */
    public String getId() {
        return id;
    }

    /**
     * Retorna o status do filtro de conteúdo explícito do usuário.
     * 
     * @return {@code true} se o filtro de conteúdo explícito estiver ativo,
     *         {@code false} caso contrário.
     */
    public Boolean getExplicitContentFilter() {
        return explicitContentFilter;
    }

    /**
     * Retorna o número de seguidores do usuário.
     * 
     * @return O número de seguidores.
     */
    public int getFollowers() {
        return followers;
    }

    /**
     * Retorna a requisição associada a este usuário.
     * Este método pode ser usado para acessar informações adicionais ou fazer
     * requisições relacionadas ao usuário.
     *
     * @return A instância de {@link Request} associada ao usuário.
     */
    public Request getRequest() {
        return request;
    }
}
