package com.example.pcd;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

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

    // --- METHOD COMPLETELY REWRITTEN ---
    public void updateFakePlayers(int count) {
        // 1. REMOVE old fake players using the modern REMOVE packet
        if (!fakePlayers.isEmpty()) {
            PacketContainer removePacket = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
            List<UUID> removeUuids = new ArrayList<>();
            for (WrappedGameProfile profile : fakePlayers) {
                removeUuids.add(profile.getUUID());
            }
            removePacket.getUUIDLists().write(0, removeUuids);
            broadcastPacket(removePacket);
        }

        fakePlayers.clear();

        if (count <= 0) {
            return;
        }

        // 2. CREATE new fake player data
        for (int i = 0; i < count; i++) {
            fakePlayers.add(createRandomGameProfile());
        }

        // 3. ADD new fake players using the modern UPDATE packet
        PacketContainer addPacket = createAddPlayerPacket(fakePlayers);
        broadcastPacket(addPacket);
    }

    // --- METHOD REWRITTEN ---
    public void sendPlayersTo(Player player) {
        if (!fakePlayers.isEmpty()) {
            try {
                protocolManager.sendServerPacket(player, createAddPlayerPacket(fakePlayers));
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to send fake player packet to " + player.getName());
            }
        }
    }

    // --- NEW HELPER METHOD ---
    private PacketContainer createAddPlayerPacket(List<WrappedGameProfile> profiles) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_UPDATE);

        // Set the packet to perform the ADD_PLAYER action
        packet.getPlayerInfoActions().write(0, EnumSet.of(EnumWrappers.PlayerInfoAction.ADD_PLAYER));

        List<PlayerInfoData> dataList = new ArrayList<>();
        for (WrappedGameProfile profile : profiles) {
            // The constructor for PlayerInfoData has changed in modern versions
            dataList.add(new PlayerInfoData(
                    profile,
                    random.nextInt(100) + 20, // Ping
                    EnumWrappers.NativeGameMode.SURVIVAL,
                    WrappedChatComponent.fromText(profile.getName())
            ));
        }
        packet.getPlayerInfoDataLists().write(0, dataList);
        return packet;
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

    private void broadcastPacket(PacketContainer packet) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            try {
                protocolManager.sendServerPacket(player, packet);
            } catch (Exception e) {
                plugin.getLogger().warning("Failed to send fake player packet to " + player.getName());
            }
        }
    }
}