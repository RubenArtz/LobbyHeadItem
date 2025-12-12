package ruben_artz.lobby;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import ruben_artz.lobby.launch.Launch;
import ruben_artz.lobby.launch.Launcher;
import ruben_artz.lobby.utils.addColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Lobby extends JavaPlugin {

    public String prefix = "&8[&9Lobby Head Item&8]&f ";
    public List<UUID> playerUUIDs = new ArrayList<>();
    private Launch launch;

    @Override
    public void onEnable() {
        try {
            this.launch = Class.forName("ruben_artz.lobby.launch.Launcher").asSubclass(Launch.class).newInstance();

            launch.launch();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void onDisable() {
        if (this.launch != null) {
            this.launch.shutdown();
            this.launch = null;
        }
    }

    public FileConfiguration getConfiguration() {
        return Launcher.getLauncher().config.getFile("config.yml");
    }

    public FileConfiguration getItems() {
        return Launcher.getLauncher().config.getFile("items.yml");
    }

    public void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(addColor.addColors(message));
    }
}
