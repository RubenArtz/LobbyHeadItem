package ruben_artz.lobby.events.bow;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.addColor;

import java.util.Objects;

public class PlayerSwapHandItems implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHandItem = event.getMainHandItem();
        ItemStack offHandItem = event.getOffHandItem();

        try {
            if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
                if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS"))) return;

                final String titleBow = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_BOW.BOW.NAME")));

                if ((mainHandItem != null && mainHandItem.hasItemMeta() && Objects.requireNonNull(mainHandItem.getItemMeta()).hasDisplayName() &&
                        (mainHandItem.getItemMeta().getDisplayName().equals(titleBow))) ||
                        (offHandItem != null && offHandItem.hasItemMeta() && Objects.requireNonNull(offHandItem.getItemMeta()).hasDisplayName() &&
                                (offHandItem.getItemMeta().getDisplayName().equals(titleBow)))) {
                    event.setCancelled(true);
                }
            }
        } catch (NullPointerException ignored) {}
    }
}