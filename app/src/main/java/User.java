import java.util.List;

import api.Request;
import fileManager.JsonFileManager;
import fileManager.Storable;

public class User implements Storable {
    private String country;
    private String name;
    private String email;
    private String id;
    private Boolean explicitContentFilter;
    private int followers;
    private List<Image> images;

    public User(String country, String name, String email, String id, Boolean explicitContentFilter, int followers,
            List<Image> images) {
        this.country = country;
        this.name = name;
        this.email = email;
        this.id = id;
        this.explicitContentFilter = explicitContentFilter;
        this.followers = followers;
        this.images = images;
    }

    public void saveData() {
        JsonFileManager.saveJsonFile(this, "User.json");
    }

    public static void main(String[] args) {
        String userJson = null;
        try {
            Request request = new Request();
            userJson = request.sendGetRequest("https://api.spotify.com/v1/me");
            System.out.println(userJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        User user = new User("BR", "nome", "email", "12312", false, 12, null);

        user.saveData();
    }

    public String getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public Boolean getExplicitContentFilter() {
        return explicitContentFilter;
    }

    public int getFollowers() {
        return followers;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setExplicitContentFilter(Boolean explicitContentFilter) {
        this.explicitContentFilter = explicitContentFilter;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
