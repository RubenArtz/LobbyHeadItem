package ruben_artz.lobby.utils;

import org.bukkit.entity.Player;
import ruben_artz.lobby.Lobby;

import java.util.Objects;

public class generateItems {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    public static void setupItems(final Player player) {
        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList("ITEMS." + key + ".WORLDS"))) return;
                ProjectUtils.setItem(
                        player,
                        plugin.getItems().getInt("ITEMS." + key + ".SLOT"),
                        plugin.getItems().getString("ITEMS." + key + ".ITEM"),
                        plugin.getItems().getString("ITEMS." + key + ".NAME"),
                        plugin.getItems().getStringList("ITEMS." + key + ".LORE")
                );
            }
        }
    }

    public static void setupHead(final Player player) {
        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS"))) return;
            ProjectUtils.setSkullOwner(
                    player,
                    plugin.getConfiguration().getInt("PLAYER_HEAD.PLAYER.ITEM.SLOT"),
                    plugin.getConfiguration().getString("PLAYER_HEAD.PLAYER.ITEM.NAME"),
                    plugin.getConfiguration().getStringList("PLAYER_HEAD.PLAYER.ITEM.LORE")
            );
        }
    }

    public static void setupBow(Player player) {
        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS"))) return;
            ProjectUtils.setBow(
                    player,
                    plugin.getConfiguration().getInt("PLAYER_BOW.BOW.SLOT"),
                    plugin.getConfiguration().getString("PLAYER_BOW.BOW.ITEM"),
                    plugin.getConfiguration().getString("PLAYER_BOW.BOW.NAME"),
                    plugin.getConfiguration().getStringList("PLAYER_BOW.BOW.LORE"));
            ProjectUtils.setArrow(
                    player,
                    plugin.getConfiguration().getInt("PLAYER_BOW.ARROW.SLOT"),
                    plugin.getConfiguration().getString("PLAYER_BOW.ARROW.ITEM"),
                    plugin.getConfiguration().getString("PLAYER_BOW.ARROW.NAME"));
        }
    }
}
