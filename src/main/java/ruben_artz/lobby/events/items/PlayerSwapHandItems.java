package ruben_artz.lobby.events.items;

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
            for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
                if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                    if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList("ITEMS." + key + ".WORLDS")))
                        return;

                    final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getItems().getString("ITEMS." + key + ".NAME")));

                    if ((mainHandItem != null && mainHandItem.hasItemMeta() && Objects.requireNonNull(mainHandItem.getItemMeta()).hasDisplayName() &&
                            (mainHandItem.getItemMeta().getDisplayName().equals(titleName))) ||
                            (offHandItem != null && offHandItem.hasItemMeta() && Objects.requireNonNull(offHandItem.getItemMeta()).hasDisplayName() &&
                                    (offHandItem.getItemMeta().getDisplayName().equals(titleName)))) {

                        event.setCancelled(true);
                    }
                }
            }
        } catch (NullPointerException ignored) {
        }
    }
}