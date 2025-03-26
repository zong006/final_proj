package vttp.final_backend.repo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import vttp.final_backend.utils.Utils;

@Repository
public class RedisRepo {
    
    @Autowired
    private StringRedisTemplate redisTemplate;
// ==================================================================================================== //
    private final String REDIS_HASH_CHATID = "chatId";

    public void addChatIdToHash(String chatId, String userId) {
        redisTemplate.opsForHash().put(REDIS_HASH_CHATID, chatId, userId);
    }
    
    public boolean isChatIdExists(String chatId) {
        return redisTemplate.opsForHash().hasKey(REDIS_HASH_CHATID, chatId);
    }

    public void removeChatIdFromHash(String chatId) {
        redisTemplate.opsForHash().delete(REDIS_HASH_CHATID, chatId);
    }

    public String getUserIdFromChatIdHaSh(String chatId){
        return (String) redisTemplate.opsForHash().get(REDIS_HASH_CHATID, chatId);
    }
// ==================================================================================================== //

    private final String REDIS_HASH_USER_PREF = "userPref";

    public void addPrefToHash(String userId, String value){
        redisTemplate.opsForHash().put(REDIS_HASH_USER_PREF, userId, value);
    }

    public List<String> getPrefFromHash(String userId){
        String value = (String) redisTemplate.opsForHash().get(REDIS_HASH_USER_PREF, userId);
        
        return (value!=null)? Arrays.asList(value.split(Utils.delimiter)) : Collections.emptyList();
    }

    public boolean PrefHashHasKey(String userId){
        return redisTemplate.opsForHash().hasKey(REDIS_HASH_CHATID, userId);
    }
// ==================================================================================================== //

    private final String REDIS_HASH_TELE_PAGE = "telePage";

    public String getPage(String chatId){
        return (String) redisTemplate.opsForHash().get(REDIS_HASH_TELE_PAGE, chatId);
    }

    public void resetPage(String chatId){
        redisTemplate.opsForHash().put(REDIS_HASH_TELE_PAGE, chatId, "1");
    }

    public void incrementPage(String chatId){
        redisTemplate.opsForHash().increment(REDIS_HASH_TELE_PAGE, chatId, 1);
    }
// ==================================================================================================== //

    private final String REDIS_HASH_HOURLY_SCORE = "hourlyScoreCache";

    public void saveHourlyScore(Map<String, String> hourlyScores){
        /* 
            for sql to populate the redis hashmap of (email, score)
        */
        
        redisTemplate.opsForHash().putAll(REDIS_HASH_HOURLY_SCORE, hourlyScores);
    }

    public Map<Object, Object> getHourlyScore(){
        /*
            to get scores for the frontend
        */ 
        return redisTemplate.opsForHash().entries(REDIS_HASH_HOURLY_SCORE);
    }

    public int getUserScore(String email){
        String s = (String) redisTemplate.opsForHash().get(REDIS_HASH_HOURLY_SCORE, email);
        return Integer.parseInt(s);
    }

    public void updateUserScore(String email, int score){
        redisTemplate.opsForHash().put(REDIS_HASH_HOURLY_SCORE, email, Integer.toString(score));
    }

    public boolean userExistsInCache(String email){
        return redisTemplate.opsForHash().hasKey(REDIS_HASH_HOURLY_SCORE, email);
    }
// ==================================================================================================== //

    private final String REDIS_HASH_USER_ACTIVITY = "userActivity";

    public void updateCurrentUseTime(String email){ // <-- telegram usage should trigger this as well

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime =  LocalDateTime.now().format(formatter);

        redisTemplate.opsForHash().put(REDIS_HASH_USER_ACTIVITY, email, dateTime);
    }   

    public Map<Object, Object> getDateTime(){
        return redisTemplate.opsForHash().entries(REDIS_HASH_USER_ACTIVITY);
    }

// ==================================================================================================== //

    private final String REDIS_HASH_USERID_EMAIL = "userIdToEmail";

    public void enterUserIdEmail(String userId, String email){
        redisTemplate.opsForHash().put(REDIS_HASH_USERID_EMAIL, userId, email);
    }

    public String getEmailFromUserId(String userId){
        return (String) redisTemplate.opsForHash().get(REDIS_HASH_USERID_EMAIL, userId);
    }

    
}
