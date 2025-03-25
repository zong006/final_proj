package vttp.final_backend.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
    
    @PostConstruct
    public void initializeSDK() throws IOException{
        InputStream is = getClass().getClassLoader().getResourceAsStream("vttp-final-proj-firebase-adminsdk-fbsvc-6c1004315d.json");
        FileInputStream fis = new FileInputStream("./src/main/resources/vttp-final-proj-firebase-adminsdk-fbsvc-6c1004315d.json");
        FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(is)).build();
        FirebaseApp.initializeApp(options);
    }

}
