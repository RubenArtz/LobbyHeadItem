/*
 *     Copyright (C) 2026 Ruben_Artz (Artz Studio)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package artzstudio.dev.lobby.spigot.utils;

import artzstudio.dev.lobby.spigot.Lobby;
import org.bukkit.entity.Player;

public class generateItems {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    public static void setupItems(final Player player) {
        var itemsSection = plugin.getItems().getSection("ITEMS");
        if (itemsSection == null) return;

        for (String key : itemsSection.getRoutesAsStrings(false)) {
            var itemSection = itemsSection.getSection(key);
            if (itemSection == null) continue;

            if (!itemSection.getBoolean("ENABLED")) continue;
            if (!ProjectUtils.getWorldsItems(player, itemSection.getStringList("WORLDS"))) return;

            ProjectUtils.setItem(
                    player,
                    itemSection.getInt("SLOT"),
                    itemSection.getString("ITEM"),
                    itemSection.getString("NAME"),
                    itemSection.getStringList("LORE")
            );
        }
    }

    public static void setupHead(final Player player) {
        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS")))
                return;
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
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS")))
                return;
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

    public static void setupGrappling(Player player) {
        if (plugin.getConfiguration().getBoolean("GRAPPLING_HOOK.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("GRAPPLING_HOOK.CONFIGURATION.WORLDS")))
                return;

            ProjectUtils.setItem(
                    player,
                    plugin.getConfiguration().getInt("GRAPPLING_HOOK.ITEM.SLOT"),
                    plugin.getConfiguration().getString("GRAPPLING_HOOK.ITEM.MATERIAL"),
                    plugin.getConfiguration().getString("GRAPPLING_HOOK.ITEM.NAME"),
                    plugin.getConfiguration().getStringList("GRAPPLING_HOOK.ITEM.LORE")
            );
        }
    }
}
