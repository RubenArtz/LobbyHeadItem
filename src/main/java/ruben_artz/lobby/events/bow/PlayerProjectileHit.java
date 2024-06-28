package ruben_artz.lobby.events.bow;

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
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;

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
            if (player != null) XSound.play(plugin.getConfiguration().getString("PLAYER_BOW.CONFIGURATION.SOUND_TELEPORT"), soundPlayer -> soundPlayer.forPlayers(player));

            if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.PARTICLES.ENABLED")) {

                try {
                    plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.PARTICLES.LIST").forEach(s -> ProjectUtils.sendParticles(player, Particle.valueOf(s)));
                } catch (NoClassDefFoundError ignored) {}

            }
            ProjectUtils.runTaskLater(50L, () -> plugin.playerUUIDs.removeIf(u -> u.equals(event.getEntity().getUniqueId())));

            if (event.getEntityType() == EntityType.ARROW) event.getEntity().remove();
        }
    }
}