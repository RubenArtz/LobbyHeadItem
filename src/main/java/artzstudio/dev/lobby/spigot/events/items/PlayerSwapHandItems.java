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
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerSwapHandItems implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = event.getMainHandItem();
        ItemStack offHandItem = event.getOffHandItem();

        var itemsSection = plugin.getItems().getSection("ITEMS");
        if (itemsSection == null) return;

        for (String key : itemsSection.getRoutesAsStrings(false)) {
            var itemSection = itemsSection.getSection(key);
            if (itemSection == null) continue;

            if (!itemSection.getBoolean("ENABLED")) continue;
            if (!ProjectUtils.getWorldsItems(player, itemSection.getStringList("WORLDS"))) return;

            String name = itemSection.getString("NAME");
            if (name == null) continue;

            String titleName = addColor.addColors(
                    player,
                    ProjectUtils.placeholderReplace(player, name)
            );

            boolean mainMatches = mainHandItem != null
                    && mainHandItem.hasItemMeta()
                    && mainHandItem.getItemMeta() != null
                    && mainHandItem.getItemMeta().hasDisplayName()
                    && mainHandItem.getItemMeta().getDisplayName().equals(titleName);

            boolean offMatches = offHandItem != null
                    && offHandItem.hasItemMeta()
                    && offHandItem.getItemMeta() != null
                    && offHandItem.getItemMeta().hasDisplayName()
                    && offHandItem.getItemMeta().getDisplayName().equals(titleName);

            if (mainMatches || offMatches) {
                event.setCancelled(true);
            }
        }
    }
}
