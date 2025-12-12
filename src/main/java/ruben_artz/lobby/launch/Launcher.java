package ruben_artz.lobby.launch;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.commands.MainCommand;
import ruben_artz.lobby.config.Configuration;
import ruben_artz.lobby.config.UpdateConfigs;
import ruben_artz.lobby.events.playerJoin;
import ruben_artz.lobby.utils.ProjectUtils;

import java.util.Arrays;
import java.util.Objects;

public class Launcher implements Launch {
    @Getter
    private static Launcher launcher;
    @Getter
    private static TaskScheduler scheduler;
    private final Lobby plugin = Lobby.getPlugin(Lobby.class);
    public Configuration config;

    @Override
    public void launch() {
        launcher = this;

        scheduler = UniversalScheduler.getScheduler(plugin);

        this.registerConfig();
        this.updateConfigs();
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
                new ruben_artz.lobby.events.items.PlayerInteract(),
                new ruben_artz.lobby.events.items.PlayerInventoryClick(),
                new ruben_artz.lobby.events.items.PlayerDropItem(),
                new ruben_artz.lobby.events.items.PlayerRespawn(),
                new ruben_artz.lobby.events.items.PlayerDeath(),
                new ruben_artz.lobby.events.items.PlayerChangedWorld()).forEach(li -> event.registerEvents(li, plugin));

        /*
        player head
         */
        Arrays.asList(
                new ruben_artz.lobby.events.head.PlayerBlockPlace(),
                new ruben_artz.lobby.events.head.PlayerChangedWorld(),
                new ruben_artz.lobby.events.head.PlayerDeath(),
                new ruben_artz.lobby.events.head.PlayerDropItem(),
                new ruben_artz.lobby.events.head.PlayerInteract(),
                new ruben_artz.lobby.events.head.PlayerInventoryClick(),
                new ruben_artz.lobby.events.head.PlayerRespawn()).forEach(li -> event.registerEvents(li, plugin));

        /*
        player bow
         */
        Arrays.asList(
                new ruben_artz.lobby.events.bow.EntityDamage(),
                new ruben_artz.lobby.events.bow.PlayerChangedWorld(),
                new ruben_artz.lobby.events.bow.PlayerDeath(),
                new ruben_artz.lobby.events.bow.PlayerDropItem(),
                new ruben_artz.lobby.events.bow.PlayerInventoryClick(),
                new ruben_artz.lobby.events.bow.PlayerProjectileHit(),
                new ruben_artz.lobby.events.bow.PlayerRespawn()).forEach(li -> event.registerEvents(li, plugin));

        if (ProjectUtils.isVersion_1_9_To_1_21()) {
            Arrays.asList(
                    new ruben_artz.lobby.events.items.PlayerSwapHandItems(),
                    new ruben_artz.lobby.events.head.PlayerSwapHandItems(),
                    new ruben_artz.lobby.events.bow.PlayerSwapHandItems()).forEach(li -> event.registerEvents(li, plugin));
        }
    }

    public void registerConfig() {
        config = new Configuration().initiate(
                "config.yml",
                "items.yml");
    }

    private void updateConfigs() {
        UpdateConfigs.updateConfigs();
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
