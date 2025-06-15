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
            sender.sendMessage(ChatColor.RED + "Usage: /pcd <set|setmax|hover|stat>");
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

            case "hover":
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Usage: /pcd hover <list|add|remove|clear>");
                    return true;
                }
                handleHoverCommand(sender, args);
                return true;

            case "stat":
                // --- THIS SECTION IS NOW FULLY RESTORED ---
                int baseCount = plugin.getCustomPlayerCount();
                int onlinePlayers = plugin.getServer().getOnlinePlayers().size();
                int maxPlayers = plugin.getCustomMaxPlayers();
                List<String> hoverList = plugin.getCustomHoverList();

                sender.sendMessage(ChatColor.GOLD + "--- PlayerCustomDisplay Stats ---");

                // Display status for dynamic online count
                if (baseCount < 0) {
                    sender.sendMessage(ChatColor.YELLOW + "Custom online count: " + ChatColor.RED + "Disabled");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Custom online count: " + ChatColor.GREEN + "Enabled");
                    sender.sendMessage(ChatColor.AQUA + "  Displayed: " + ChatColor.WHITE + (baseCount + onlinePlayers) + ChatColor.GRAY + " (Base: " + baseCount + " + Online: " + onlinePlayers + ")");
                }

                // Display status for custom max players
                if (maxPlayers < 0) {
                    sender.sendMessage(ChatColor.YELLOW + "Custom max players: " + ChatColor.RED + "Disabled");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Custom max players: " + ChatColor.GREEN + "Enabled" + ChatColor.AQUA + " -> " + ChatColor.WHITE + maxPlayers);
                }

                // Display status for custom hover list
                if (hoverList.isEmpty()) {
                    sender.sendMessage(ChatColor.YELLOW + "Custom hover list: " + ChatColor.RED + "Disabled");
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "Custom hover list: " + ChatColor.GREEN + "Enabled" + ChatColor.AQUA + " (" + hoverList.size() + " lines)");
                }
                return true;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Usage: /pcd <set|setmax|hover|stat>");
                return true;
        }
    }

    private void handleHoverCommand(CommandSender sender, String[] args) {
        String hoverAction = args[1].toLowerCase();
        List<String> currentList = new ArrayList<>(plugin.getCustomHoverList());

        switch (hoverAction) {
            case "list":
                if (currentList.isEmpty()) {
                    sender.sendMessage(ChatColor.YELLOW + "The hover list is currently empty.");
                } else {
                    sender.sendMessage(ChatColor.GOLD + "--- Hover List ---");
                    for (int i = 0; i < currentList.size(); i++) {
                        sender.sendMessage(ChatColor.GRAY + "" + (i + 1) + ": " + ChatColor.RESET + currentList.get(i));
                    }
                }
                break;

            case "add":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /pcd hover add <text>");
                    return;
                }
                String textToAdd = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                currentList.add(textToAdd); // Don't translate colors here, main class does it.
                plugin.setCustomHoverList(currentList);
                sender.sendMessage(ChatColor.GREEN + "Added line to hover list.");
                break;

            case "remove":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "Usage: /pcd hover remove <line_number>");
                    return;
                }
                try {
                    int lineNum = Integer.parseInt(args[2]);
                    if (lineNum < 1 || lineNum > currentList.size()) {
                        sender.sendMessage(ChatColor.RED + "Invalid line number.");
                        return;
                    }
                    currentList.remove(lineNum - 1);
                    plugin.setCustomHoverList(currentList);
                    sender.sendMessage(ChatColor.GREEN + "Removed line " + lineNum + " from hover list.");
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED + "Please enter a valid line number.");
                }
                break;

            case "clear":
                currentList.clear();
                plugin.setCustomHoverList(currentList);
                sender.sendMessage(ChatColor.GREEN + "Hover list has been cleared.");
                break;

            default:
                sender.sendMessage(ChatColor.RED + "Unknown hover command. Usage: /pcd hover <list|add|remove|clear>");
                break;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], Arrays.asList("set", "setmax", "hover", "stat"), completions);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("setmax")) {
                StringUtil.copyPartialMatches(args[1], Arrays.asList("100", "500", "-1"), completions);
            } else if (args[0].equalsIgnoreCase("hover")) {
                StringUtil.copyPartialMatches(args[1], Arrays.asList("list", "add", "remove", "clear"), completions);
            }
        }

        Collections.sort(completions);
        return completions;
    }
}