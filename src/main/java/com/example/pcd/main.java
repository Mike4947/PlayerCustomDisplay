package com.example.pcd;

import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    private int customPlayerCount = -1;
    private UpdateChecker updateChecker; // Add a field for the update checker

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadPlayerCount();

        // --- NEW: Initialize and run the UpdateChecker ---
        this.updateChecker = new UpdateChecker(this);
        // Check for updates 5 seconds after the server starts to avoid spam
        getServer().getScheduler().runTaskLater(this, this.updateChecker::check, 100L);

        PCDCommand pcdCommand = new PCDCommand(this);
        getCommand("pcd").setExecutor(pcdCommand);
        getCommand("pcd").setTabCompleter(pcdCommand);

        getServer().getPluginManager().registerEvents(new ServerListListener(this), this);

        getLogger().info("PlayerCustomDisplay has been enabled!");
    }

    // A "getter" for other classes to access the update checker
    public UpdateChecker getUpdateChecker() {
        return updateChecker;
    }

    public void loadPlayerCount() {
        this.customPlayerCount = getConfig().getInt("custom-player-count", -1);
    }

    public int getCustomPlayerCount() {
        return customPlayerCount;
    }

    public void setCustomPlayerCount(int count) {
        this.customPlayerCount = count;
        getConfig().set("custom-player-count", count);
        saveConfig();
    }
}