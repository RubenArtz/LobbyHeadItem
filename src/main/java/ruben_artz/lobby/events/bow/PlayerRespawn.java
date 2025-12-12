package ruben_artz.lobby.events.bow;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.generateItems;

public class PlayerRespawn implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS")))
                return;

            if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.GIVE_ON_RESPAWN")) {
                generateItems.setupBow(player);
            }
        }
    }
}