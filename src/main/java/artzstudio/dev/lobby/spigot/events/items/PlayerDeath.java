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

package artzstudio.dev.lobby.spigot.events.items;

import artzstudio.dev.lobby.spigot.Lobby;
import artzstudio.dev.lobby.spigot.utils.ProjectUtils;
import artzstudio.dev.lobby.spigot.utils.addColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeath implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        final Player player = event.getEntity();

        var itemsSection = plugin.getItems().getSection("ITEMS");
        if (itemsSection == null) return;

        for (String key : itemsSection.getRoutesAsStrings(false)) {
            String path = "ITEMS." + key;

            if (!plugin.getItems().getBoolean(path + ".ENABLED")) continue;
            if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList(path + ".WORLDS"))) return;

            String itemName = plugin.getItems().getString(path + ".NAME");
            if (itemName == null) continue;

            String titleName = addColor.addColors(
                    player,
                    ProjectUtils.placeholderReplace(player, itemName)
            );

            if (!plugin.getItems().getBoolean(path + ".SETTINGS.NO_DROP_ON_DEATH")) continue;

            event.getDrops().removeIf(item -> {
                if (item == null || !item.hasItemMeta()) return false;
                var meta = item.getItemMeta();
                return meta != null && meta.hasDisplayName()
                        && meta.getDisplayName().equalsIgnoreCase(titleName);
            });
        }
    }
}