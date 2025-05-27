import java.util.HashMap;
import java.util.Map;

import api.HttpClientUtil;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args) {
        launch(args);
        System.out.println("Application started");
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("Content-type", "application/json; charset=UTF-8");
                try {
                    HttpClientUtil.sendGetRequest("https://jsonplaceholder.typicode.com/posts", headers);
                    System.out.println("Requisição get enviada com sucesso");
                    HttpClientUtil.sendPostRequest("https://jsonplaceholder.typicode.com/posts", headers, headers);
                    System.out.println("Requisição post enviada com sucesso");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}
