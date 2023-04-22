package com.fauth;

import com.fauth.manager.AccountManager;
import com.fauth.task.LoginQueue;
import com.fauth.utils.Database;
import com.fauth.utils.Loader;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.util.logging.Level;

public final class FAuth extends JavaPlugin {

    public final Database database = new Database();
    public final AccountManager accountManager = new AccountManager();
    public final FileConfiguration config = getConfig();
    public Connection connection;

    @Override
    public void onEnable() {
        Loader.handleConfig(this);
        connection = database.connect(this);
        Loader.loadCommands(this);
        Loader.loadEvents(this);
        LoginQueue.startTask(this);
    }

    @Override
    public void onDisable() {
        try {
            connection.close();
        } catch (Exception err) {
            getLogger().log(Level.WARNING, err.getMessage());
        }
        getLogger().log(Level.INFO, "FAuth has been disabled!");
    }
}
