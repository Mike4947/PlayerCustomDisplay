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

// The class now implements TabCompleter as well as CommandExecutor
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

        if (args.length < 2 || !args[0].equalsIgnoreCase("set")) {
            sender.sendMessage(ChatColor.RED + "Usage: /pcd set <number>");
            return true;
        }

        try {
            int newPlayerCount = Integer.parseInt(args[1]);
            plugin.setCustomPlayerCount(newPlayerCount);
            sender.sendMessage(ChatColor.GREEN + "Custom player count has been set to: " + newPlayerCount);
            if (newPlayerCount < 0) {
                sender.sendMessage(ChatColor.YELLOW + "A number less than 0 disables the feature.");
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid number provided. Please enter a valid integer.");
        }

        return true;
    }

    // --- NEW METHOD FOR TAB COMPLETION ---
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();

        // Logic for the first argument (e.g., /pcd <HERE>)
        if (args.length == 1) {
            // Find all subcommands that start with what the user has typed so far.
            StringUtil.copyPartialMatches(args[0], Collections.singletonList("set"), completions);
        }
        // Logic for the second argument (e.g., /pcd set <HERE>)
        else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            // Suggest some helpful example numbers.
            StringUtil.copyPartialMatches(args[1], Arrays.asList("100", "500", "1000", "-1"), completions);
        }

        // Sort the suggestions alphabetically and return them.
        Collections.sort(completions);
        return completions;
    }
}