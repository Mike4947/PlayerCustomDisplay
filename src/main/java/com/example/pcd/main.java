package com.example.pcd;

import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    private int customPlayerCount = -1;
    // --- NEW: Add a variable for the max player count ---
    private int customMaxPlayers = -1;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadPlayerCount();
        // --- NEW: Load the new value on startup ---
        loadMaxPlayerCount();

        PCDCommand pcdCommand = new PCDCommand(this);
        getCommand("pcd").setExecutor(pcdCommand);
        getCommand("pcd").setTabCompleter(pcdCommand);

        getServer().getPluginManager().registerEvents(new ServerListListener(this), this);

        getLogger().info("PlayerCustomDisplay has been enabled!");
    }

    public void loadPlayerCount() {
        this.customPlayerCount = getConfig().getInt("custom-player-count", -1);
    }

    // --- NEW: Method to load the max players value ---
    public void loadMaxPlayerCount() {
        this.customMaxPlayers = getConfig().getInt("custom-max-players", -1);
    }

    public int getCustomPlayerCount() {
        return customPlayerCount;
    }

    // --- NEW: Getter for the max players value ---
    public int getCustomMaxPlayers() {
        return customMaxPlayers;
    }

    public void setCustomPlayerCount(int count) {
        this.customPlayerCount = count;
        getConfig().set("custom-player-count", count);
        saveConfig();
    }

    // --- NEW: Setter for the max players value ---
    public void setCustomMaxPlayers(int count) {
        this.customMaxPlayers = count;
        getConfig().set("custom-max-players", count);
        saveConfig();
    }
}