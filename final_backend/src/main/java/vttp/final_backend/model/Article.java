package vttp.final_backend.model;



import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;

public class Article {
    // private String id;
    private String title;
    private String url;
    private String section;
    private Long date;
    private String imageUrl;

    public String getUrl() {
        return url;
    }



    public void setUrl(String url) {
        this.url = url;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }



    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getTitle() {
        return title;
    }



    public void setTitle(String title) {
        this.title = title;
    }



    public String getSection() {
        return section;
    }



    public void setSection(String section) {
        this.section = section;
    }



    public Long getDate() {
        return date;
    }



    public void setDate(Long date) {
        this.date = date;
    }


    public JsonObject toJson(){
        JsonObjectBuilder job = Json.createObjectBuilder()
                                    // .add("id", getId())
                                    .add("title", getTitle())
                                    .add("url", getUrl())
                                    .add("section", getSection())
                                    .add("date", getDate())
                                    .add("imageUrl", getImageUrl());
                                    // .add("sectionId", getSectionId())
                                    // .add("pages", getPages());
        return job.build();
    }
}
