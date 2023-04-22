package com.fauth.utils;

import com.fauth.FAuth;
import org.bukkit.entity.Player;
import org.jasypt.util.password.BasicPasswordEncryptor;

import java.util.logging.Level;

public class AuthManager {
    public static boolean isLogged(FAuth plugin, Player player) {
        return false;
    }

    public static boolean isRegistered(FAuth plugin, Player player) {
        String username = player.getName();
        return DatabaseManager.isRegistered(plugin, username);
    }

    public static boolean register(FAuth plugin, Player player, String password) {
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();
        String encryptedPassword = encryptor.encryptPassword(password);

        try {
            DatabaseManager.register(plugin, player.getName(), encryptedPassword, player.getAddress().getAddress().getHostAddress());
            return true;
        } catch (Exception err) {
            plugin.getLogger().log(Level.WARNING, err.getMessage());
            return false;
        }
    }

    public static boolean login(FAuth plugin, Player player, String password) {
        BasicPasswordEncryptor encryptor = new BasicPasswordEncryptor();

        String encryptedPassword = DatabaseManager.getPassword(plugin, player.getName());
        if (encryptedPassword == null) {
            return false;
        }

        if(encryptor.checkPassword(password, encryptedPassword)) {
            return true;
        } else {
            return false;
        }
    }
}
