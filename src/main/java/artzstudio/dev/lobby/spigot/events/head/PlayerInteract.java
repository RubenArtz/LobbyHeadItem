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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class PlayerInteract implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final ItemStack inHand = player.getInventory().getItemInHand();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS")))
                return;

            final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_HEAD.PLAYER.ITEM.NAME")));
            final int itemIndex = plugin.getConfiguration().getInt("PLAYER_HEAD.PLAYER.ITEM.SLOT") - 1;
            final ItemStack correctSlot = player.getInventory().getItem(itemIndex);

            try {
                if (action.equals(Action.RIGHT_CLICK_AIR) ||
                        action.equals(Action.RIGHT_CLICK_BLOCK) ||
                        action.equals(Action.LEFT_CLICK_AIR) ||
                        action.equals(Action.LEFT_CLICK_BLOCK)) {
                    if (player.getInventory().getItemInHand().getItemMeta() != null &&
                            player.getInventory().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(titleName) &&
                            correctSlot != null && correctSlot.equals(inHand)) {
                        ProjectUtils.sendCommands(
                                player,
                                Objects.requireNonNull(plugin.getConfiguration().getString("PLAYER_HEAD.CONFIGURATION.COMMANDS.TYPE")),
                                plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.COMMANDS.COMMAND")
                        );
                        ProjectUtils.sendSound(
                                player,
                                plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.SOUNDS.ENABLED"),
                                plugin.getConfiguration().getString("PLAYER_HEAD.CONFIGURATION.SOUNDS.SOUND")
                        );
                    }
                }
            } catch (NullPointerException ignored) {
            }

        }
    }
}