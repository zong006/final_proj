package vttp.final_backend.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import vttp.final_backend.model.User;
import vttp.final_backend.repo.RedisRepo;
import vttp.final_backend.repo.UserRepo;
import vttp.final_backend.repo.UserSqlRepo;
import vttp.final_backend.utils.Utils;

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserSqlRepo userSqlRepo;

    @Autowired
    private RedisRepo redisRepo;


    public User loginUser(User user, String uid){
        userSqlRepo.loginUser(user, uid);

        if (!redisRepo.userExistsInCache(user.getUsername())){ // <-- new user. add to the cache
            redisRepo.updateUserScore(user.getUsername(), 0);
            // assign a new user id to user
            String userId = UUID.randomUUID().toString().substring(0,8);
            user.setId(userId);
            redisRepo.enterUserIdEmail(userId, user.getUsername());
        }
        redisRepo.updateCurrentUseTime(user.getUsername());
        

        return userRepo.loginUser(user);
    }

    public long updateUserPref(User user){
        List<String> topicsArray = Arrays.asList(user.getSelectedTopics());
        StringBuilder sb = new StringBuilder();

        topicsArray.forEach(x -> {
            sb.append(x);
            sb.append(Utils.delimiter);
        });

        if (sb.length() > 0) {
            sb.setLength(sb.length() - Utils.delimiter.length()); 
        }
        
        redisRepo.addPrefToHash(user.getId(), sb.toString());
        

        return userRepo.updateUserPref(user);
    }

    // method to run every hour to update user activity

    @Scheduled(fixedRate = 3600000) // <-- schedule to run every hour to sync redis to sql 
    public void syncRedisUserActivityToSql() {
        Map<Object, Object> activities = redisRepo.getDateTime();
        // redisTemplate.opsForHash().entries("user_scores");

        for (Map.Entry<Object, Object> entry : activities.entrySet()) {
            String email = (String) entry.getKey();
            String latestUse = (String) entry.getValue();

            userSqlRepo.updateLoginActivity(email, latestUse);
        }

        System.out.println("synced redis cahce (activity) scores to sql");
    }


    // method to run every 24 hours to check if user has logged in within the past 24 hours

    @Scheduled(fixedRate = 86400000) // <-- schedule to run every 24hours to remind users to doomscroll
    public void getListOfInactiveUsers() {

        List<String> inactiveEmails = userSqlRepo.getEmailsOfInactiveUsers();

        // send email to users

        // get the telegram chatId corresponding to these emails, and ping them on tele

    }
}
