package vttp.final_backend.repo;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


import vttp.final_backend.model.User;

@Repository
public class UserRepo {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    private final String USER_COLLECTION = "userPref";
    
    /*
        db.userPref.updateOne(
            {"_id" : << username >>},
            {
                $set: {
                    "selectedTopics" :  [ array of topics to be added ]
                }
            }
        )
    */ 
    public long updateUserPref(User user){
        Query query = Query.query(Criteria.where("_id").is(user.getUsername()));
        Update update = new Update().set("selectedTopics", user.getSelectedTopics());

        return mongoTemplate.updateMulti(query, update, USER_COLLECTION).getModifiedCount() ;
    }


    public User loginUser(User user){
        
        List<User> result = getUser(user);
        if (result.size()==1){ // <-- user exists
            System.out.println("user exists");
            return result.getFirst();
        }
        else{
            System.out.println("new user");
            
            mongoTemplate.insert(user, USER_COLLECTION);
            return user;
        }  
    }

    /*
        db.getCollection("userPref").find({
            "_id" : << username >>
        })

    */ 
    private List<User> getUser(User user){
        String username = user.getUsername();
        Query query = Query.query(Criteria.where("_id").is(username));

        return mongoTemplate.find(query, User.class, USER_COLLECTION);
        
    }

    /*
        db.getCollection("userPref").updateOne(
            {
                "id" : << userId >>
            },
            {
                $set : { "teleId" : << chatId >> } 
            }
        )
    */ 
    public void saveTelegramChatId(String userId, String chatId){
        Query query = Query.query(Criteria.where("id").is(userId));

        Update update = new Update().set("teleId", chatId);

        mongoTemplate.updateMulti(query, update, Document.class, USER_COLLECTION);
    }
    
}
