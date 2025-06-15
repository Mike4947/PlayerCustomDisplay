package com.example.pcd;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class main extends JavaPlugin {

    private int customPlayerCount = -1;
    private int customMaxPlayers = -1;
    // --- NEW: Add a variable for the hover list ---
    private List<String> customHoverList = new ArrayList<>();

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadPlayerCount();
        loadMaxPlayerCount();
        // --- NEW: Load the hover list on startup ---
        loadHoverList();

        PCDCommand pcdCommand = new PCDCommand(this);
        getCommand("pcd").setExecutor(pcdCommand);
        getCommand("pcd").setTabCompleter(pcdCommand);

        getServer().getPluginManager().registerEvents(new ServerListListener(this), this);

        getLogger().info("PlayerCustomDisplay has been enabled!");
    }

    // --- NEW: Method to load the hover list ---
    public void loadHoverList() {
        this.customHoverList = getConfig().getStringList("player-hover-list").stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    public void loadPlayerCount() {
        this.customPlayerCount = getConfig().getInt("custom-player-count", -1);
    }

    public void loadMaxPlayerCount() {
        this.customMaxPlayers = getConfig().getInt("custom-max-players", -1);
    }

    public int getCustomPlayerCount() {
        return customPlayerCount;
    }

    public int getCustomMaxPlayers() {
        return customMaxPlayers;
    }

    // --- NEW: Getter for the hover list ---
    public List<String> getCustomHoverList() {
        return customHoverList;
    }

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

    // --- NEW: Setter for the hover list ---
    public void setCustomHoverList(List<String> list) {
        this.customHoverList = list;
        // We need to save the list without the color codes translated back
        List<String> toSave = list.stream()
                .map(line -> line.replace('§', '&'))
                .collect(Collectors.toList());
        getConfig().set("player-hover-list", toSave);
        saveConfig();
    }
}