package ruben_artz.lobby.events.items;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.utils.ProjectUtils;
import ruben_artz.lobby.utils.addColor;

import java.util.Objects;

public class PlayerDeath implements Listener {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);


    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        final Player player = event.getEntity();

        for (String key : Objects.requireNonNull(plugin.getItems().getConfigurationSection("ITEMS")).getKeys(false)) {
            if (plugin.getItems().getBoolean("ITEMS." + key + ".ENABLED")) {
                if (!ProjectUtils.getWorldsItems(player, plugin.getItems().getStringList("ITEMS." + key + ".WORLDS"))) return;

                final String titleName = addColor.addColors(player, ProjectUtils.placeholderReplace(player, plugin.getItems().getString("ITEMS." + key + ".NAME")));

                try {
                    if (plugin.getItems().getBoolean("ITEMS." + key + ".SETTINGS.NO_DROP_ON_DEATH")) {
                        event.getDrops().removeIf(i -> {
                            Objects.requireNonNull(i.getItemMeta()).getDisplayName();
                            return i.getItemMeta().getDisplayName().equalsIgnoreCase(titleName);
                        });
                    }
                } catch (NullPointerException ignored) {}

            }
        }
    }
}