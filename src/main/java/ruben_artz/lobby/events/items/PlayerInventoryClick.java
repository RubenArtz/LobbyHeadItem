package ruben_artz.lobby.events.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.addColor;

import java.util.Objects;

public class PlayerInventoryClick implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList("ITEMS." + key + ".WORLDS")))
                    return;

                final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getItems().getString("ITEMS." + key + ".NAME")));

                try {
                    if ((event.getCurrentItem() != null) &&
                            (event.getCurrentItem().getItemMeta() != null) &&
                            (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(titleName)) &&
                            (plugin.getItems().getBoolean("ITEMS." + key + ".SETTINGS.MOVE_DISABLE"))) {
                        if (!player.hasPermission("LobbyHeadItem.Admin")) {
                            event.setCancelled(true);
                        }
                    }
                } catch (NullPointerException ignored) {
                }

            }
        }
    }
}