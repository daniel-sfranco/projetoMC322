package projetofinal.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import projetofinal.App;

public class visualizarPlaylistsController {
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
}
