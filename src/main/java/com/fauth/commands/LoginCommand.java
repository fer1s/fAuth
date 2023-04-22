package com.fauth.commands;

import com.fauth.FAuth;
import com.fauth.manager.AccountManager;
import com.fauth.utils.AuthManager;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class LoginCommand implements CommandExecutor {

    private final FAuth plugin;

    public LoginCommand(@Nonnull FAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }

        String prefix = plugin.config.getString("prefix");
        AccountManager accountManager = plugin.accountManager;

        Player player = (Player) commandSender;
        String password = strings[0];

        if(accountManager.isCached(player.getName())) {
            if(prefix == null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJesteś już zalogowany!"));
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &cJesteś już zalogowany!"));
            }
            return true;
        }

        if(strings.length != 1) {
            if(prefix == null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Użycie: /login <hasło>"));
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &7Użycie: /login <hasło>"));
            }
            return true;
        }

        Boolean status = AuthManager.login(plugin, player, password);

        if(status) {
            if(prefix == null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aZalogowano pomyślnie!"));
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &aZalogowano pomyślnie!"));
            }

            player.setWalkSpeed(0.2F);
            player.setFlySpeed(0.1F);

            player.setCollidable(true);
            player.setCanPickupItems(true);
            player.setInvulnerable(false);
            player.setSilent(false);
            player.setSleepingIgnored(false);

            accountManager.addToCache(player.getName());
        } else {
            if(prefix == null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNiepoprawne hasło!"));
            } else {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &cNiepoprawne hasło!"));
            }
        }

        return true;
    }
}
