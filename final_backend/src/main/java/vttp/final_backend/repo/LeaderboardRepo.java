package vttp.final_backend.repo;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

@Repository
public class LeaderboardRepo {
    
    @Autowired
    private JdbcTemplate sqlTemplate;

    public Map<String, Integer> getAllScores(){
        
        String SQL_ALL_SCORES = """
                select email, score from user_activity ua
                inner join user_details ud 
                on ua.uid = ud.uid 
                ;
                """;

        Map<String, Integer> scores = new LinkedHashMap<>();
        SqlRowSet rs = sqlTemplate.queryForRowSet(SQL_ALL_SCORES);
        while (rs.next()){
            scores.put(rs.getString("email"), rs.getInt("score"));
        }
        return scores;
    }

    // public int getUserScore(String email){
    //     String SQL_GET_SCORE = """
    //             select score from user_activity ua
    //             inner join user_details ud
    //             on ua.uid = ud.uid
    //             where ud.email = ?;
    //             """;
    //     SqlRowSet rs = sqlTemplate.queryForRowSet(SQL_GET_SCORE, email);

    //     while (rs.next()){
    //         return rs.getInt("score");
    //     }
    //     return 0;
    // }

    public void updateScore(String email, int newScore){
        String SQL_UPDATE_SCORE = """
                update user_activity ua
                inner join user_details ud
                on ua.uid = ud.uid
                set ua.score = ?
                where ud.email = ?;
                """;

        sqlTemplate.update(SQL_UPDATE_SCORE, newScore, email);
    }

}
