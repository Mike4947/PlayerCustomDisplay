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
            sender.sendMessage(ChatColor.RED + "Usage: /pcd <set|setmax|stat>");
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "set":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /pcd set <number>");
                    return true;
                }
                try {
                    int newBaseCount = Integer.parseInt(args[1]);
                    plugin.setCustomPlayerCount(newBaseCount);
                    sender.sendMessage(ChatColor.GREEN + "Base player count has been set to: " + newBaseCount);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid number provided.");
                }
                return true;

            // --- NEW: Handle the "setmax" subcommand ---
            case "setmax":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /pcd setmax <number>");
                    return true;
                }
                try {
                    int newMaxCount = Integer.parseInt(args[1]);
                    plugin.setCustomMaxPlayers(newMaxCount);
                    sender.sendMessage(ChatColor.GREEN + "Custom max player count has been set to: " + newMaxCount);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Invalid number provided.");
                }
                return true;

            case "stat":
                int baseCount = plugin.getCustomPlayerCount();
                int onlinePlayers = plugin.getServer().getOnlinePlayers().size();
                // Also get the max players for the stat display
                int maxPlayers = plugin.getCustomMaxPlayers();


                sender.sendMessage(ChatColor.GOLD + "--- PlayerCustomDisplay Stats ---");
                if (baseCount < 0) {
                    sender.sendMessage(ChatColor.YELLOW + "Custom online count: " + ChatColor.RED + "Disabled");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Custom online count: " + ChatColor.GREEN + "Enabled");
                    sender.sendMessage(ChatColor.AQUA + "  Displayed: " + ChatColor.WHITE + (baseCount + onlinePlayers) + ChatColor.GRAY + " (Base: " + baseCount + " + Online: " + onlinePlayers + ")");
                }

                if (maxPlayers < 0) {
                    sender.sendMessage(ChatColor.YELLOW + "Custom max players: " + ChatColor.RED + "Disabled");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Custom max players: " + ChatColor.GREEN + "Enabled" + ChatColor.AQUA + " -> " + ChatColor.WHITE + maxPlayers);
                }
                return true;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Usage: /pcd <set|setmax|stat>");
                return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            // --- NEW: Add "setmax" to tab completion ---
            StringUtil.copyPartialMatches(args[0], Arrays.asList("set", "setmax", "stat"), completions);
        }
        else if (args.length == 2 && (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("setmax"))) {
            StringUtil.copyPartialMatches(args[1], Arrays.asList("100", "500", "-1"), completions);
        }

        Collections.sort(completions);
        return completions;
    }
}