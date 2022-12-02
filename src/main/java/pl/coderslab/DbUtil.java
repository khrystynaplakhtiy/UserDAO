package pl.coderslab;

import pl.coderslab.entity.User;

import java.sql.*;
import java.util.ArrayList;

public class DbUtil {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/workshop2?useSSL=false&characterEncoding=utf8";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "coderslab";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public static long create(Connection conn, String query, String... params) {
        long id = 0;
        try (PreparedStatement statement = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                id = rs.getLong(1);
                System.out.println("Inserted ID: " + id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }


    public static ArrayList<String> readData(Connection conn, String query) {
        ArrayList<String> userData = new ArrayList<String>(4);

        try (PreparedStatement statement = conn.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery();) {
            while (resultSet.next()) {
                userData.add(Long.toString(resultSet.getLong("id")));
                userData.add(resultSet.getString("email"));
                userData.add(resultSet.getString("username"));
                userData.add(resultSet.getString("password"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }

    public static void update(Connection conn, String query, String... params) {
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (int i = 0; i < params.length; i++) {
                statement.setString(i + 1, params[i]);
            }
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(Connection conn, String query){
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.executeUpdate();
            System.out.println("User successfully deleted");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



}

