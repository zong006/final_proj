package vttp.final_backend.repo;

import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp.final_backend.model.User;

@Repository
public class UserSqlRepo {
    
    @Autowired
    private JdbcTemplate template;
    
    private final String SQL_GET_LAST_LOGIN = """
            select last_login from user_activity
            where uid = ?
            """;

    public Optional<String> getUser(String uid){
        String SQL_GET_USER = """
            select uid, email from user_details
            where uid = ?;
            """;
        return template.query(SQL_GET_USER, (ResultSet rs) -> {
            if (rs.next()){
                return Optional.of(rs.getString("uid"));
            }
            else {
                return Optional.empty();
            }
        },uid) ;
    }

    public String loginUser(User user, String uid){
        String SQL_INSERT_NEW_USER = """
            insert into user_details
            (uid, email)
            values 
            (?, ?);
            """;

        String SQL_NEW_USER_ACTIVITY = """
            insert into user_activity
            (uid, score, last_login, current_login)
            values (?, 0, NOW(), NOW())
            ;
            """;

        Optional<String> opt = getUser(uid);
        if (opt.isEmpty()){ // <-- user is new 
            template.update(SQL_INSERT_NEW_USER, uid, user.getUsername());
            template.update(SQL_NEW_USER_ACTIVITY, uid);

            return uid;
        }
        return "user exists";
    }

    public void updateLoginActivity(String email, String latestLogin) {
        // latest login is already in the format yyyy-MM-dd HH:mm:ss
        String SQL_UPDATE_USER_ACTIVITY = """
                update user_activity ua
                inner join user_details ud ON ua.uid = ud.uid
                set ua.last_login = ua.current_login, 
                ua.current_login = ?
                where ud.email = ?
                ;
                """;

        template.update(SQL_UPDATE_USER_ACTIVITY, latestLogin, email);
    }

    public List<String> getEmailsOfInactiveUsers(){
        String SQL_GET_INACTIVE_EMAILS = """
                select ud.email
                from user_activity ua
                inner join user_details ud on ua.uid = ud.uid
                where ua.current_login < NOW() - INTERVAL 1 DAY
                ;
                """;

        SqlRowSet rs = template.queryForRowSet(SQL_GET_INACTIVE_EMAILS);

        List<String> inactiveUsers = new LinkedList<>();
        while (rs.next()){
            inactiveUsers.add(rs.getString("email"));
        }

        return inactiveUsers;
    }
}
