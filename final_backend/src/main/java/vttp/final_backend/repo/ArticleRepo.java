package vttp.final_backend.repo;

import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRepo {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    private String SECTION_COLLECTION = "sections";
    private String USER_PREF_COLLECTION = "userPref";

    public void saveSections(Map<String, String> sections){
        Document d = new Document(sections);
        long count = mongoTemplate.getCollection(SECTION_COLLECTION).countDocuments();
        if (count == 0) {
            mongoTemplate.insert(d, SECTION_COLLECTION);
        }   
    }

    // db.getCollection("sections").find({}, {"_id":0})
    public List<Document> getSections(){
        Query query = new Query();
        query.fields().exclude("_id");
        List<Document> result = mongoTemplate.find(query, Document.class, SECTION_COLLECTION);
        return result;
    }

    /*
        db.userPref.find({"id" : << userID >>})
                    .projection({"selectedTopics":1, "id":1, "_id":0})
    */ 
    public List<Document> getUserPref(String userID){
        Query query = Query.query(Criteria.where("id").is(userID));
        query.fields().exclude("_id").include("selectedTopics", "id");

        List<Document> results = mongoTemplate.find(query, Document.class, USER_PREF_COLLECTION);

        return results;
    }
}
