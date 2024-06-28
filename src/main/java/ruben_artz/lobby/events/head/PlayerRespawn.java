package ruben_artz.lobby.events.head;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.generateItems;

public class PlayerRespawn implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.GIVE_ON_RESPAWN")) {
                generateItems.setupHead(player);
            }
        }
    }
}