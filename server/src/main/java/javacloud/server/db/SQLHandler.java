package javacloud.server.db;

import javacloud.server.ServerConfig;
import javacloud.shared.model.User;
import javacloud.shared.utils.StringUtils;

import java.sql.*;

public class SQLHandler {
    private static Connection connection;
    private static Statement statement;

    public static void connect() {
        try {
            Class.forName(ServerConfig.get().getDbDriver());
            connection = DriverManager.getConnection(ServerConfig.get().getDbConnectionUrl());
            statement = connection.createStatement();
        } catch (Exception e) {
            throw new RuntimeException("Database connection error", e);
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
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("token"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static User getUser(String token) {
        if (StringUtils.isNullOrEmpty(token)) {
            return null;
        }

        try {
            ResultSet rs = statement.executeQuery(String.format("SELECT * FROM users WHERE token ='%s'", token));

            if (rs.next()) {
                return new User(rs.getInt("id"), rs.getString("username"), rs.getString("token"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateToken(String username, String token) {
        try {
            statement.execute(String.format("UPDATE users SET token = '%s' WHERE username ='%s'", token, username));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
