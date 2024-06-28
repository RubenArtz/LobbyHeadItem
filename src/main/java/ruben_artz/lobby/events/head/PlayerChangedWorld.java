package ruben_artz.lobby.events.head;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.generateItems;

public class PlayerChangedWorld implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final String world = player.getWorld().getName();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS").forEach(action -> {

                try {
                    if (world.equalsIgnoreCase(action)) {
                        ProjectUtils.runTaskLater(10L, () -> generateItems.setupHead(player));
                        return;
                    }
                    if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.REMOVE_WHEN_CHANGING_THE_WORLD")) {
                        if (XMaterial.PLAYER_HEAD.parseMaterial() != null) {
                            player.getInventory().remove(XMaterial.PLAYER_HEAD.parseMaterial());
                        }
                    }
                } catch (NullPointerException ignored) {}

            });
        }
    }
}