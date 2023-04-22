package com.fauth.utils;

import com.fauth.FAuth;
import com.fauth.commands.*;
import com.fauth.events.*;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.logging.Level;

public class Loader {
    public static void handleConfig(@Nonnull FAuth plugin) {
        plugin.config.addDefault("postgres_host", "localhost");
        plugin.config.addDefault("postgres_user", "postgres");
        plugin.config.addDefault("postgres_password", "postgres");
        plugin.config.addDefault("postgres_database", "fauth");
        plugin.config.addDefault("postgres_port", 5432);

        plugin.config.addDefault("prefix", "&8[&cfAuth&8]");
        plugin.config.addDefault("min_password_length", 6);
        plugin.config.addDefault("max_password_length", 32);
        plugin.config.addDefault("time_to_login", 30);
        plugin.config.options().copyDefaults(true);
        plugin.saveConfig();
    }

    public static void loadCommands(@Nonnull FAuth plugin) {
        plugin.getLogger().log(Level.INFO, "Loading commands...");
        Objects.requireNonNull(plugin.getCommand("login")).setExecutor(new LoginCommand(plugin));
        Objects.requireNonNull(plugin.getCommand("register")).setExecutor(new RegisterCommand(plugin));
        plugin.getLogger().log(Level.INFO, "Commands loaded successfully!");
    }

    public static void loadEvents(@Nonnull FAuth plugin) {
        plugin.getLogger().log(Level.INFO, "Loading events...");
        plugin.getServer().getPluginManager().registerEvents(new PlayerJoinListener(plugin), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerQuitListener(plugin), plugin);
        plugin.getLogger().log(Level.INFO, "Events loaded successfully!");
    }
}
