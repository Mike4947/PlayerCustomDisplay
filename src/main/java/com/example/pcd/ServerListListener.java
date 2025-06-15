package com.example.pcd;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent.ListedPlayerInfo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;
import java.util.UUID;

public class ServerListListener implements Listener {

    private final main plugin;

    public ServerListListener(main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerListPing(PaperServerListPingEvent event) {
        int baseCount = plugin.getCustomPlayerCount();
        int onlinePlayers = plugin.getServer().getOnlinePlayers().size();
        // Calculate the number that will be displayed for the online player count
        int dynamicPlayerCount = baseCount >= 0 ? baseCount + onlinePlayers : onlinePlayers;

        if (baseCount >= 0) {
            event.setNumPlayers(dynamicPlayerCount);
        }

        int maxCount = plugin.getCustomMaxPlayers();
        // Calculate the number that will be displayed for the max player count
        int displayedMax = maxCount >= 0 ? maxCount : event.getMaxPlayers();

        if (maxCount >= 0) {
            event.setMaxPlayers(displayedMax);
        }

        List<String> hoverList = plugin.getCustomHoverList();
        if (hoverList != null && !hoverList.isEmpty()) {
            List<ListedPlayerInfo> sample = event.getListedPlayers();
            sample.clear();

            for (String line : hoverList) {
                // --- PLACEHOLDER REPLACEMENT LOGIC ---
                String processedLine = line
                        .replace("%online%", String.valueOf(dynamicPlayerCount))
                        .replace("%max%", String.valueOf(displayedMax))
                        .replace("%base_count%", String.valueOf(baseCount));

                ListedPlayerInfo info = new ListedPlayerInfo(processedLine, UUID.randomUUID());
                sample.add(info);
            }
        }
    }
}