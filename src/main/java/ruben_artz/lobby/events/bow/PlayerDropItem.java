package ruben_artz.lobby.events.bow;

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

        final String titleBow = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_BOW.BOW.NAME")));
        final String titleArrow = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_BOW.ARROW.NAME")));

        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS"))) return;

            try {
                if ((itemStack.hasItemMeta()) &&
                        (getDisplayName.equalsIgnoreCase(titleBow)) ||
                        (getDisplayName.equalsIgnoreCase(titleArrow)) &&
                                plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.NO_DROP_ITEM")) {
                    if (!player.hasPermission("LobbyHeadItem.Admin")) {
                        event.setCancelled(true);
                    }
                }
            } catch (NullPointerException ignored) {}

        }
    }
}