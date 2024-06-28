package ruben_artz.lobby.events.bow;

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

        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
            plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS").forEach(action -> {

                try {
                    if (world.equalsIgnoreCase(action)) {
                        ProjectUtils.runTaskLater(10L, () -> generateItems.setupBow(player));
                        return;
                    }
                    if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.REMOVE_WHEN_CHANGING_THE_WORLD")) {
                        if (XMaterial.BOW.parseMaterial() != null) player.getInventory().remove(XMaterial.BOW.parseMaterial());
                        if (XMaterial.ARROW.parseMaterial() != null) player.getInventory().remove(XMaterial.ARROW.parseMaterial());
                    }
                } catch (NullPointerException ignored) {}

            });
        }
    }
}