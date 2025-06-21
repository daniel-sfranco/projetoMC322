/*
 * App.java
 * 
 * Material usado na disciplina MC322 - Programação orientada a objetos.
 * 
 * A documentação para javadoc deste arquivo foi feita com o uso de IA
 * e posteriormente revisada e/ou corrigida.
 */

package projetofinal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;

/**
 * Classe principal da aplicação JavaFX. Responsável por iniciar a interface gráfica
 * e controlar a troca de telas utilizando arquivos FXML.
 * 
 * A primeira tela carregada é a "playlist.fxml".
 * 
 * @author Ana
 * @author Anna
 */
public class App extends Application {

    /** Cena principal da aplicação, utilizada para trocar a raiz (root) dinamicamente. */
    private static Scene scene;

    /**
     * Método de inicialização da aplicação JavaFX. 
     * Carrega a tela inicial e configura a janela principal.
     *
     * @param stage O palco principal da aplicação.
     * @throws IOException Se houver erro ao carregar o arquivo FXML.
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("playlist"), 1186, 706);
        stage.setScene(scene);
        stage.setTitle("Montar Playlist");
        stage.setResizable(false); // impede o redimensionamento da janela

        stage.show();
    }

    /**
     * Altera a raiz (root) da cena atual para um novo arquivo FXML.
     *
     * @param fxml O nome do arquivo FXML (sem extensão) a ser carregado.
     * @throws IOException Se o arquivo FXML não for encontrado ou não puder ser carregado.
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Carrega um arquivo FXML da pasta de recursos.
     *
     * @param fxml O nome do arquivo FXML (sem extensão) a ser carregado.
     * @return Um objeto {@code Parent} representando a raiz da nova cena.
     * @throws IOException Se houver falha ao carregar o arquivo FXML.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/projetofinal/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Método principal da aplicação. Inicializa o usuário singleton e inicia a interface gráfica.
     *
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        User user = User.getInstance();
        launch();
    }
}
