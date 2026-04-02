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

        var items = plugin.getItems().getSection("ITEMS");
        if (items == null) return;

        for (String key : items.getRoutesAsStrings(false)) {
            String path = "ITEMS." + key;

            if (!plugin.getItems().getBoolean(path + ".ENABLED")) continue;

            var worlds = plugin.getItems().getStringList(path + ".WORLDS");
            boolean allowedInWorld = worlds.stream().anyMatch(w -> w.equalsIgnoreCase(world));

            if (allowedInWorld) {
                ProjectUtils.runTaskLater(10L, () -> generateItems.setupItems(player));
                continue;
            }

            if (!plugin.getItems().getBoolean(path + ".SETTINGS.REMOVE_WHEN_CHANGING_THE_WORLD")) continue;

            String itemName = plugin.getItems().getString(path + ".ITEM");
            if (itemName == null) continue;

            XMaterial.matchXMaterial(itemName).ifPresent(xMaterial -> {
                var material = xMaterial.get();
                if (material != null) {
                    player.getInventory().remove(material);
                }
            });
        }
    }
}