package ruben_artz.lobby.events.bow;

import org.bukkit.entity.Horse;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import ruben_artz.lobby.Lobby;

public class EntityDamage implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

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