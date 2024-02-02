package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import org.bukkit.scheduler.BukkitScheduler;

public class RegionTimer {
    private MapProtect plugin;
    private RegionManager manager;
    private BukkitScheduler scheduler;
    public RegionTimer(MapProtect plugin, RegionManager regionManager){
        this.plugin = plugin;
        this.manager = regionManager;
        scheduler = plugin.getServer().getScheduler();
        blockPlaceTimer();
    }

    public void blockPlaceTimer(){
        scheduler.runTaskTimer(plugin, ()->{

        },0L, 20L);
    }

}
