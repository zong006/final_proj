package vttp.final_backend.restcontroller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import vttp.final_backend.model.User;
import vttp.final_backend.service.SendEmailService;
import vttp.final_backend.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user, @RequestHeader("Authorization") String idToken){
    

        System.out.println(user.toString());
        System.out.println(">>> header: " + idToken.toString());

        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);

            String id = decodedToken.getUid();
            // String email = decodedToken.getEmail();
            // String displayName = decodedToken.getName();
            
            // System.out.println(id + " " + email + " " + displayName);
            User response = userService.loginUser(user, id);

            return ResponseEntity.ok().body(response); 
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Authentication Failed");
            errorResponse.put("details", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, String>> updateUserPref(@RequestBody User user, @PathVariable String id){
        System.out.println(user.toString());
        System.out.println(id);
        
        Map<String, String> response = new HashMap<>();
        response.put(id, Long.toString(userService.updateUserPref(user)));
        return ResponseEntity.ok().body(response);
    }

    @Autowired
    private SendEmailService emailService;
    @GetMapping("/send-email")
    public String sendEmail() {
        String response = emailService.sendEmail("ongzhicong@gmail.com", "Test Subject", "This is a test email.");
        
        return response.equals("Success")? "Email sent successfully!" : "failed";
    }

}
