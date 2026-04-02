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

package artzstudio.dev.lobby.spigot.events.bow;

import artzstudio.dev.lobby.spigot.Lobby;
import artzstudio.dev.lobby.spigot.utils.ProjectUtils;
import artzstudio.dev.lobby.spigot.utils.generateItems;
import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class PlayerChangedWorld implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final String world = player.getWorld().getName();

        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
            plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS").forEach(action -> {

                try {
                    if (world.equalsIgnoreCase(action)) {
                        ProjectUtils.runTaskLater(10L, () -> generateItems.setupBow(player));
                        return;
                    }
                    if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.REMOVE_WHEN_CHANGING_THE_WORLD")) {
                        if (XMaterial.BOW.get() != null) player.getInventory().remove(XMaterial.BOW.get());
                        if (XMaterial.ARROW.get() != null) player.getInventory().remove(XMaterial.ARROW.get());
                    }
                } catch (NullPointerException ignored) {
                }

            });
        }
    }
}