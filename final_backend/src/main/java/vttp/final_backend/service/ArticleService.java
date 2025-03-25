package vttp.final_backend.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.final_backend.model.Article;
import vttp.final_backend.model.Section;
import vttp.final_backend.repo.ArticleRepo;
import vttp.final_backend.repo.RedisRepo;
import vttp.final_backend.utils.Utils;

@Service
public class ArticleService {
    
    @Value("${api_key}") 
    private String api_key;

    @Value("${python_api}") 
    private String python_api_url;

    @Autowired
    private ArticleRepo articleRepo;

    @Autowired
    private RedisRepo redisChatIdRepo;
    
    private Set<String> excludedTopics = Utils.excludedTopics;

    @PostConstruct
    public void init(){
        Map<String, String> sections = getSections();
        articleRepo.saveSections(sections);
    }

    public List<Article> collateArticles(String userID, String page) throws IOException, ParseException{
        List<String> selectedTopics = getUserPref(userID);
        List<Article> collatedArticles = new LinkedList<>();
        String pageSize = Integer.toString(Utils.articlesPerLoad / selectedTopics.size());
        
        if (selectedTopics.size()>0 && !selectedTopics.get(0).equals("")){
            System.out.println("not empty");
            String p = pageSize;
            selectedTopics.forEach(topic -> {
                    try {
                        List<Article> topicArticles = getArticlesByTopic(topic, p, page);
                        collatedArticles.addAll(topicArticles);
                    } catch (IOException | ParseException e) {
                        e.printStackTrace();
                    }  
            });
        }
        else {
            pageSize = Integer.toString(10);
            collatedArticles.addAll(getArticlesByTopic("empty", pageSize, page));
        }
        
        collatedArticles.sort(Comparator.comparingLong(Article::getDate).reversed());  
        return filteredArticles(collatedArticles);
    }

    public List<Article> getArticlesByTopic(String topic, String pageSize, String page) throws IOException, ParseException{
        
        String url = Utils.newsUrl + Utils.newsSearchQuery + Utils.negativeSearch 
                        + Utils.newsApiEntry + api_key 
                        + Utils.showThumbnail
                        + Utils.newsPageSizeEntry + pageSize
                        + Utils.newsPageEntry + page
                        + Utils.sortbyNewest;

        if (!topic.equals("empty")){
            url +=   Utils.setSection + topic;
        }
        

        List<Article> articles = new ArrayList<>();

        JsonReader jsonReader = generateJson(url);
        JsonObject jsonData = jsonReader.readObject();
        JsonObject response = jsonData.getJsonObject("response");
        JsonArray results = response.getJsonArray("results");

        for (int i=0 ; i<results.size() ; i++){

            JsonObject x = results.getJsonObject(i);            
            Article article = new Article();
            article.setTitle(x.getString("webTitle"));
            article.setSection(x.getString("sectionName"));
            article.setUrl(x.getString("webUrl"));
            
            String thumbnailUrl;
            try {
                thumbnailUrl = x.getJsonObject("fields").getString("thumbnail");
            } catch (NullPointerException e) {
                thumbnailUrl = Utils.defaultThumbnailUrl;
            }
            article.setImageUrl(thumbnailUrl);

            String dateString = x.getString("webPublicationDate");
            Long dateTime = convertDateToLong(dateString);
            article.setDate(dateTime);

            articles.add(article);
        }
        return articles;
    }

    private List<String> getUserPref(String userID){
        
        // should get from redis to not have to always query mongo
        
        return redisChatIdRepo.getPrefFromHash(userID);
        // List<String> selectedTopics = new LinkedList<>();
        // Document result = articleRepo.getUserPref(userID).getFirst();
        // result.getList("selectedTopics", String.class).forEach(x -> selectedTopics.add(x));
        // return selectedTopics;
    }

    public List<Section> getSectionsFromMongo(){
        Document d =  articleRepo.getSections().getFirst();
        List<Section> sections = new LinkedList<>();

        d.forEach((key, value) -> {
            Section s = new Section();
            s.setWebTitle(key);
            s.setId(value.toString());
            sections.add(s);
        });

        return sections;
    }

    private Map<String, String> getSections(){

        String url = Utils.newsUrl + Utils.newsSectionQuery + Utils.newsApiEntry + api_key;
        
        JsonReader jsonReader = generateJson(url);
        JsonObject jsonData = jsonReader.readObject();
        JsonObject response = jsonData.getJsonObject("response");
        JsonArray results = response.getJsonArray("results");

        Map<String, String> sections = new LinkedHashMap<>();
        for (int i=1 ; i<results.size() ; i++){ // first entry is always "about" page of guardian
            JsonObject entry = results.getJsonObject(i);
            if (!excludedTopics.contains(entry.getString("id"))){
                sections.put(entry.getString("webTitle") ,entry.getString("id")); // value(id) is the one being saved 
            }    
        }
        return sections;
    }

    private List<Article> filteredArticles(List<Article> articles){
        JsonArrayBuilder jab = Json.createArrayBuilder();
        
        articles.forEach(x -> jab.add(x.getTitle()));
        JsonArray jsonArray = jab.build();

        JsonObject resposne =  generateJson(python_api_url, jsonArray).readObject().asJsonObject();
        JsonArray sentiments = resposne.getJsonArray("sentiments");

        List<Article> filteredArticles = new LinkedList<>();
        for (int i=0 ; i<articles.size() ; i++){
            if (sentiments.get(i).equals(JsonValue.TRUE)){
                // System.out.println(">>> filtered true: " + articles.get(i).getTitle());
                filteredArticles.add(articles.get(i));
            }
        }

        return filteredArticles;
    }

    private JsonReader generateJson(String url){
        RestTemplate restTemplate = new RestTemplate();
        System.out.println(">>>> current serach url: " + url);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String respBody = responseEntity.getBody();
        InputStream is = new ByteArrayInputStream(respBody.getBytes());
        JsonReader jsonReader = Json.createReader(is);
        return jsonReader;
    }

    private JsonReader generateJson(String url, JsonArray jsonArray){
        RestTemplate restTemplate = new RestTemplate();

        RequestEntity<String> req = RequestEntity.post(url).contentType(MediaType.APPLICATION_JSON).body(jsonArray.toString(), String.class);
        ResponseEntity<String> response = restTemplate.exchange(req, String.class);
        String respBody = response.getBody();
        InputStream is = new ByteArrayInputStream(respBody.getBytes());
        JsonReader jsonReader = Json.createReader(is);
        return jsonReader;
    }

    private Long convertDateToLong(String dateString) throws ParseException{

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = sdf.parse(dateString);
        return date.getTime();
    }
}
