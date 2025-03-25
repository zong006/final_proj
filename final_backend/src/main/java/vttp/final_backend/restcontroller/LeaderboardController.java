package vttp.final_backend.restcontroller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.final_backend.service.LeaderBoardService;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {
    
    @Autowired
    private LeaderBoardService leaderBoardService;

    @GetMapping("/get")
    public ResponseEntity<Map<String, Integer>> getHourlyScores(){
        // leaderBoardService.pushScoresToRedis();
        
        Map<String, Integer> scores = leaderBoardService.getHourlyScoreCache();
        System.out.println(scores);
        return ResponseEntity.ok().body(scores);
    }

    @GetMapping("/{email}")
    public ResponseEntity<String> getScore(@PathVariable String email){
        
        return ResponseEntity.ok().body(leaderBoardService.getUserScore(email));
    }

    @PostMapping("/update/{email}")
    public ResponseEntity<String> updateScore(@PathVariable String email, @RequestBody int score){
        leaderBoardService.updateUserScore(email, score);

        JsonObject response = Json.createObjectBuilder().add("mesage", "updated: " + email + "score: " + score).build();
        
        return ResponseEntity.ok().body(response.toString());
    }
}
