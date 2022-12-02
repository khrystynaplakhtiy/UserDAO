package pl.coderslab;

import org.json.JSONObject;
import pl.coderslab.entity.User;
import pl.coderslab.entity.UserDAO;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
//        User user = new User();
//        user.setUserName("mr_testuser2");
//        user.setEmail("someemail12@mail.com");
//        user.setPassword("pass123456");
//        user.setId(5);

//        UserDAO.createUserAndSyncID(user);
        User dbUser = UserDAO.read(2);
//        System.out.println(dbUser);
//        Boolean res = UserDAO.userCheck(user, dbUser);
//        System.out.println(user);
//        System.out.println(res);
//
//        JSONObject updateData = new JSONObject();
//        updateData.put("username", "updated_user");
//        updateData.put("email", "newemail@mail.com");
//        updateData.put("password", "pass987654");
//
//        UserDAO.update(user, updateData);
//        UserDAO.delete(5);

        ArrayList<String> allUsers = UserDAO.findAll();
        System.out.println(allUsers);

    }
}
