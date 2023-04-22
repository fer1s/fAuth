package com.fauth.commands;

import com.fauth.FAuth;
import com.fauth.manager.AccountManager;
import com.fauth.utils.AuthManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class RegisterCommand implements CommandExecutor {

    private final FAuth plugin;

    public RegisterCommand(@Nonnull FAuth plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
            return true;
        }

        AccountManager accountManager = plugin.accountManager;
        String prefix = plugin.config.getString("prefix");
        String minPasswordLength = plugin.config.getString("min_password_length");
        String maxPasswordLength = plugin.config.getString("max_password_length");
        Player player = (Player) commandSender;

        if(accountManager.isCached(player.getName())) {
            if(prefix != null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &cJesteś już zalogowany!"));
            }else{
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cJesteś już zalogowany!"));
            }
            return true;
        }

        if(strings.length != 2) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &7Użycie: /register <hasło> <hasło>"));
            return true;
        }


        String password = strings[0];
        String confirmPassword = strings[1];

        if(!password.equals(confirmPassword)) {
            if(prefix != null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &7Hasła nie są takie same!"));
                return true;
            }else{
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Hasła nie są takie same!"));
                return true;
            }
        }

        if(password.length() < Integer.parseInt(minPasswordLength)) {
            if (prefix != null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &7Hasło jest za krótkie!"));
                return true;
            }else{
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Hasło jest za krótkie!"));
                return true;
            }
        }

        if(password.length() > Integer.parseInt(maxPasswordLength)) {
            if (prefix != null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &7Hasło jest za długie!"));
                return true;
            }else{
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Hasło jest za długie!"));
                return true;
            }
        }

        if(AuthManager.isRegistered(plugin, player)) {
            if (prefix != null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &7Jesteś już zarejestrowany!"));
                return true;
            }else{
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Jesteś już zarejestrowany!"));
                return true;
            }
        }

        Boolean status = AuthManager.register(plugin, player, password);

        if(status) {
            if(prefix != null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &aZostałeś zarejestrowany!"));
            }else{
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aZostałeś zarejestrowany!"));
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
            if(prefix != null) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + " &cWystąpił błąd!"));
            }else{
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cWystąpił błąd!"));
            }
        }

        return true;
    }
}
