package ruben_artz.lobby.events.bow;

import com.cryptomorin.xseries.XSound;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;

public class playerTeleport implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileHitEvent(ProjectileHitEvent event) {
        final Player player = (Player) event.getEntity().getShooter();
        final Location location = event.getEntity().getLocation();
        location.setYaw(player != null ? player.getLocation().getYaw() : 0);
        location.setPitch(player != null ? player.getLocation().getPitch() : 0);

        if (!(event.getEntity().getShooter() instanceof Player)) return;
        if (!(event.getEntity() instanceof Arrow)) return;

        if (player != null) plugin.playerUUIDs.add(player.getUniqueId());
        if (player != null) player.teleport(location);
        if (player != null) XSound.play(player, plugin.getConfiguration().getString("PLAYER_BOW.CONFIGURATION.SOUND_TELEPORT"));
        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.PARTICLES.ENABLED")) {
            plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.PARTICLES.LIST").forEach(s -> ProjectUtils.sendParticles(player, Particle.valueOf(s)));
        }
        ProjectUtils.syncTaskLater(50L, () -> plugin.playerUUIDs.removeIf(u -> u.equals(event.getEntity().getUniqueId())));

        if (event.getEntityType() == EntityType.ARROW) event.getEntity().remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamageEvent(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player) && !(event.getEntity() instanceof Pig) && !(event.getEntity() instanceof Horse)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;

        if (plugin.playerUUIDs.stream().anyMatch(u -> u.equals(event.getEntity().getUniqueId()))) {
            event.setCancelled(true);
            plugin.playerUUIDs.removeIf(u -> u.equals(event.getEntity().getUniqueId()));
        }
    }
}
