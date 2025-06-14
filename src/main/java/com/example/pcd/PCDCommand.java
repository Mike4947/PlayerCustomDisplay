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
            int newBaseCount = Integer.parseInt(args[1]);
            plugin.setCustomPlayerCount(newBaseCount);
            // --- MODIFIED MESSAGE ---
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

    // --- Tab completion method remains the same ---
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Collections.singletonList("set"), completions);
        }
        else if (args.length == 2 && args[0].equalsIgnoreCase("set")) {
            StringUtil.copyPartialMatches(args[1], Arrays.asList("100", "500", "1000", "-1"), completions);
        }

        Collections.sort(completions);
        return completions;
    }
}