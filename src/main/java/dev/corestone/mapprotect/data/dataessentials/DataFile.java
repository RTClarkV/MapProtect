package dev.corestone.mapprotect.data.dataessentials;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStreamReader;

public interface DataFile {
    YamlConfiguration getConfig();
    void set(String path, Object object);
    void remove(String path);
    void update(YamlConfiguration internalYamlConfig);
}
