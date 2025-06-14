package com.example.pcd; // <-- Updated package

import org.bukkit.plugin.java.JavaPlugin;

// The class name is now 'main' to match your plugin.yml
public final class main extends JavaPlugin {

    private int customPlayerCount = -1;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadPlayerCount();

        getCommand("pcd").setExecutor(new PCDCommand(this));
        getServer().getPluginManager().registerEvents(new ServerListListener(this), this);

        getLogger().info("PlayerCustomDisplay has been enabled!");
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