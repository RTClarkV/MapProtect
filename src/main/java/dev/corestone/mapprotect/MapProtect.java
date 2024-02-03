package dev.corestone.mapprotect;

import dev.corestone.mapprotect.data.RegionData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class MapProtect extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
//        new RegionManager(this);
        new RegionManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
