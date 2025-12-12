package ruben_artz.lobby.events.items;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.generateItems;

import java.util.Objects;

public class PlayerChangedWorld implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);


    @EventHandler(priority = EventPriority.HIGH)
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final String world = player.getWorld().getName();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {

                try {
                    plugin.getItems().getStringList("ITEMS." + key + ".WORLDS").forEach(action -> {
                        if (world.equalsIgnoreCase(action)) {
                            ProjectUtils.runTaskLater(10L, () -> generateItems.setupItems(player));
                            return;
                        }
                        if (plugin.getItems().getBoolean("ITEMS." + key + ".SETTINGS.REMOVE_WHEN_CHANGING_THE_WORLD")) {
                            player.getInventory().remove(Objects.requireNonNull(XMaterial.valueOf(plugin.getItems().getString("ITEMS." + key + ".ITEM")).get()));
                        }
                    });
                } catch (NullPointerException ignored) {
                }

            }
        }
    }
}