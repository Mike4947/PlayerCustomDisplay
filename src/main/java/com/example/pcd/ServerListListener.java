package com.example.pcd;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent.ListedPlayerInfo;
import org.bukkit.Bukkit;
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
        if (baseCount >= 0) {
            int online = plugin.getServer().getOnlinePlayers().size();
            event.setNumPlayers(baseCount + online);
        }

        int maxCount = plugin.getCustomMaxPlayers();
        if (maxCount >= 0) {
            event.setMaxPlayers(maxCount);
        }

        List<String> hoverList = plugin.getCustomHoverList();
        if (hoverList != null && !hoverList.isEmpty()) {
            List<ListedPlayerInfo> sample = event.getListedPlayers();
            sample.clear();

            for (String line : hoverList) {
                ListedPlayerInfo info = new ListedPlayerInfo(line, UUID.randomUUID());
                sample.add(info);
            }
        }
    }
}
