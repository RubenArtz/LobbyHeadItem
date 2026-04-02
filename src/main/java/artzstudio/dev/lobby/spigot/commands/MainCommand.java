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

package artzstudio.dev.lobby.spigot.commands;

import artzstudio.dev.lobby.spigot.Lobby;
import artzstudio.dev.lobby.spigot.utils.addColor;
import artzstudio.dev.lobby.spigot.utils.generateItems;
import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabExecutor {
    private final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            if (!sender.hasPermission("LobbyHeadItem.Admin")) {
                sender.sendMessage(addColor.addColors("&cYou do not have permissions to execute this command"));
                return true;
            }

            sender.sendMessage(addColor.addColors(" "));
            sender.sendMessage(addColor.addColors("&7Use &f'/lob help' &7for more information about the plugin."));
            sender.sendMessage(addColor.addColors(" "));
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("updatePlayerItems")) {
                if (sender instanceof Player player) {

                    deleteItems(player);

                    generateItems.setupItems(player);
                    generateItems.setupHead(player);
                    generateItems.setupBow(player);
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("help")) {
                if (!sender.hasPermission("LobbyHeadItem.Admin")) {
                    sender.sendMessage(addColor.addColors("&cYou do not have permissions to execute this command"));
                    return true;
                }

                sender.sendMessage(addColor.addColors("&8&m-----------------------------------------------------"));
                sender.sendMessage(addColor.addColors(" "));
                sender.sendMessage(addColor.addColors("&f                        &bLobby Head Item &cv" + plugin.getDescription().getVersion()));
                sender.sendMessage(addColor.addColors("&f                    &7Powered by &9&lＳＴＮ &f&lＳＴＵＤＩＯＳ&e®"));
                sender.sendMessage(addColor.addColors(" "));
                sender.sendMessage(addColor.addColors("&6▪ &e/lob help - &7About the plugin."));
                sender.sendMessage(addColor.addColors("&6▪ &e/lob reload - &7Reload the plugin config."));
                sender.sendMessage(addColor.addColors(" "));
                sender.sendMessage(addColor.addColors("&8&m-----------------------------------------------------"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("LobbyHeadItem.Admin")) {
                    sender.sendMessage(addColor.addColors("&cYou do not have permissions to execute this command"));
                    return true;
                }

                plugin.getConfigManager().reloadAll();

                if (sender instanceof Player player) {

                    deleteItems(player);

                    generateItems.setupItems(player);
                    generateItems.setupHead(player);
                    generateItems.setupBow(player);
                    generateItems.setupGrappling(player);
                }

                sender.sendMessage(addColor.addColors("&8&m-----------------------------------------------------"));
                sender.sendMessage(addColor.addColors(" "));
                sender.sendMessage(addColor.addColors("&f                        &bLobby Head Item &cv" + plugin.getDescription().getVersion()));
                sender.sendMessage(addColor.addColors("&f                    &7Powered by &9&lＳＴＮ &f&lＳＴＵＤＩＯＳ&e®"));
                sender.sendMessage(addColor.addColors(" "));
                sender.sendMessage(addColor.addColors("&f   &a&o(( The configuration was successfully reloaded! ))"));
                sender.sendMessage(addColor.addColors(" "));
                sender.sendMessage(addColor.addColors("&8&m-----------------------------------------------------"));
                return true;
            }
            return true;
        }
        return false;
    }


    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        final List<String> commands = new ArrayList<>();
        if (!sender.hasPermission("LobbyHeadItem.Admin")) return null;
        if (args.length == 1) {
            String partialCommand = args[0];
            commands.add("reload");
            commands.add("help");
            StringUtil.copyPartialMatches(partialCommand, commands, completions);
        } else {
            return ImmutableList.of();
        }
        Collections.sort(completions);
        return completions;
    }

    private void deleteItems(Player player) {
        var itemsSection = plugin.getItems().getSection("ITEMS");
        if (itemsSection == null) return;

        for (String key : itemsSection.getRoutesAsStrings(false)) {
            String basePath = "ITEMS." + key;

            if (!plugin.getItems().getBoolean(basePath + ".SETTINGS.REMOVE_WHEN_CHANGING_THE_WORLD")) {
                continue;
            }

            String itemName = plugin.getItems().getString(basePath + ".ITEM");
            if (itemName == null) {
                continue;
            }

            XMaterial.matchXMaterial(itemName).ifPresent(material -> {
                if (material.get() != null) {
                    player.getInventory().remove(material.get());
                }
            });
        }

        if (plugin.getConfiguration().getBoolean("PLAYER_BOW.CONFIGURATION.REMOVE_WHEN_CHANGING_THE_WORLD")) {
            if (XMaterial.BOW.get() != null) player.getInventory().remove(XMaterial.BOW.get());
            if (XMaterial.ARROW.get() != null) player.getInventory().remove(XMaterial.ARROW.get());
        }

        if (plugin.getConfiguration().getBoolean("PLAYER_HEAD.CONFIGURATION.REMOVE_WHEN_CHANGING_THE_WORLD")) {
            if (XMaterial.PLAYER_HEAD.get() != null) {
                player.getInventory().remove(XMaterial.PLAYER_HEAD.get());
            }
        }

        if (plugin.getConfiguration().getBoolean("GRAPPLING_HOOK.CONFIGURATION.REMOVE_WHEN_CHANGING_THE_WORLD")) {
            if (XMaterial.FISHING_ROD.get() != null) {
                player.getInventory().remove(XMaterial.FISHING_ROD.get());
            }
        }
    }
}