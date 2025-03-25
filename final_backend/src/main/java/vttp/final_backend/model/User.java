package vttp.final_backend.model;

import java.util.Arrays;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "userPref")
public class User {

    private String id;

    public void setId(String id) {
        this.id = id;
    }
    @Id
    private String username;

    private String displayName;
    private String[] selectedTopics;

    public String getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public String[] getSelectedTopics() {
        return selectedTopics;
    }
    public void setSelectedTopics(String[] selectedTopics) {
        this.selectedTopics = selectedTopics;
    }
    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", displayName=" + displayName + ", selectedTopics="
                + Arrays.toString(selectedTopics) + "]";
    }
}
