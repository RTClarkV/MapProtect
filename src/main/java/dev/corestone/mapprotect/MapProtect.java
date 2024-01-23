package dev.corestone.mapprotect;

import dev.corestone.mapprotect.data.RegionData;
import dev.corestone.mapprotect.regions.RegionManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MapProtect extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new RegionManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
