package ruben_artz.lobby.events.head;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.addColor;

import java.util.Objects;

public class PlayerDropItem implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItemDrop().getItemStack();
        final String getDisplayName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS")))
                return;

            final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_HEAD.PLAYER.ITEM.NAME")));

            if ((itemStack.hasItemMeta()) &&
                    (getDisplayName.equalsIgnoreCase(titleName)) &&
                    (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.NO_DROP_ITEM"))) {
                if (!player.hasPermission("LobbyHeadItem.Admin")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}