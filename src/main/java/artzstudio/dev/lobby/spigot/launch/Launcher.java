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

package artzstudio.dev.lobby.spigot.launch;

import artzstudio.dev.lobby.spigot.Lobby;
import artzstudio.dev.lobby.spigot.commands.MainCommand;
import artzstudio.dev.lobby.spigot.events.fishing.*;
import artzstudio.dev.lobby.spigot.events.playerJoin;
import artzstudio.dev.lobby.spigot.utils.ProjectUtils;
import com.tcoded.folialib.FoliaLib;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import java.util.Arrays;
import java.util.Objects;

public class Launcher implements Launch {
    @Getter
    private static Launcher launcher;
    @Getter
    private static FoliaLib foliaLib;
    private final Lobby plugin = Lobby.getPlugin(Lobby.class);

    @Override
    public void launch() {
        launcher = this;

        foliaLib = new FoliaLib(plugin);
        foliaLib.getOptions().enableInvalidTickDebugMode();

        plugin.loadALLFiles();

        this.registerEvents();
        this.registerCommands();
        this.registerMetrics();
        this.startPlugin();
    }

    @Override
    public void shutdown() {

    }

    private void registerCommands() {
        Objects.requireNonNull(plugin.getCommand("lobbyheaditem")).setExecutor(new MainCommand());
    }

    private void registerEvents() {
        final PluginManager event = plugin.getServer().getPluginManager();

        event.registerEvents(new playerJoin(), plugin);

        /*
        create items
         */
        Arrays.asList(
                new artzstudio.dev.lobby.spigot.events.items.PlayerInteract(),
                new artzstudio.dev.lobby.spigot.events.items.PlayerInventoryClick(),
                new artzstudio.dev.lobby.spigot.events.items.PlayerDropItem(),
                new artzstudio.dev.lobby.spigot.events.items.PlayerRespawn(),
                new artzstudio.dev.lobby.spigot.events.items.PlayerDeath(),
                new artzstudio.dev.lobby.spigot.events.items.PlayerChangedWorld()).forEach(li -> event.registerEvents(li, plugin));

        /*
        player head
         */
        Arrays.asList(
                new artzstudio.dev.lobby.spigot.events.head.PlayerBlockPlace(),
                new artzstudio.dev.lobby.spigot.events.head.PlayerChangedWorld(),
                new artzstudio.dev.lobby.spigot.events.head.PlayerDeath(),
                new artzstudio.dev.lobby.spigot.events.head.PlayerDropItem(),
                new artzstudio.dev.lobby.spigot.events.head.PlayerInteract(),
                new artzstudio.dev.lobby.spigot.events.head.PlayerInventoryClick(),
                new artzstudio.dev.lobby.spigot.events.head.PlayerRespawn()).forEach(li -> event.registerEvents(li, plugin));

        /*
        player bow
         */
        Arrays.asList(
                new artzstudio.dev.lobby.spigot.events.bow.EntityDamage(),
                new artzstudio.dev.lobby.spigot.events.bow.PlayerChangedWorld(),
                new artzstudio.dev.lobby.spigot.events.bow.PlayerDeath(),
                new artzstudio.dev.lobby.spigot.events.bow.PlayerDropItem(),
                new artzstudio.dev.lobby.spigot.events.bow.PlayerInventoryClick(),
                new artzstudio.dev.lobby.spigot.events.bow.PlayerProjectileHit(),
                new artzstudio.dev.lobby.spigot.events.bow.PlayerRespawn()).forEach(li -> event.registerEvents(li, plugin));

        /*
        Fishing
         */
        Arrays.asList(
                new GrapplingChangedWorld(),
                new GrapplingDeath(),
                new GrapplingDropItem(),
                new GrapplingHookEvent(),
                new GrapplingInventoryClick(),
                new GrapplingRespawn()
        ).forEach(li -> event.registerEvents(li, plugin));

        if (ProjectUtils.isVersion_1_9_To_1_26()) {
            Arrays.asList(
                    new artzstudio.dev.lobby.spigot.events.items.PlayerSwapHandItems(),
                    new artzstudio.dev.lobby.spigot.events.head.PlayerSwapHandItems(),
                    new artzstudio.dev.lobby.spigot.events.bow.PlayerSwapHandItems(),
                    new artzstudio.dev.lobby.spigot.events.fishing.GrapplingSwapHandItems()).forEach(li -> event.registerEvents(li, plugin));
        }
    }

    private void registerMetrics() {
        final Metrics metrics = new Metrics(plugin, 8343);
        metrics.addCustomChart(new SingleLineChart("players", () -> Bukkit.getOnlinePlayers().size()));
    }

    public void startPlugin() {
        plugin.sendConsole(plugin.prefix + "&aSuccessfully enabled &cv" + plugin.getDescription().getVersion());
        plugin.sendConsole("&8--------------------------------------------------------------------------------------");
        plugin.sendConsole("&7         Developed by &cRuben_Artz");
        plugin.sendConsole(plugin.prefix + "&aVersion: &c" + plugin.getDescription().getVersion() + " &ais loading... &8(&6Current&8)");
        plugin.sendConsole(plugin.prefix + "&aServer: &c" + Bukkit.getVersion());
        plugin.sendConsole(plugin.prefix + "&aLoading necessary files...");
        plugin.sendConsole(" ");
        plugin.sendConsole("&fLobby Head Item Starting plugin...");
        plugin.sendConsole("&f");
        plugin.sendConsole("&8--------------------------------------------------------------------------------------");
    }
}
