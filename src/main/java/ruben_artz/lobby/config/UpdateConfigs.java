package ruben_artz.lobby.config;

import ruben_artz.lobby.Lobby;
import ruben_artz.lobby.launch.Launcher;
import ruben_artz.lobby.utils.ProjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UpdateConfigs {
    private static final Lobby plugin = Lobby.getPlugin(Lobby.class);

    public static void updateConfigs() {
        ProjectUtils.syncRunTask(() -> {
            /*
            Update config of "config.yml"
             */
            if (!Objects.equals(plugin.getConfig().getString("version"), "1.0")) {
                try {
                    Files.copy(Paths.get(plugin.getDataFolder() + "/config.yml"), Paths.get(plugin.getDataFolder() + "/old-config-" + plugin.getConfig().getString("version") + ".yml"), StandardCopyOption.REPLACE_EXISTING);
                    File file = new File(plugin.getDataFolder(), "config.yml");
                    file.delete();
                    Launcher.getLauncher().registerConfig();
                    plugin.sendConsole("&cYour config.yml file was updated in this version!");
                } catch (IOException e) {
                    throw new RuntimeException();
                }
            }
            /*
            Update config of "items.yml"
             */
            ProjectUtils.runTaskLater(20L, () -> {
                if (!Objects.equals(plugin.getItems().getString("version"), "1.0")) {
                    try {
                        Files.copy(Paths.get(plugin.getDataFolder() + "/items.yml"), Paths.get(plugin.getDataFolder() + "/old-items-" + plugin.getConfig().getString("version") + ".yml"), StandardCopyOption.REPLACE_EXISTING);
                        File file = new File(plugin.getDataFolder(), "items.yml");
                        file.delete();
                        Launcher.getLauncher().registerConfig();
                        plugin.sendConsole("&cYour items.yml file was updated in this version!");
                    } catch (IOException e) {
                        throw new RuntimeException();
                    }
                }
            });
        });
    }
}
