package com.example.pcd; // <-- Updated package

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PCDCommand implements CommandExecutor {

    // Now correctly refers to the 'main' class
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
}