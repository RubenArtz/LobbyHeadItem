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

package artzstudio.dev.lobby.spigot;

import artzstudio.dev.lobby.spigot.config.ConfigType;
import artzstudio.dev.lobby.spigot.config.ConfigurationManager;
import artzstudio.dev.lobby.spigot.launch.Launch;
import artzstudio.dev.lobby.spigot.utils.addColor;
import artzstudio.dev.lobby.spigot.utils.slim.SlimJar;
import dev.dejvokep.boostedyaml.YamlDocument;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Lobby extends JavaPlugin {

    @Getter
    public String prefix = "&8[&9Lobby Head Item&8]&f ";
    public List<UUID> playerUUIDs = new ArrayList<>();
    @Getter
    private ConfigurationManager configManager;
    private Launch launch;

    @Override
    public void onLoad() {
        SlimJar.load(this);
    }

    @Override
    public void onEnable() {
        try {
            this.launch = Class.forName("artzstudio.dev.lobby.spigot.launch.Launcher")
                    .asSubclass(Launch.class)
                    .getDeclaredConstructor()
                    .newInstance();

            this.launch.launch();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalStateException("There was an error starting the plugin!");
        } catch (InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        if (this.launch != null) {
            this.launch.shutdown();
            this.launch = null;
        }
    }

    public void loadALLFiles() {
        /*
        load Configuration
         */
        this.configManager = new ConfigurationManager(this);

        this.configManager.loadAll();
    }

    public YamlDocument getConfiguration() {
        return configManager.get(ConfigType.CONFIG);
    }

    public YamlDocument getItems() {
        return configManager.get(ConfigType.ITEMS);
    }

    public void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(addColor.addColors(message));
    }
}
