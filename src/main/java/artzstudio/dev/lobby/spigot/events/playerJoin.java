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

package artzstudio.dev.lobby.spigot.events;

import artzstudio.dev.lobby.spigot.utils.generateItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class playerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        /*
        items
         */
        generateItems.setupItems(player);

        /*
        head Player
         */
        generateItems.setupHead(player);

        /*
        bow & arrow
         */
        generateItems.setupBow(player);

        /*
        fishing
         */
        generateItems.setupGrappling(player);
    }
}
