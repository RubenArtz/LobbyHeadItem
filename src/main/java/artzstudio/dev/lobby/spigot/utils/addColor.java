/*
 *     Copyright (C) 2026 Ruben_Artz (Artz Studio)
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package artzstudio.dev.lobby.spigot.utils;

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
