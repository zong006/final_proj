package vttp.final_backend.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vttp.final_backend.model.Article;
import vttp.final_backend.repo.RedisRepo;
import vttp.final_backend.repo.UserRepo;

@Service
public class TelegramService {
    
    @Value("${telegram_token}")
    private String BOT_TOKEN;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RedisRepo redisRepo;

    @Autowired
    private ArticleService articleService;
    
    @SuppressWarnings("unchecked")
    public void processUpdate(Map<String, Object> update) throws IOException, ParseException {
        System.out.println("telegram service..." );
        if (update.containsKey("message")) {
            System.out.println(update);

            Map<String, Object> message = (Map<String, Object>) update.get("message");
            String text = (String) message.get("text");  
            Integer chatIdInt = (Integer) ((Map<String, Object>) message.get("chat")).get("id"); 
            // System.out.println("User " + chatId + " sent: " + text);
            String userId;
            String chatId = Integer.toString(chatIdInt);

            if (text.startsWith("/start") && !redisRepo.isChatIdExists(chatId)){ // <-- first time using telegram to scroll. user should be registered in website
                userId = text.split(" ")[1]; // <-- get the userId from registering from website
                redisRepo.addChatIdToHash(chatId, userId); // <-- link the userId to the telegram chatId
                // System.out.println("added to redis hash");
                userRepo.saveTelegramChatId(userId, chatId);
                redisRepo.resetPage(chatId); // <-- set page to 1
                // System.out.println("added to mongo");
            }
            // logic to feed articles to the user according to preferences
            
            String page = redisRepo.getPage(chatId);
            if (page==null){
                page = "1";
            }

            userId = redisRepo.getUserIdFromChatIdHaSh(chatId);

            if (text.equals("/break")){
                // direct to some page to feel relaxed
                sendMessage(chatIdInt, " Feeling down? Click on this link to feel relaxed.");
                sendMessage(chatIdInt, "https://i.pinimg.com/736x/ba/b2/ff/bab2ff09ecdfb271d0e69c33e71053b8.jpg");
                sendMessage(chatIdInt, " Come back and continue doomscrolling when you feel better! \\uD83D\\uDE0A");
            }   
            else { // <--- also handle the case where user enters netiher of the choices. for some reason..
                
                
                if (!text.equals("/more") && !text.equals("/begin")){
                    sendMessage(chatIdInt, "Unrecognized command \\uD83D\\uDE44 ");
                }
                else if (text.equals("/begin")){
                    // System.out.println("begin");
                    redisRepo.resetPage(chatId);
                    page = "1";
                }
                sendMessage(chatIdInt, "Enjoy!! \uD83D\uDE0A\uD83C\uDF89");
                List<Article> articles = articleService.collateArticles(userId, page);
                articles.forEach(x -> sendArticleToTelegram(chatIdInt, x));

                // update last use time
                String email = redisRepo.getEmailFromUserId(userId);
                redisRepo.updateCurrentUseTime(email);

                redisRepo.incrementPage(chatId);
                // System.out.println("after increment: " + page);
            }  
        }
    }

    private void sendMessage(Integer chatId, String text) {
        String TELEGRAM_API_URL = "https://api.telegram.org/bot" + BOT_TOKEN;
        String url = TELEGRAM_API_URL + "/sendMessage?chat_id=" + chatId + "&text=" + text;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getForObject(url, String.class);
    }

    private void sendArticleToTelegram(Integer chatId, Article article) {
        String TELEGRAM_API_URL = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendPhoto";;

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date(article.getDate());
        String dateString = sdf.format(date);

        String messageText = String.format(
            "%s \n\nSection: %s\nDate: %s",
            article.getTitle(),
            article.getSection(),
            dateString  
        );

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("chat_id", chatId);
        requestBody.put("photo", article.getImageUrl());
        requestBody.put("caption", messageText);

        Map<String, String> button = new HashMap<>();
        button.put("text", "Read Article");
        button.put("url", article.getUrl());

        List<List<Map<String, String>>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(button));

        Map<String, Object> replyMarkup = new HashMap<>();
        replyMarkup.put("inline_keyboard", keyboard);

        requestBody.put("reply_markup", replyMarkup);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        restTemplate.postForObject(TELEGRAM_API_URL, request, String.class);
    }
}
