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
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInteract implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final ItemStack inHand = player.getInventory().getItemInHand();

        var itemsSection = plugin.getItems().getSection("ITEMS");
        if (itemsSection == null) return;

        for (String key : itemsSection.getRoutesAsStrings(false)) {
            String path = "ITEMS." + key;

            if (!plugin.getItems().getBoolean(path + ".ENABLED")) continue;
            if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList(path + ".WORLDS"))) return;

            String name = plugin.getItems().getString(path + ".NAME");
            if (name == null) continue;

            String titleName = addColor.addColors(
                    player,
                    ProjectUtils.placeholderReplace(player, name)
            );

            int itemIndex = plugin.getItems().getInt(path + ".SLOT") - 1;
            if (itemIndex < 0) continue;

            ItemStack correctSlot = player.getInventory().getItem(itemIndex);
            ItemStack handItem = player.getInventory().getItemInHand();

            boolean validAction =
                    action == Action.RIGHT_CLICK_AIR ||
                            action == Action.RIGHT_CLICK_BLOCK ||
                            action == Action.LEFT_CLICK_AIR ||
                            action == Action.LEFT_CLICK_BLOCK;

            if (!validAction) continue;
            if (!handItem.hasItemMeta()) continue;
            if (handItem.getItemMeta() == null || !handItem.getItemMeta().hasDisplayName()) continue;
            if (!handItem.getItemMeta().getDisplayName().equalsIgnoreCase(titleName)) continue;
            if (correctSlot == null || !correctSlot.equals(inHand)) continue;

            String commandType = plugin.getItems().getString(path + ".COMMANDS.TYPE");
            if (commandType == null) continue;

            ProjectUtils.sendCommands(
                    player,
                    commandType,
                    plugin.getItems().getStringList(path + ".COMMANDS.COMMAND")
            );

            ProjectUtils.sendSound(
                    player,
                    plugin.getItems().getBoolean(path + ".SOUNDS.ENABLED"),
                    plugin.getItems().getString(path + ".SOUNDS.SOUND")
            );
        }
    }
}