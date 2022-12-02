package pl.coderslab.entity;

import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import pl.coderslab.DbUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class UserDAO {

    private static final String CREATE_USER_QUERY = "INSERT INTO users(username, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT * FROM users WHERE id=";

    private static final String UPDATE_USER_QUERY =
            "UPDATE users SET username = ?, email = ?, password = ? where id = ?";

    private static final String DELETE_USER_QUERY =
            "DELETE FROM users WHERE id=";

    private static final String FIND_ALL_QUERY = "SELECT * FROM users";


    public static void createUserAndSyncID(User user) {
        try (Connection conn = DbUtil.getConnection()) {

            String password = user.getPassword();
            String dbPassword = hashPassword(password);

            long id = DbUtil.create(conn, CREATE_USER_QUERY, user.getUserName(), user.getEmail(), dbPassword);
            user.setId(id);
            System.out.println("User successfully created");
            System.out.println("updated user with id " + id);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static User read(long userId) {
        User user = new User();
        try (Connection conn = DbUtil.getConnection()) {

            ArrayList<String> userData = DbUtil.readData(conn, SELECT_USER_BY_ID_QUERY + userId);
            if (userData.size() == 0) {
                System.out.println("User not found in DB - id: " + userId);

            } else {
                user.setId(Long.parseLong(userData.get(0)));
                user.setEmail(userData.get(1));
                user.setUserName(userData.get(2));
                user.setPassword(userData.get(3));
                System.out.println(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;

    }

    public static boolean userCheck(User user, User dbUser) {
        boolean passCheck = BCrypt.checkpw(user.getPassword(), dbUser.getPassword());

        return (user.getId() == dbUser.getId()) && (user.getEmail().equals(dbUser.getEmail())) &&
                (user.getUserName().equals(dbUser.getUserName())) && passCheck;
    }

    public static void update(User user, JSONObject updateParams) {
        long id = user.getId();

        user.setUserName(updateParams.getString("username"));
        user.setEmail(updateParams.getString("email"));
        user.setPassword(updateParams.getString("password"));

        try (Connection conn = DbUtil.getConnection()) {
            DbUtil.update(conn, UPDATE_USER_QUERY, user.getUserName(), user.getEmail(), hashPassword(user.getPassword()), Long.toString(id));
            System.out.println("Successfully updated user. New user data");
            User dbUser = read(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(long userId) {
        try (Connection conn = DbUtil.getConnection()) {
            User user = read(userId);
            if (user.getId() == 0) {
                System.out.println("Can not delete non-existing user");
            } else {
                DbUtil.delete(conn, DELETE_USER_QUERY + userId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> findAll() {

        ArrayList<String> allUserData = new ArrayList<>();

        try (Connection conn = DbUtil.getConnection()) {
            allUserData = DbUtil.readData(conn, FIND_ALL_QUERY);
            if (allUserData.size() == 0) {
                System.out.println("Users table is empty");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allUserData;


    }

}


