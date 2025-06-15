package com.example.pcd;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServerListListener implements Listener {

    private final main plugin;

    public ServerListListener(main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(PaperServerListPingEvent event) {
        // Handle custom online player count
        int baseCount = plugin.getCustomPlayerCount();
        if (baseCount >= 0) {
            int onlinePlayers = plugin.getServer().getOnlinePlayers().size();
            int dynamicPlayerCount = baseCount + onlinePlayers;
            event.setNumPlayers(dynamicPlayerCount);
        }

        // --- NEW: Handle custom max player count ---
        int maxCount = plugin.getCustomMaxPlayers();
        if (maxCount >= 0) {
            event.setMaxPlayers(maxCount);
        }
    }
}