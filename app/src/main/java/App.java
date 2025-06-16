import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Classe principal da aplicação JavaFX que exibe uma janela com um botão.
 * Quando o botão é clicado, imprime "Hello World!" no console.
 */
public class App extends Application {
    /**
     * Método principal que inicia a aplicação JavaFX.
     * 
     * @param args Argumentos da linha de comando (não utilizados).
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Método que configura e exibe a janela principal da aplicação.
     * 
     * @param primaryStage O palco primário da aplicação.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
