package ruben_artz.lobby.events.items;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
public class itemsManager implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

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
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList("ITEMS." + key + ".WORLDS"))) return;

                final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getItems().getString("ITEMS." + key + ".NAME")));

                if ((event.getCurrentItem() != null) &&
                        (event.getCurrentItem().getItemMeta() != null) &&
                        (event.getCurrentItem().getItemMeta().getDisplayName().equalsIgnoreCase(titleName)) &&
                        (plugin.getItems().getBoolean("ITEMS." + key + ".SETTINGS.MOVE_DISABLE"))) {
                    if (!player.hasPermission("LobbyHeadItem.Admin")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final ItemStack itemStack = event.getItemDrop().getItemStack();
        final String getDisplayName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList("ITEMS." + key + ".WORLDS"))) return;

                final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getItems().getString("ITEMS." + key + ".NAME")));

                if ((itemStack.hasItemMeta()) &&
                        (getDisplayName.equalsIgnoreCase(titleName)) &&
                        (plugin.getItems().getBoolean("ITEMS." + key + ".SETTINGS.NO_DROP_ITEM"))) {
                    if (!player.hasPermission("LobbyHeadItem.Admin")) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawnEvent(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                if (plugin.getItems().getBoolean("ITEMS." + key + ".SETTINGS.GIVE_ON_RESPAWN")) {
                    generateItems.setupItems(player);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        final Player player = event.getEntity();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList("ITEMS." + key + ".WORLDS"))) return;

                final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getItems().getString("ITEMS." + key + ".NAME")));

                if (plugin.getItems().getBoolean("ITEMS." + key + ".SETTINGS.NO_DROP_ON_DEATH")) {
                    event.getDrops().removeIf(i -> {
                        Objects.requireNonNull(i.getItemMeta()).getDisplayName();
                        return i.getItemMeta().getDisplayName().equalsIgnoreCase(titleName);
                    });
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChangeWorld(PlayerChangedWorldEvent event) {
        final Player player = event.getPlayer();
        final String world = player.getWorld().getName();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                plugin.getItems().getStringList("ITEMS." + key + ".WORLDS").forEach(action -> {
                    if (world.equalsIgnoreCase(action)) {
                        ProjectUtils.scheduleSyncDelayedTask(10L, () -> generateItems.setupItems(player));
                        return;
                    }
                    if (plugin.getItems().getBoolean("ITEMS." + key + ".SETTINGS.REMOVE_WHEN_CHANGING_THE_WORLD"))
                        player.getInventory().remove(Objects.requireNonNull(XMaterial.valueOf(plugin.getItems().getString("ITEMS." + key + ".ITEM")).parseMaterial()));
                });
            }
        }
    }
}
