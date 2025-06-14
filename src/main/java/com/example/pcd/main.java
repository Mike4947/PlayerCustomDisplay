package com.example.pcd;

import org.bukkit.plugin.java.JavaPlugin;

public final class main extends JavaPlugin {

    private int customPlayerCount = -1;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadPlayerCount();

        // --- UPDATED COMMAND REGISTRATION ---
        // We create one instance of our command class
        PCDCommand pcdCommand = new PCDCommand(this);
        // We tell the server that this instance will handle BOTH execution and tab completion
        getCommand("pcd").setExecutor(pcdCommand);
        getCommand("pcd").setTabCompleter(pcdCommand);

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