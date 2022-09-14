package ruben_artz.lobby.events.head;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.events.utils.generateItems;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.addColor;

import java.util.Objects;

@SuppressWarnings("deprecation")
public class headManager implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Action action = event.getAction();
        final ItemStack inHand = player.getInventory().getItemInHand();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS"))) return;

            final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_HEAD.PLAYER.ITEM.NAME")));
            final int itemIndex = plugin.getConfiguration().getInt("PLAYER_HEAD.PLAYER.ITEM.SLOT") - 1;
            final ItemStack correctSlot = player.getInventory().getItem(itemIndex);

            if (action.equals(Action.RIGHT_CLICK_AIR) ||
                    action.equals(Action.RIGHT_CLICK_BLOCK) ||
                    action.equals(Action.LEFT_CLICK_AIR) ||
                    action.equals(Action.LEFT_CLICK_BLOCK)) {
                if (player.getInventory().getItemInHand().getItemMeta() != null &&
                        player.getInventory().getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(titleName) &&
                        correctSlot != null && correctSlot.equals(inHand)) {
                    ProjectUtils.sendCommands(
                            player,
                            Objects.requireNonNull(plugin.getConfiguration().getString("PLAYER_HEAD.CONFIGURATION.COMMANDS.TYPE")),
                            plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.COMMANDS.COMMAND")
                    );
                    ProjectUtils.sendSound(
                            player,
                            plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.SOUNDS.ENABLED"),
                            plugin.getConfiguration().getString("PLAYER_HEAD.CONFIGURATION.SOUNDS.SOUND")
                    );
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlaceEvent(BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS"))) return;

            final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_HEAD.PLAYER.ITEM.NAME")));

            if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.BLOCK_PLACE")) {
                if (event.getItemInHand().getItemMeta() != null && event.getItemInHand().getItemMeta().getDisplayName().equalsIgnoreCase(titleName)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS"))) return;

            final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_HEAD.PLAYER.ITEM.NAME")));

            if ((event.getCurrentItem() != null) &&
                    (event.getCurrentItem().getItemMeta() != null) &&
                    (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(titleName)) &&
                    (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.MOVE_DISABLE"))) {
                if (!player.hasPermission("LobbyHeadItem.Admin")) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItemDrop().getItemStack();
        final String getDisplayName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS"))) return;

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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.GIVE_ON_RESPAWN")) {
                generateItems.setupHead(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        final Player player = event.getEntity();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            if (!ProjectUtils.getWorldsItems(player, plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS"))) return;

            final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getConfiguration().getString("PLAYER_HEAD.PLAYER.ITEM.NAME")));

            if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.NO_DROP_ON_DEATH")) {
                event.getDrops().removeIf(i -> {
                    Objects.requireNonNull(i.getItemMeta()).getDisplayName();
                    return i.getItemMeta().getDisplayName().equalsIgnoreCase(titleName);
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final String world = player.getWorld().getName();

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.ENABLED")) {
            plugin.getConfiguration().getStringList("PLAYER_HEAD.CONFIGURATION.WORLDS").forEach(action -> {
                if (world.equalsIgnoreCase(action)) {
                    ProjectUtils.scheduleSyncDelayedTask(10L, () -> generateItems.setupHead(player));
                    return;
                }
                if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.REMOVE_WHEN_CHANGING_THE_WORLD")) {
                    if (XMaterial.PLAYER_HEAD.parseMaterial() != null) {
                        player.getInventory().remove(XMaterial.PLAYER_HEAD.parseMaterial());
                    }
                }
            });
        }
    }
}
