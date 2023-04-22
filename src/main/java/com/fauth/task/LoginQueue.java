package com.fauth.task;

import com.fauth.FAuth;
import lombok.RequiredArgsConstructor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LoginQueue {

    private static final ConcurrentHashMap<String, PlayerLogin> queue = new ConcurrentHashMap<>();

    public static void startTask(FAuth plugin) {
        final Server server = plugin.getServer();
        server.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if(queue.isEmpty()) return;

            for(Map.Entry<String, PlayerLogin> entry : queue.entrySet()) {
                String name = entry.getKey();
                Player player = server.getPlayer(name);
                if(player == null) {
                    queue.remove(name);
                    return;
                }

                PlayerLogin login = entry.getValue();
                int seconds = login.seconds;
                if (seconds >= plugin.config.getInt("time_to_login")) {
                    server.getScheduler().runTask(plugin, () -> player.kickPlayer("Czas na autoryzację się skończył!"));
                    queue.remove(name);
                    return;
                }

                login.addSecond();
            }
        }, 0, 20);
    }

    public static void addToQueue(@Nonnull String name, boolean registered) {
        queue.put(name, new PlayerLogin(registered));
    }

    public static void removeFromQueue(@Nonnull String name) {
        queue.remove(name);
    }

    @RequiredArgsConstructor
    private static class PlayerLogin {

        private final boolean registered;
        private int seconds;

        public void addSecond() {
            seconds++;
        }
    }
}
