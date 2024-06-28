package ruben_artz.lobby.events.head;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.addColor;

public class PlayerBlockPlace implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS"))) return;

            final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_HEAD.PLAYER.ITEM.NAME")));

            try {
                if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.BLOCK_PLACE")) {
                    if (event.getItemInHand().getItemMeta() != null && event.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(titleName)) {
                        event.setCancelled(true);
                    }
                }
            } catch (NullPointerException ignored) {}

        }
    }
}