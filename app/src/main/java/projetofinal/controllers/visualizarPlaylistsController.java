package projetofinal.controllers;

import fileManager.PlaylistFileManager;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import music.Playlist;
import music.Track;
import projetofinal.App;

import java.io.IOException;
import java.util.ArrayList;

public class visualizarPlaylistsController {

    @FXML
    private VBox playlistContainer;

    @FXML
    private void acessarCriarPlaylists() {
        try {
            App.setRoot("playlist");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {
        try {
            ArrayList<String> playlistIds = PlaylistFileManager.getUserPlaylists();
            for (String id : playlistIds) {
                Playlist playlist = new Playlist(id);
                VBox card = criarCardPlaylist(playlist);
            
                final String playlistId = playlist.getId(); 
            
                playlistContainer.getChildren().add(card);

                card.setOnMouseClicked(event -> {
                    try {
                        Playlist.editPlaylist(playlistId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private VBox criarCardPlaylist(Playlist playlist) {
        VBox card = new VBox();
        card.setSpacing(8);
        card.setStyle("-fx-background-color: #DDDDDD; -fx-padding: 10; -fx-background-radius: 10;");
        card.setPrefWidth(800);
    
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER_LEFT);
    
        Label nome = new Label(playlist.getName());
        nome.setPrefWidth(300);
        nome.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
    
        Label duracao = new Label(calcularDuracaoTotal(playlist));
        duracao.setStyle("-fx-text-fill: #555555;");
    
        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);
    
        hbox.getChildren().addAll(nome, spacer1, duracao, spacer2);
        card.getChildren().add(hbox);
    
        card.setStyle(card.getStyle() + "-fx-cursor: hand;");
    
        return card;
    }
    

    
    private String calcularDuracaoTotal(Playlist playlist) {
        int totalMs = playlist.getTracks().stream().mapToInt(Track::getDuration).sum();
        int totalMin = totalMs / 60000;
        return totalMin + " min";
    }
}
