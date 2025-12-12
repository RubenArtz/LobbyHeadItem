package ruben_artz.lobby.config;

import com.google.common.collect.Lists;
import org.bukkit.configuration.file.YamlConfiguration;
import ruben_artz.lobby.Lobby;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class Configuration {
    private final Lobby plugin = Lobby.getPlugin(Lobby.class);
    private final YamlConfiguration langFile = null;
    private Map<String, YamlConfiguration> files;
    private Map<String, File> filesData;

    public Configuration initiate(String... fileNames) {
        this.files = new HashMap<>();
        this.filesData = new HashMap<>();

        if (!files.isEmpty() || !filesData.isEmpty()) {
            files.clear();
            filesData.clear();
        }

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        for (String fileName : fileNames) {
            File file = new File(plugin.getDataFolder(), fileName);
            if (!file.isDirectory()) {
                if (!file.exists()) {
                    plugin.saveResource(fileName, false);
                    plugin.sendConsole(plugin.prefix + "Creating &a" + fileName + " &fsince it does not exist...");
                }

                if (fileName.endsWith(".yml")) {
                    files.put(fileName, YamlConfiguration.loadConfiguration(file));
                    filesData.put(fileName, file);
                }
            }
        }
        return this;
    }

    public void reloadFiles() {
        initiate();
    }

    public YamlConfiguration getFile(String path) {
        return files.get(path);
    }

    public File getFileData(String path) {
        return filesData.get(path);
    }

    public String getString(String path) {
        return langFile.contains(path) ? langFile.getString(path) : plugin.prefix + "The specified path (lang/../" + path + ") could not be found.";
    }

    public List<String> getStringList(final String path) {
        final List<String> lore = Lists.newArrayList();
        lore.addAll(langFile.getStringList(path));
        return lore;
    }

    public int getInt(final String path) {
        return langFile.getInt(path);
    }

    public void saveFile(String path) {
        try {
            getFile(path).save(getFileData(path));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}