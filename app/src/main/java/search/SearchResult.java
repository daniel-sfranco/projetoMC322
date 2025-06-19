package search;

public class SearchResult {
    private String name;
    private String id;

    public SearchResult(String name, String id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "SearchResult [name=" + name + ", id=" + id + "]";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
