package ruben_artz.lobby.events.bow;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.events.utils.generateItems;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.addColor;

import java.util.Objects;

public class bowManager implements Listener {
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();
        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS"))) return;

            if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.GIVE_ON_RESPAWN")) {
                generateItems.setupBow(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        final Player player = event.getEntity();
        final String titleBow = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_BOW.BOW.NAME")));
        final String titleArrow = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_BOW.ARROW.NAME")));
        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_BOW.CONFIGURATION.WORLDS"))) return;

            if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.NO_DROP_ON_DEATH")) {

                try {
                    event.getDrops().removeIf(i -> {
                        Objects.requireNonNull(i.getItemMeta()).getDisplayName();
                        return i.getItemMeta().getDisplayName().equalsIgnoreCase(titleBow)
                                || i.getItemMeta().getDisplayName().equalsIgnoreCase(titleArrow);
                    });
                } catch (NullPointerException ignored) {}

            }
        }
    }

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
