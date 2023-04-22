package com.fauth.utils;

import com.fauth.FAuth;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public class Database {
    public Connection connect(@Nonnull FAuth plugin) {
        plugin.getLogger().log(Level.INFO, "Connecting to database...");

        String host = plugin.config.getString("postgres_host");
        String user = plugin.config.getString("postgres_user");
        String password = plugin.config.getString("postgres_password");
        String database = plugin.config.getString("postgres_database");
        int port = plugin.config.getInt("postgres_port");

        Connection connection = null;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://" + host + "/");
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("port", port);
        config.setDriverClassName(org.postgresql.Driver.class.getName());

        HikariDataSource ds = new HikariDataSource(config);

        try {
            connection = ds.getConnection();

            try {
                plugin.getLogger().log(Level.INFO, "Creating database...");
                connection.createStatement().execute("CREATE DATABASE " + database);
                connection.close();

                config.setJdbcUrl("jdbc:postgresql://" + host + "/" + database);
                ds = new HikariDataSource(config);
                connection = ds.getConnection();
            } catch (SQLException err) {
                if(err.getMessage().contains("already exists")) {
                    plugin.getLogger().log(Level.INFO, "Database already exists, connecting to it...");
                    config.setJdbcUrl("jdbc:postgresql://" + host + "/" + database);
                    ds = new HikariDataSource(config);
                    connection = ds.getConnection();
                } else {
                    plugin.getLogger().log(Level.WARNING, err.getMessage());
                }
            }
        } catch (SQLException err) {
            plugin.getLogger().log(Level.WARNING, err.getMessage());
        }

        // Create tables
        try {
            plugin.getLogger().log(Level.INFO, "Creating tables if doesn't exist...");
            // this is postgresql
            String query = "" +
                    "CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY," +
                    "username VARCHAR(30) NOT NULL," +
                    "password TEXT NOT NULL," +
                    "discord_id VARCHAR(30)," +
                    "ip VARCHAR(30) NOT NULL," +
                    "last_login TIMESTAMP," +
                    "last_logout TIMESTAMP," +

                    "UNIQUE (username)," +
                    "UNIQUE (discord_id)," +
                    "UNIQUE (ip)" +
                    ");";

            connection.createStatement().execute(query);

        } catch (SQLException err) {
            plugin.getLogger().log(Level.WARNING, err.getMessage());
        }

        plugin.getLogger().log(Level.INFO, "Connected to database successfully!");
        return connection;
    }
}
