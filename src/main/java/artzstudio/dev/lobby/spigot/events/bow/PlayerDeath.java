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
import artzstudio.dev.lobby.spigot.utils.addColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Objects;

public class PlayerDeath implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final String titleBow = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_BOW.BOW.NAME")));
        final String titleArrow = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_BOW.ARROW.NAME")));
        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS")))
                return;

            if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.NO_DROP_ON_DEATH")) {

                try {
                    event.getDrops().removeIf(i -> {
                        Objects.requireNonNull(i.getItemMeta()).getDisplayName();
                        return i.getItemMeta().getDisplayName().equalsIgnoreCase(titleBow)
                                || i.getItemMeta().getDisplayName().equalsIgnoreCase(titleArrow);
                    });
                } catch (NullPointerException ignored) {
                }

            }
        }
    }
}