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

import com.fasterxml.jackson.core.JsonProcessingException;

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
    private ArrayList<Playlist> playlists;
    private Request request;

    public Category(String categoryId) throws RequestException {
        this.request = User.getInstance().getRequest();
        this.id = categoryId.toString().replaceAll("\"", "");
        this.playlists = new ArrayList<Playlist>();
        Json categoryData = this.request.sendGetRequest("browse/categories/" + this.id);
        this.name = categoryData.get("name").toString().replaceAll("\"", "");
    }

    /**
     * Construtor para criar uma nova instância de Category.
     *
     * @param id           O ID único da categoria no Spotify.
     * @param name         O nome da categoria (e.g., "Pop", "Rock").
     * @param playlistsIds Uma {@code ArrayList} de ids de playlists que fazem parte
     *                     desta categoria.
     */
    public Category(String id, String name, ArrayList<Playlist> playlists) {
        this.request = User.getInstance().getRequest();
        this.id = id;
        this.name = name;
        this.playlists = playlists;
    }

    public Category(String id, String name){
        this.request = User.getInstance().getRequest();
        this.id = id;
        this.name = name;
        this.playlists = new ArrayList<>();
    }

    public void addPlaylists() throws RequestException {
        String query = String.format("search?type=playlist&market=%s&q=%s", User.getInstance().getCountry(),
                this.name.replace(" ", "+"));
        Json playlistsData = new Json(this.request.sendGetRequest(query).get("playlists.items").toString());
        for (Json playlistData : playlistsData.parseJsonArray()) {
            if (playlistData == null || playlistData.get("id") == null) {
                continue;
            }
            this.playlists.add(new Playlist(playlistData.get("id").toString().replaceAll("\"", "")));
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
    public ArrayList<Playlist> getPlaylists() {
        if (playlists.isEmpty()) {
            try {
                addPlaylists();
            } catch (RequestException e) {
                System.out.println("Erro ao adicionar playlists da categoria com id " + id);
                System.out.println(e.getMessage());
            }
        }
        return playlists;
    }

    /**
     * Retorna uma representação em string da categoria, incluindo seu ID, nome e
     * os IDs das playlists associadas.
     *
     * @return Uma string representando a categoria.
     */
    @Override
    public ArrayList<Track> getTracks() {
        if (playlists.isEmpty()) {
            try {
                addPlaylists();
            } catch (RequestException e) {
                System.out.println("Erro ao adicionar playlists da categoria com id " + id);
                System.out.println(e.getMessage());
            }
        }
        ArrayList<Track> tracks = new ArrayList<Track>();
        for (Playlist playlist : playlists) {
            tracks.addAll(playlist.getTracks());
        }
        return tracks;
    }

    @Override
    public String toString() {
        return "\nCategory [id=" + id + ", name=" + name + ", playlists=" + playlists + "]";
    }

    public static void main(String[] args) throws RequestException, JsonProcessingException {
        ArrayList<Category> categories = new ArrayList<Category>();
        Request request = User.getInstance().getRequest();
        Json categoryList = request.sendGetRequest("browse/categories?locale=pt_" + User.getInstance().getCountry()).get("categories");
        Json items = new Json(categoryList.get("items").toString());
        ArrayList<Json> categoriesData = items.parseJsonArray();
        for (Json categoryData : categoriesData) {
            if(categories.size() == 3) break;
            String categoryId = categoryData.get("id").toString();
            try {
                Category category = new Category(categoryId);
                categories.add(category);
                System.out.print(category);
            } catch (RequestException e) {
                System.out.println("Erro ao processar a categoria com id " + categoryId);
                System.out.println(e.getMessage());
            }
        }
        Category example = categories.get(0);
        example.addPlaylists();
        System.out.println(example);
    }
}
