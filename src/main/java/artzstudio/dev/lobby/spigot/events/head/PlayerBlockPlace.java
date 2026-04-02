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

package artzstudio.dev.lobby.spigot.events.head;

import artzstudio.dev.lobby.spigot.Lobby;
import artzstudio.dev.lobby.spigot.utils.ProjectUtils;
import artzstudio.dev.lobby.spigot.utils.addColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class PlayerBlockPlace implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS")))
                return;

            final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_HEAD.PLAYER.ITEM.NAME")));

            try {
                if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.BLOCK_PLACE")) {
                    if (event.getItemInHand().getItemMeta() != null && event.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(titleName)) {
                        event.setCancelled(true);
                    }
                }
            } catch (NullPointerException ignored) {
            }

        }
    }
}