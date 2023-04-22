package com.fauth.events;

import com.fauth.FAuth;
import com.fauth.task.LoginQueue;
import com.fauth.utils.DatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final FAuth plugin;

    public PlayerJoinListener(FAuth plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String username = player.getName();
        String prefix = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("prefix"));
        Boolean isPrefix = prefix != null;

        boolean isRegistered = DatabaseManager.isRegistered(plugin, username);
        LoginQueue.addToQueue(username, isRegistered);

        player.setWalkSpeed(0F);
        player.setFlySpeed(0F);

        player.setCollidable(false);
        player.setCanPickupItems(false);
        player.setInvulnerable(true);
        player.setSilent(true);
        player.setSleepingIgnored(true);

        if (isRegistered) {
            String message = isPrefix ? prefix + " &7Zaloguj się za pomocą komendy &c/login <hasło>." : "&7Zaloguj się za pomocą komendy &c/login <hasło>.";
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            String message = isPrefix ? prefix + " &7Zarejestruj się za pomocą komendy &c/register <hasło> <powtórz hasło>." : "&7Zarejestruj się za pomocą komendy &c/register <hasło> <powtórz hasło>.";
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

}
