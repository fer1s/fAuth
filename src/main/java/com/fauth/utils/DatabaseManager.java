package com.fauth.utils;

import com.fauth.FAuth;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;

public class DatabaseManager {
    public static boolean register(FAuth plugin, String username, String password, String ip) {
        try {
            String query = "INSERT INTO users(username, password, ip) VALUES(?, ?, ?)";
            PreparedStatement preparedStatement = plugin.connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, ip);

            preparedStatement.executeUpdate();

            return true;
        } catch (Exception err) {
            plugin.getLogger().log(Level.WARNING, err.getMessage());
            return false;
        }
    }

    // get user password by username
    public static String getPassword(FAuth plugin, String username) {
        try {
            String query = "SELECT password FROM users WHERE username = ?";
            PreparedStatement preparedStatement = plugin.connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String password = rs.getString("password");
                return password;
            }

            return null;
        } catch (Exception err) {
            plugin.getLogger().log(Level.WARNING, err.getMessage());
            return null;
        }
    }

    public static boolean isRegistered(FAuth plugin, String username) {
        try {
            String query = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = plugin.connection.prepareStatement(query);
            preparedStatement.setString(1, username);

            return preparedStatement.executeQuery().next();
        } catch (Exception err) {
            plugin.getLogger().log(Level.WARNING, err.getMessage());
            return false;
        }
    }
}
