package ruben_artz.lobby.events.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.addColor;

import java.util.Objects;

public class PlayerInteract implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final ItemStack inHand = player.getInventory().getItemInHand();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList("ITEMS." + key + ".WORLDS"))) return;

                final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getItems().getString("ITEMS." + key + ".NAME")));
                final int itemIndex = plugin.getItems().getInt("ITEMS." + key + ".SLOT") - 1;
                final ItemStack correctSlot = player.getInventory().getItem(itemIndex);

                try {
                    if (action.equals(Action.RIGHT_CLICK_AIR) ||
                            action.equals(Action.RIGHT_CLICK_BLOCK) ||
                            action.equals(Action.LEFT_CLICK_AIR) ||
                            action.equals(Action.LEFT_CLICK_BLOCK)) {
                        if (player.getInventory().getItemInHand().getItemMeta() != null &&
                                player.getInventory().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(titleName) &&
                                correctSlot != null && correctSlot.equals(inHand)) {
                            ProjectUtils.sendCommands(
                                    player,
                                    Objects.requireNonNull(plugin.getItems().getString("ITEMS." + key + ".COMMANDS.TYPE")),
                                    plugin.getItems().getStringList("ITEMS." + key + ".COMMANDS.COMMAND")
                            );
                            ProjectUtils.sendSound(
                                    player,
                                    plugin.getItems().getBoolean("ITEMS." + key + ".SOUNDS.ENABLED"),
                                    plugin.getItems().getString("ITEMS." + key + ".SOUNDS.SOUND")
                            );
                        }
                    }
                } catch (NullPointerException ignored) {}

            }
        }
    }
}