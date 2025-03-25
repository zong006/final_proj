package vttp.final_backend.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import vttp.final_backend.repo.LeaderboardRepo;
import vttp.final_backend.repo.RedisRepo;

@Service
public class LeaderBoardService {

    @Autowired
    private LeaderboardRepo leaderboardRepo;

    @Autowired
    private RedisRepo redisRepo;

    // probably no need
    public void pushScoresToRedis(){
        Map<String, Integer> scores = leaderboardRepo.getAllScores();
        Map<String, String> formattedScores = new HashMap<>();

        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            String key = entry.getKey();
            String value = Integer.toString(entry.getValue());
            formattedScores.put(key, value);
        }
        redisRepo.saveHourlyScore(formattedScores);
    } 

    public Map<String, Integer> getHourlyScoreCache(){
        Map<Object, Object> scores = redisRepo.getHourlyScore();
        Map<String, Integer> formattedScores = new HashMap<>();

        for (Map.Entry<Object, Object> entry : scores.entrySet()) {
            String key = entry.getKey().toString();
            Integer value = Integer.parseInt(entry.getValue().toString());
            formattedScores.put(key, value);
        }
        return formattedScores;
    }

    public String getUserScore(String email){
        
        int s = redisRepo.getUserScore(email);
        return Integer.toString(s);
    }

    public void updateUserScore(String email, int score){
        redisRepo.updateUserScore(email, score);
    }

    @Scheduled(fixedRate = 3600000) // <-- schedule to run every hour to sync redis to sql 
    // @Scheduled(fixedRate = 300000) // <-- for testing every 5 min
    public void syncRedisToSql() {
        Map<Object, Object> scores = redisRepo.getHourlyScore();
        // redisTemplate.opsForHash().entries("user_scores");

        for (Map.Entry<Object, Object> entry : scores.entrySet()) {
            String email = (String) entry.getKey();
            String score = (String) entry.getValue();

            leaderboardRepo.updateScore(email, Integer.parseInt(score));
        }

        System.out.println("synced redis cahce (leaderboard) scores to sql");
    }


}
