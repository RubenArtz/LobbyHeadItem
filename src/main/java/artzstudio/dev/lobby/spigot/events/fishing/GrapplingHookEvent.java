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
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Objects;

public class GrapplingHookEvent implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerFish(PlayerFishEvent event) {
        final Player player = event.getPlayer();

        if (!plugin.getConfiguration().getBoolean("GRAPPLING_HOOK.ENABLED")) return;

        if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("GRAPPLING_HOOK.CONFIGURATION.WORLDS")))
            return;

        final String titleHook = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("GRAPPLING_HOOK.ITEM.NAME")));
        boolean isHook = false;

        try {
            if (player.getInventory().getItemInMainHand().hasItemMeta() &&
                    Objects.requireNonNull(player.getInventory().getItemInMainHand().getItemMeta()).getDisplayName().equalsIgnoreCase(titleHook)) {
                isHook = true;
            } else if (player.getInventory().getItemInOffHand().hasItemMeta() &&
                    Objects.requireNonNull(player.getInventory().getItemInOffHand().getItemMeta()).getDisplayName().equalsIgnoreCase(titleHook)) {
                isHook = true;
            }
        } catch (NullPointerException ignored) {
        }

        if (!isHook) return;

        if (event.getState() == PlayerFishEvent.State.FISHING) {
            Vector velocity = event.getHook().getVelocity();

            event.getHook().setVelocity(velocity.multiply(3.0));
            return;
        }

        if (event.getState() == PlayerFishEvent.State.IN_GROUND ||
                event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY ||
                event.getState() == PlayerFishEvent.State.REEL_IN) {

            Location playerLoc = player.getLocation();
            Location hookLoc = event.getHook().getLocation();

            Vector direction = hookLoc.toVector().subtract(playerLoc.toVector());

            double distance = playerLoc.distance(hookLoc);
            double multiplier = 0.25;

            if (distance > 20) {
                multiplier = 0.15;
            }

            direction.multiply(multiplier);

            direction.setY(direction.getY() + 0.4);

            if (direction.getY() > 1.5) {
                direction.setY(1.5);
            }

            player.setVelocity(direction);

            plugin.playerUUIDs.add(player.getUniqueId());

            ProjectUtils.runTaskAtEntityLater(player, 60L, () -> plugin.playerUUIDs.remove(player.getUniqueId()));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemDamage(PlayerItemDamageEvent event) {
        if (!plugin.getConfiguration().getBoolean("GRAPPLING_HOOK.ENABLED")) return;

        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if (!item.hasItemMeta()) return;

        final String titleHook = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("GRAPPLING_HOOK.ITEM.NAME")));

        try {
            if (Objects.requireNonNull(item.getItemMeta()).getDisplayName().equalsIgnoreCase(titleHook)) {
                event.setCancelled(true);
            }
        } catch (NullPointerException ignored) {
        }
    }
}