package com.example.pcd;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

public class FakePlayerManager {

    private final main plugin;
    private final ProtocolManager protocolManager;
    private final List<WrappedGameProfile> fakePlayers = new ArrayList<>();
    private final Random random = new Random();

    private static final Map<String, UUID> NAME_TO_UUID_SKINS = Map.of(
            "Notch", UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"),
            "jeb_", UUID.fromString("853c80ef-3c37-49fd-aa49-938b674adae6"),
            "MHF_Steve", UUID.fromString("606e2ff0-ed77-4842-9d6c-e1d3321c7838"),
            "MHF_Alex", UUID.fromString("b8b715fb-4146-4635-a74f-a035775ac2a9"),
            "Technoblade", UUID.fromString("b876ec32-e396-476b-a115-8438d83c67d4")
    );
    private final List<String> names = new ArrayList<>(NAME_TO_UUID_SKINS.keySet());

    public FakePlayerManager(main plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void updateFakePlayers(int count) {
        // --- ADDED LOGGING ---
        plugin.getLogger().info("Received request to update fake players to a count of: " + count);

        if (!fakePlayers.isEmpty()) {
            plugin.getLogger().info("Removing " + fakePlayers.size() + " existing fake players...");
            broadcastPacket(createPlayerPacket(EnumWrappers.PlayerInfoAction.REMOVE_PLAYER, fakePlayers));
            plugin.getLogger().info("...Removal packet sent.");
        }

        fakePlayers.clear();

        if (count <= 0) {
            plugin.getLogger().info("Count is " + count + ", no new players will be added.");
            return;
        }

        plugin.getLogger().info("Generating " + count + " new fake player profiles...");
        for (int i = 0; i < count; i++) {
            fakePlayers.add(createRandomGameProfile());
        }
        plugin.getLogger().info("...Generated " + fakePlayers.size() + " profiles.");

        plugin.getLogger().info("Sending ADD_PLAYER packet for new fake players...");
        broadcastPacket(createPlayerPacket(EnumWrappers.PlayerInfoAction.ADD_PLAYER, fakePlayers));
        plugin.getLogger().info("...ADD_PLAYER packet sent.");
    }

    public void sendPlayersTo(Player player) {
        if (!fakePlayers.isEmpty()) {
            try {
                plugin.getLogger().info("Sending fake player list to joining player: " + player.getName());
                protocolManager.sendServerPacket(player, createPlayerPacket(EnumWrappers.PlayerInfoAction.ADD_PLAYER, fakePlayers));
            } catch (Exception e) {
                // --- IMPROVED ERROR LOGGING ---
                plugin.getLogger().log(Level.SEVERE, "Could not send fake player packet to " + player.getName(), e);
            }
        }
    }

    private WrappedGameProfile createRandomGameProfile() {
        String name = names.get(random.nextInt(names.size()));
        UUID skinUUID = NAME_TO_UUID_SKINS.get(name);

        UUID uniqueId = UUID.randomUUID();
        String uniqueName = name + "§r§" + random.nextInt(9) + "§r";
        WrappedGameProfile newProfile = new WrappedGameProfile(uniqueId, uniqueName);

        OfflinePlayer skinOwner = plugin.getServer().getOfflinePlayer(skinUUID);
        PlayerProfile skinOwnerProfile = skinOwner.getPlayerProfile();

        if (!skinOwnerProfile.isComplete()) {
            skinOwnerProfile.complete();
        }

        for (ProfileProperty property : skinOwnerProfile.getProperties()) {
            if (property.getName().equals("textures")) {
                newProfile.getProperties().put("textures", new WrappedSignedProperty(property.getName(), property.getValue(), property.getSignature()));
                break;
            }
        }
        return newProfile;
    }

    private PacketContainer createPlayerPacket(EnumWrappers.PlayerInfoAction action, List<WrappedGameProfile> profiles) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, action);

        List<PlayerInfoData> dataList = new ArrayList<>();
        for (WrappedGameProfile profile : profiles) {
            dataList.add(new PlayerInfoData(
                    profile,
                    random.nextInt(100) + 20,
                    EnumWrappers.NativeGameMode.SURVIVAL,
                    WrappedChatComponent.fromText(profile.getName())
            ));
        }
        packet.getPlayerInfoDataLists().write(0, dataList);
        return packet;
    }

    private void broadcastPacket(PacketContainer packet) {
        plugin.getLogger().info("Broadcasting packet to " + plugin.getServer().getOnlinePlayers().size() + " players.");
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            try {
                protocolManager.sendServerPacket(player, packet);
            } catch (Exception e) {
                // --- IMPROVED ERROR LOGGING ---
                plugin.getLogger().log(Level.SEVERE, "Could not broadcast fake player packet to " + player.getName(), e);
            }
        }
    }
}