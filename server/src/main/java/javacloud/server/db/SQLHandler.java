package javacloud.server.db;

import javacloud.server.ServerConfig;
import javacloud.shared.model.User;

import java.sql.*;

public class SQLHandler {
    private static Connection connection;
    private static Statement statement;

    public static void connect() {
        try {
            Class.forName(ServerConfig.get().getDbDriver());
            connection = DriverManager.getConnection(ServerConfig.get().getDbConnectionString());
            statement = connection.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static User getUser(String username, String password) {
        try {
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM users WHERE username ='%s' AND password = '%s'", username, password));

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
