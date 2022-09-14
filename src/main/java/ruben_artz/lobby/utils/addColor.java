package ruben_artz.lobby.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class addColor {

    public static String addColors(String input) {
        if ((input == null) || (input.isEmpty())) return input;
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String addColors(Player player, String input) {
        if ((input == null) || (input.isEmpty())) return input;
        return ChatColor.translateAlternateColorCodes('&', ProjectUtils.setPlaceholders(player, input));
    }
}
