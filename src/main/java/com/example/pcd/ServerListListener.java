package com.example.pcd;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ServerListListener implements Listener {

    private final main plugin;

    public ServerListListener(main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(PaperServerListPingEvent event) {
        int customCount = plugin.getCustomPlayerCount();
        if (customCount >= 0) {
            event.setNumPlayers(customCount);
        }
    }

    // --- NEW: Notify admins when they join if an update is available ---
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // The update checker will handle the permission check and notification logic
        plugin.getUpdateChecker().notify(event.getPlayer());
    }
}