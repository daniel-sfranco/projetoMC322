package projetofinal.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import music.Playlist;
import projetofinal.App;
import search.AlbumResearcher;
import search.ArtistResearcher;
import search.GenreResearcher;
import search.PlaylistResearcher;
import search.Researcher;
import search.SearchManager;
import search.SearchResult;
import search.TrackResearcher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller para a tela principal da aplicação, onde será possível escolher as
 * opções para criação da playlist
 */
public class PlaylistController {
    @FXML
    private TextField numeroMusicasTextField;

    @FXML
    private TextField generoTextField;
    @FXML
    private FlowPane generosAdicionadosFlow;

    @FXML
    private TextField playlistTextField;
    @FXML
    private FlowPane playlistAdicionadosFlow;

    @FXML
    private TextField artistasTextField;
    @FXML
    private FlowPane artistasAdicionadosFlow;

    @FXML
    private TextField albumTextField;
    @FXML
    private FlowPane albumAdicionadosFlow;

    @FXML
    private TextField musicaTextField;
    @FXML
    private FlowPane musicaAdicionadosFlow;

    private List<SearchResult> artistasSelecionados = new ArrayList<>();
    private List<SearchResult> albunsSelecionados = new ArrayList<>();
    private List<SearchResult> musicasSelecionadas = new ArrayList<>();
    private StringBuilder generosSelecionados = new StringBuilder();
    private StringBuilder playlistSelecionadas = new StringBuilder();
    private String playlistSelecionadaId = null;

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
    private Label mensagemErroLabel;

    @FXML
    private void GerarPlaylist() {
        try {
            mensagemErroLabel.setVisible(false);
            // 1. Pega número de músicas (com validação simples)
            int numeroDeMusicas = 0;
            String texto = numeroMusicasTextField.getText().trim();

            if (!texto.isEmpty()) {
                numeroDeMusicas = Integer.parseInt(texto);
            } else {
                mensagemErroLabel.setText("Insira um número de músicas");
                mensagemErroLabel.setVisible(true);
                return;
            }

            // 2. Começa a construção
            Playlist.PlaylistBuilder builder = Playlist.builder(numeroDeMusicas);

            // 3. Adiciona valores se existirem
            if (generosSelecionados.length() > 0) {
                builder = builder.addGenre(generosSelecionados.toString());
            }

            if (playlistSelecionadaId != null) {
                if (playlistSelecionadaId.length() > 0) {
                    builder = builder.addPlaylist(playlistSelecionadaId.toString());
                }
            }

            for (SearchResult artista : artistasSelecionados) {
                builder = builder.addArtist(new ArrayList<>(List.of(artista.getId())));
            }

            for (SearchResult album : albunsSelecionados) {
                builder = builder.addAlbum(new ArrayList<>(List.of(album.getId())));
            }

            for (SearchResult musica : musicasSelecionadas) {
                builder = builder.addTrack(new ArrayList<>(List.of(musica.getId())));
            }

            // 4. Finaliza a construção
            Playlist novaPlaylist = builder.build();
            System.out.println("Playlist criada com sucesso: " + novaPlaylist);
            App.setRoot("visualizarPlaylists");
        } catch (Exception e) {
            mensagemErroLabel.setText("Erro ao gerar playlist: " + e.getMessage());
            mensagemErroLabel.setVisible(true);
            e.printStackTrace();
        }
    }

    // Métodos de adição
    @FXML
    private void adicionarGenero() {
        adicionarItem(generoTextField, new GenreResearcher(), generosSelecionados, generosAdicionadosFlow);
    }

    @FXML
    private void adicionarPlaylist() {
        adicionarItem(playlistTextField, new PlaylistResearcher(), playlistSelecionadas, playlistAdicionadosFlow);

    }

    @FXML
    private void adicionarArtista() {
        adicionarItem(artistasTextField, new ArtistResearcher(), artistasSelecionados, artistasAdicionadosFlow);
    }

    @FXML
    private void adicionarAlbum() {
        adicionarItem(albumTextField, new AlbumResearcher(), albunsSelecionados, albumAdicionadosFlow);
    }

    @FXML
    private void adicionarMusica() {
        adicionarItem(musicaTextField, new TrackResearcher(), musicasSelecionadas, musicaAdicionadosFlow);
    }

    // Método utilitário genérico para adicionar elementos
    private void adicionarItem(TextField inputField, Researcher tipoBusca, Object destino, FlowPane painelDestino) {
        String query = inputField.getText().trim();
        if (query.isEmpty())
            return;

        mensagemErroLabel.setVisible(false);
        List<SearchResult> resultados = SearchManager.search(query, tipoBusca);

        if (!resultados.isEmpty()) {
            SearchResult item = resultados.get(0);

            if (destino instanceof List) {
                @SuppressWarnings("unchecked")
                List<SearchResult> lista = (List<SearchResult>) destino;

                if (!lista.contains(item)) {
                    lista.add(item);
                }
            } else if (destino instanceof StringBuilder) {
                // Só aceita um único valor por vez
                painelDestino.getChildren().clear(); // limpa o antigo visualmente

                ((StringBuilder) destino).setLength(0); // limpa o valor antigo
                ((StringBuilder) destino).append(item.getName());

                if (tipoBusca instanceof PlaylistResearcher) {
                    playlistSelecionadaId = item.getId(); // salva só o ID aqui

                }
            }
            adicionarItemComRemocao(destino, painelDestino, item);

            inputField.clear();
        } else {
            mensagemErroLabel.setText("Nenhum resultado encontrado para: " + query);
            mensagemErroLabel.setVisible(true);
        }
    }

    private void adicionarItemComRemocao(Object destino, FlowPane flowPane, SearchResult item) {
        // Cria label com nome + botão "x"
        Label label = new Label(item.getName());
        Button botaoRemover = new Button("x");

        botaoRemover.setStyle(
                "-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 0 4;");
        label.setStyle("-fx-text-fill: white;"); // Removemos o fundo da label

        HBox container = new HBox(label, botaoRemover);
        container.setSpacing(5);
        container.setStyle("""
                    -fx-background-color: #FF1AA8;
                    -fx-text-fill: white;
                    -fx-padding: 4 8;
                    -fx-background-radius: 12;
                    -fx-alignment: center;
                """);

        // Adiciona ao FlowPane
        flowPane.getChildren().add(container);

        // Remoção no clique
        botaoRemover.setOnAction(e -> {
            flowPane.getChildren().remove(container);

            if (destino instanceof List<?>) {
                ((List<?>) destino).remove(item);
            } else if (destino instanceof StringBuilder) {
                ((StringBuilder) destino).setLength(0); // limpa se for único
            }
        });
    }

}
