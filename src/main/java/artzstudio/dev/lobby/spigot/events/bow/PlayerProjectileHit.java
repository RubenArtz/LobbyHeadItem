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
import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class PlayerProjectileHit implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHitEvent(ProjectileHitEvent event) {

        if (!plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) return;

        if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player) {
            final Player player = (Player) event.getEntity().getShooter();
            final Location location = event.getEntity().getLocation();
            location.setYaw(player != null ? player.getLocation().getYaw() : 0);
            location.setPitch(player != null ? player.getLocation().getPitch() : 0);

            if (player != null) plugin.playerUUIDs.add(player.getUniqueId());
            if (player != null) player.teleport(location);
            if (player != null)
                XSound.play(plugin.getConfiguration().getString("PLAYER_BOW.CONFIGURATION.SOUND_TELEPORT"), soundPlayer -> soundPlayer.forPlayers(player));

            if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.PARTICLES.ENABLED")) {

                try {
                    plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.PARTICLES.LIST").forEach(s -> ProjectUtils.sendParticles(player, Particle.valueOf(s)));
                } catch (NoClassDefFoundError ignored) {
                }

            }
            ProjectUtils.runTaskLater(50L, () -> plugin.playerUUIDs.removeIf(u -> u.equals(event.getEntity().getUniqueId())));

            if (event.getEntityType() == EntityType.ARROW) event.getEntity().remove();
        }
    }
}