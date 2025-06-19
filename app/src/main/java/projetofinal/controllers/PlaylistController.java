package projetofinal.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import projetofinal.App;
import search.SearchManager;
import search.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlaylistController {

    @FXML private TextField generoTextField;
    @FXML private FlowPane generosAdicionadosFlow;

    @FXML private TextField artistasTextField; // com 's'
    @FXML private FlowPane artistasAdicionadosFlow;

    @FXML private TextField albumTextField;
    @FXML private FlowPane albumAdicionadosFlow;  // sem 's'

    @FXML private TextField musicaTextField;
    @FXML private FlowPane musicaAdicionadosFlow; // sem 's'

    private final List<SearchResult> generosSelecionados = new ArrayList<>();
    private final List<SearchResult> artistasSelecionados = new ArrayList<>();
    private final List<SearchResult> albunsSelecionados = new ArrayList<>();
    private final List<SearchResult> musicasSelecionadas = new ArrayList<>();

    // Ações de navegação
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
        System.out.println("Gerando playlist...");
        // Lógica de criação da playlist baseada nas listas acima
    }

    // Métodos de adição
    @FXML
    private void adicionarGenero() {
        adicionarItem(generoTextField, "genre", generosSelecionados, generosAdicionadosFlow);
    }

    @FXML
    private void adicionarPlaylist() {
        //playlist - arrumar
        adicionarItem(generoTextField, "playlist", generosSelecionados, generosAdicionadosFlow);
    }


    @FXML
    private void adicionarArtista() {
        adicionarItem(artistasTextField, "artist", artistasSelecionados, artistasAdicionadosFlow);
    }

    @FXML
    private void adicionarAlbum() {
        adicionarItem(albumTextField, "album", albunsSelecionados, albumAdicionadosFlow);
    }

    @FXML
    private void adicionarMusica() {
        adicionarItem(musicaTextField, "track", musicasSelecionadas, musicaAdicionadosFlow);
    }

    // Método utilitário genérico para adicionar elementos
    private void adicionarItem(TextField inputField, String tipoBusca, List<SearchResult> listaDestino, FlowPane painelDestino) {
        String query = inputField.getText().trim();
        if (query.isEmpty()) return;

        List<SearchResult> resultados = SearchManager.search(query, tipoBusca);

        if (!resultados.isEmpty()) {
            SearchResult item = resultados.get(0);

            if (!listaDestino.contains(item)) {
                listaDestino.add(item);

                Label itemLabel = new Label(item.getName());
                itemLabel.setStyle("-fx-background-color: #FF1AA8; -fx-text-fill: white; -fx-padding: 4 8; -fx-background-radius: 12;");
                painelDestino.getChildren().add(itemLabel);
            }

            inputField.clear();
        } else {
            System.out.println("Nenhum resultado encontrado para: " + query);
        }
    }
}
