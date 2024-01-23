package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.data.RegionData;

public class RegionManager {
    private MapProtect plugin;
    private RegionData regionData;

    public RegionManager(MapProtect plugin){
        this.plugin = plugin;
        regionData = new RegionData(plugin);
    }

}
