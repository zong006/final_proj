package vttp.final_backend.config;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {
    

    @PostConstruct
    public void initializeSDK() throws IOException{
        // String workingDir = System.getProperty("user.dir");
        // System.out.println(">>>> workingdir: " + workingDir);
        
    
        String firebaseCredentialsJson = System.getenv("FIREBASE_JSON");
        InputStream inputStream = new ByteArrayInputStream(firebaseCredentialsJson.getBytes(StandardCharsets.UTF_8));
        FirebaseOptions options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(inputStream)).build();
        FirebaseApp.initializeApp(options);

        // FileInputStream fis = new FileInputStream("./src/main/resources/vttp-final-proj-firebase-adminsdk-fbsvc-6c1004315d.json");
        //     options = FirebaseOptions.builder().setCredentials(GoogleCredentials.fromStream(fis)).build();
        //     FirebaseApp.initializeApp(options);
    }

}
