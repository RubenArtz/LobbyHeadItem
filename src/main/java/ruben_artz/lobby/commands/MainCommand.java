package ruben_artz.lobby.commands;

import com.google.common.collect.ImmutableList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.launch.Launcher;
import ruben_artz.lobby.utils.addColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainCommand implements CommandExecutor, TabExecutor {
    private final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("LobbyHeadItem.Admin")) {
            sender.sendMessage(addColor.addColors("&cYou do not have permissions to execute this command"));
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage(addColor.addColors(" "));
            sender.sendMessage(addColor.addColors("&7Use &f'/lob help' &7for more information about the plugin."));
            sender.sendMessage(addColor.addColors(" "));
            return true;
        }
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help")) {
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
                Launcher.getLauncher().registerConfig();
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


    @Override @Nullable
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
}
