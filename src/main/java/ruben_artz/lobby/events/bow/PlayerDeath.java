package ruben_artz.lobby.events.bow;

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
}