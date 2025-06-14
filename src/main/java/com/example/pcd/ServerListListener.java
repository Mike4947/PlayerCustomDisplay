package com.example.pcd; // <-- Updated package

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ServerListListener implements Listener {

    // Now correctly refers to the 'main' class
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
}