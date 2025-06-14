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
        // Get the base number from the config
        int baseCount = plugin.getCustomPlayerCount();

        // If the feature is enabled (not -1)
        if (baseCount >= 0) {
            // --- MODIFIED LOGIC ---
            // Get the actual number of players currently online
            int onlinePlayers = plugin.getServer().getOnlinePlayers().size();
            // Calculate the new dynamic count
            int dynamicPlayerCount = baseCount + onlinePlayers;
            // Set the player count in the server list to our new dynamic number
            event.setNumPlayers(dynamicPlayerCount);
        }
        // If baseCount is -1, this block is skipped, and the server shows the real player count.
    }
}