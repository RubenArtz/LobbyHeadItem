package ruben_artz.lobby.events.bow;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.addColor;

public class PlayerInventoryClick implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        final String titleBow = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_BOW.BOW.NAME")));
        final String titleArrow = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_BOW.ARROW.NAME")));

        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS"))) return;

            try {
                if ((event.getCurrentItem() != null) &&
                        (event.getCurrentItem().getItemMeta() != null) &&
                        (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(titleBow)) ||
                        (event.getCurrentItem() != null) &&
                                (event.getCurrentItem().getItemMeta() != null) &&
                                (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(titleArrow)) &&
                                plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.MOVE_DISABLE")) {
                    if (!player.hasPermission("LobbyHeadItem.Admin")) {
                        event.setCancelled(true);
                    }
                }
            } catch (NullPointerException ignored) {}

        }
    }
}