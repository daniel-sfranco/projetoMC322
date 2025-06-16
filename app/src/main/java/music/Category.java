/*
* Category.java
* 
* Material usado na disciplina MC322 - Programação orientada a objetos.AAA
* 
* A documentação para javadoc deste arquivo foi feita com o uso de IA
* e posteriormente revisada e/ou corrigida.
*/

package music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import api.Json;
import api.Request;
import exceptions.RequestException;
import user.User;

/**
 * Representa uma categoria de música no Spotify, como "Pop" ou "Rock".
 * Uma categoria é identificada por um ID e um nome, e agrupa várias playlists.
 * Implementa a interface {@link MusicSource} por ser uma fonte de conteúdo
 * musical.
 * 
 * @author Vinícius de Oliveira - 251527
 * @author Daniel Soares Franco - 259083
 */
public class Category implements MusicSource {
    private String id;
    private String name;
    private ArrayList<String> playlistsIds;
    private Request request;

    public Category(String categoryId) throws RequestException {
        this.request = User.getInstance().getRequest();
        this.id = categoryId;
        this.playlistsIds = new ArrayList<String>();
        Json categoryData = this.request.sendGetRequest("browse/categories/" + categoryId);
        this.name = categoryData.get("name").toString();
        if (this.name.startsWith("\"") && this.name.endsWith("\"")) {
            this.name = this.name.substring(1, this.name.length() - 1); // Remove aspas
        }

    }

    /**
     * Construtor para criar uma nova instância de Category.
     *
     * @param id           O ID único da categoria no Spotify.
     * @param name         O nome da categoria (e.g., "Pop", "Rock").
     * @param playlistsIds Uma {@code ArrayList} de ids de playlists que fazem parte
     *                     desta categoria.
     */
    public Category(String id, String name, ArrayList<String> playlistsIds) {
        this.request = User.getInstance().getRequest();
        this.id = id;
        this.name = name;
        this.playlistsIds = playlistsIds;
    }

    public void addPlaylists() throws RequestException {
        String query = String.format("search?type=playlist&market=%s&q=%s", User.getInstance().getCountry(),
                this.name.replace(" ", "+"));
        Json playlistsData = new Json(this.request.sendGetRequest(query).get("playlists.items").toString());
        ArrayList<HashMap<String, Object>> playlistsDataList = playlistsData
                .parseJson(new TypeReference<ArrayList<HashMap<String, Object>>>() {
                });
        for (HashMap<String, Object> playlistData : playlistsDataList) {
            if (playlistData == null) {
                continue;
            }
            this.playlistsIds.add(playlistData.get("id").toString());
        }
    }

    /**
     * Retorna o ID único da categoria.
     * Implementação do método {@code getId()} da interface {@link MusicSource}.
     *
     * @return O ID da categoria.
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Retorna o nome da categoria.
     * Implementação do método {@code getName()} da interface {@link MusicSource}.
     *
     * @return O nome da categoria.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retorna a lista de playlists associadas a esta categoria.
     *
     * @return Uma {@code ArrayList} de ids de playlists.
     */
    public ArrayList<String> getPlaylistsIds() {
        if (playlistsIds.isEmpty()) {
            try {
                addPlaylists();
            } catch (RequestException e) {
                System.out.println("Erro ao adicionar playlists da categoria com id " + id);
                System.out.println(e.getMessage());
            }
        }
        return playlistsIds;
    }

    /**
     * Retorna uma representação em string da categoria, incluindo seu ID, nome e
     * os IDs das playlists associadas.
     *
     * @return Uma string representando a categoria.
     */
    @Override
    public ArrayList<String> getTracksIds() {
        if (playlistsIds.isEmpty()) {
            try {
                addPlaylists();
            } catch (RequestException e) {
                System.out.println("Erro ao adicionar playlists da categoria com id " + id);
                System.out.println(e.getMessage());
            }
        }
        ArrayList<String> tracksIds = new ArrayList<String>();

        for (String playlistId : playlistsIds) {
            try {
                Playlist currentPlaylist = new Playlist(playlistId);
                tracksIds.addAll(currentPlaylist.getTracksIds());
            } catch (RequestException e) {
                System.out.println("Eu ao adicionar faixas da playlist com id " + playlistId);
                System.out.println(e.getMessage());
            }
        }

        return tracksIds;
    }

    public static void main(String[] args) throws RequestException, JsonProcessingException {
        ArrayList<Category> categories = new ArrayList<Category>();
        Request request = User.getInstance().getRequest();
        Json categoryList = new Json(request.sendGetRequest("browse/categories").get("categories"));
        Json items = new Json(categoryList.get("items").toString());
        ArrayList<LinkedHashMap<String, Object>> categoriesData = items
                .parseJson(new TypeReference<ArrayList<LinkedHashMap<String, Object>>>() {
                });
        for (LinkedHashMap<String, Object> categoryData : categoriesData) {
            String categoryId = categoryData.get("id").toString();
            try {
                Category category = new Category(categoryId);
                categories.add(category);
                System.out.println("Category: " + category.getName());
            } catch (RequestException e) {
                System.out.println("Erro ao processar a categoria com id " + categoryId);
                System.out.println(e.getMessage());
            }
        }
        Category example = categories.get(0);
        example.addPlaylists();
        for (String playlistId : example.getPlaylistsIds()) {
            System.out.println("Playlist ID: " + playlistId);
        }
    }
}
