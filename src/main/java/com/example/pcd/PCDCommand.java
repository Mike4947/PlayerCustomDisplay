package com.example.pcd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PCDCommand implements CommandExecutor, TabCompleter {

    private final main plugin;

    public PCDCommand(main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("playercustomdisplay.admin")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /pcd <set|stat>");
            return true;
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (args.length < 2) {
                sender.sendMessage(ChatColor.RED + "Usage: /pcd set <number>");
                return true;
            }
            try {
                int newBaseCount = Integer.parseInt(args[1]);
                plugin.setCustomPlayerCount(newBaseCount);
                sender.sendMessage(ChatColor.GREEN + "Base player count has been set to: " + newBaseCount);
                sender.sendMessage(ChatColor.GRAY + "The number in the server list will be this value plus the online player count.");
                if (newBaseCount < 0) {
                    sender.sendMessage(ChatColor.YELLOW + "A number less than 0 disables the dynamic player count feature.");
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(ChatColor.RED + "Invalid number provided. Please enter a valid integer.");
            }
            return true;
        }

        if (args[0].equalsIgnoreCase("stat")) {
            int baseCount = plugin.getCustomPlayerCount();
            int onlinePlayers = plugin.getServer().getOnlinePlayers().size();

            sender.sendMessage(ChatColor.GOLD + "--- PlayerCustomDisplay Stats ---");
            if (baseCount < 0) {
                sender.sendMessage(ChatColor.YELLOW + "Feature Status: " + ChatColor.RED + "Disabled");
                sender.sendMessage(ChatColor.AQUA + "Real Player Count: " + ChatColor.WHITE + onlinePlayers);
            } else {
                int dynamicCount = baseCount + onlinePlayers;
                sender.sendMessage(ChatColor.YELLOW + "Feature Status: " + ChatColor.GREEN + "Enabled");
                sender.sendMessage(ChatColor.AQUA + "Base Player Count: " + ChatColor.WHITE + baseCount);
                sender.sendMessage(ChatColor.AQUA + "Actual Online Players: " + ChatColor.WHITE + onlinePlayers);
                sender.sendMessage(ChatColor.AQUA + "Displayed Player Count: " + ChatColor.WHITE + dynamicCount);
            }
            return true;
        }

        sender.sendMessage(ChatColor.RED + "Unknown subcommand. Usage: /pcd <set|stat>");
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList("set", "stat"), completions);
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            StringUtil.copyPartialMatches(args[1], Arrays.asList("100", "500", "1000", "-1"), completions);
        }

        Collections.sort(completions);
        return completions;
    }
}