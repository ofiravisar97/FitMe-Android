package me.ofir.fitme.Entites;

public class News {
    private String title;
    private String description;
    private String Url;
    private String imageUrl;

    public News(String title, String description, String url,String image) {
        this.title = title;
        this.description = description;
        Url = url;
        imageUrl = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl){this.imageUrl = imageUrl;}

    public String getImageUrl(){return imageUrl;}

    public News() {
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
