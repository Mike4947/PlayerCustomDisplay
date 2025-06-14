package com.example.pcd;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

// Add "implements Listener"
public final class main extends JavaPlugin implements Listener {

    private int customPlayerCount = -1;
    // --- NEW ---
    private FakePlayerManager fakePlayerManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadPlayerCount();

        // --- NEW ---
        this.fakePlayerManager = new FakePlayerManager(this);
        // Update the tab list on startup/reload if a number is set
        updateFakePlayerList(this.customPlayerCount);

        PCDCommand pcdCommand = new PCDCommand(this);
        getCommand("pcd").setExecutor(pcdCommand);
        getCommand("pcd").setTabCompleter(pcdCommand);

        getServer().getPluginManager().registerEvents(new ServerListListener(this), this);
        // --- NEW: Register events for this class ---
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("PlayerCustomDisplay has been enabled!");
    }

    // --- NEW: Method to handle player join events ---
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // When a player joins, send them the list of fake players
        fakePlayerManager.sendPlayersTo(event.getPlayer());
    }

    // --- NEW: Public method to be called by the command ---
    public void updateFakePlayerList(int count) {
        fakePlayerManager.updateFakePlayers(count);
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
        // --- NEW: Update the tab list whenever the count is set ---
        updateFakePlayerList(count);
    }
}