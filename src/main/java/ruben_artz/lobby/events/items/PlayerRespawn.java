package ruben_artz.lobby.events.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.generateItems;

import java.util.Objects;

public class PlayerRespawn implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                if (plugin.getItems().getBoolean("ITEMS." + key + ".SETTINGS.GIVE_ON_RESPAWN")) {
                    generateItems.setupItems(player);
                }
            }
        }
    }
}