package ruben_artz.lobby.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import ruben_artz.lobby.events.utils.generateItems;

public class playerJoin implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        /*
        items
         */
        generateItems.setupItems(player);
        /*
        head Player
         */
        generateItems.setupHead(player);
        /*
        bow & arrow
         */
        generateItems.setupBow(player);
    }
}
