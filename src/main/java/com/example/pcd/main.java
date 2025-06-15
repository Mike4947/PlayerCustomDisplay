package com.example.pcd;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class main extends JavaPlugin {

    private int customPlayerCount;
    private int customMaxPlayers;
    private List<String> customHoverList;

    @Override
    public void onEnable() {
        // This ensures the config file exists and loads default values if it's new
        saveDefaultConfig();
        // This loads all values from the config into the plugin
        loadAllValues();

        // Register the command handler
        getCommand("pcd").setExecutor(new PCDCommand(this));

        // Register the event listener
        getServer().getPluginManager().registerEvents(new ServerListListener(this), this);

        getLogger().info("PlayerCustomDisplay has been enabled!");
    }

    /**
     * Loads all values from the config.yml file into the plugin's variables.
     * This is the method the /pcd reload command will call.
     */
    public void loadAllValues() {
        this.customPlayerCount = getConfig().getInt("custom-player-count", -1);
        this.customMaxPlayers = getConfig().getInt("custom-max-players", -1);
        // This part loads the hover list and translates color codes
        this.customHoverList = getConfig().getStringList("player-hover-list").stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    // --- Getters for other classes to use ---
    public int getCustomPlayerCount() { return customPlayerCount; }
    public int getCustomMaxPlayers() { return customMaxPlayers; }
    public List<String> getCustomHoverList() { return customHoverList; }

    // --- Setters for the commands to use ---
    public void setCustomPlayerCount(int count) {
        this.customPlayerCount = count;
        getConfig().set("custom-player-count", count);
        saveConfig();
    }

    public void setCustomMaxPlayers(int count) {
        this.customMaxPlayers = count;
        getConfig().set("custom-max-players", count);
        saveConfig();
    }

    public void setCustomHoverList(List<String> list) {
        this.customHoverList = list;
        // When saving, convert color codes back to the '&' format for the file
        List<String> toSave = list.stream()
                .map(line -> line.replace('§', '&'))
                .collect(Collectors.toList());
        getConfig().set("player-hover-list", toSave);
        saveConfig();
    }
}