public class Image {
    private String url;
    private int height;
    private int width;

    public Image(String url, int height, int width){
        this.url = url;
        this.height = height;
        this.width = width;
    }

    public String getUrl(){
        return url;
    }

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }
}
