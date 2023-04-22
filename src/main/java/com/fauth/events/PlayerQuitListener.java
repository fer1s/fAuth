package com.fauth.events;

import com.fauth.FAuth;
import com.fauth.manager.AccountManager;
import com.fauth.task.LoginQueue;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final FAuth plugin;
    AccountManager accountManager;

    public PlayerQuitListener(FAuth plugin) {
        this.plugin = plugin;
        this.accountManager = plugin.accountManager;
    }

    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String name = player.getName();

        accountManager.removeFromCache(name);
        LoginQueue.removeFromQueue(name);
    }
}
