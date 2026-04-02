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

package artzstudio.dev.lobby.spigot.events.fishing;

import artzstudio.dev.lobby.spigot.Lobby;
import artzstudio.dev.lobby.spigot.utils.ProjectUtils;
import artzstudio.dev.lobby.spigot.utils.addColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class GrapplingSwapHandItems implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = event.getMainHandItem();
        ItemStack offHandItem = event.getOffHandItem();

        try {
            if (plugin.getConfiguration().getBoolean("GRAPPLING_HOOK.ENABLED")) {
                if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("GRAPPLING_HOOK.CONFIGURATION.WORLDS")))
                    return;

                final String titleHook = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("GRAPPLING_HOOK.ITEM.NAME")));

                if ((mainHandItem != null && mainHandItem.hasItemMeta() && Objects.requireNonNull(mainHandItem.getItemMeta()).hasDisplayName() &&
                        (mainHandItem.getItemMeta().getDisplayName().equals(titleHook))) ||
                        (offHandItem != null && offHandItem.hasItemMeta() && Objects.requireNonNull(offHandItem.getItemMeta()).hasDisplayName() &&
                                (offHandItem.getItemMeta().getDisplayName().equals(titleHook)))) {
                    event.setCancelled(true);
                }
            }
        } catch (NullPointerException ignored) {
        }
    }
}