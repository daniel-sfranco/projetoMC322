package projetofinal.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import projetofinal.App;

import java.io.IOException;

public class PlaylistController {

    @FXML
    private void acessarVisualizarPlaylists(ActionEvent event) {
        try {
            App.setRoot("visualizarPlaylists");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void acessarCriarPlaylists(ActionEvent event) {
        try {
            App.setRoot("playlist");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void GerarPlaylist() {
        System.out.println("Gerando playlist..."); // vamo testar se funciona
    }


}
