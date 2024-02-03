package dev.corestone.mapprotect;

import dev.corestone.mapprotect.data.LocationData;
import dev.corestone.mapprotect.data.RegionData;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionInterface;
import dev.corestone.mapprotect.regions.WandManager;
import org.bukkit.Location;

import java.util.ArrayList;

public class RegionManager {
    private MapProtect plugin;
    private WandManager wandManager;

    private ArrayList<RegionInterface> regions = new ArrayList<>();

    public RegionManager(MapProtect plugin){
        this.plugin = plugin;
        loadRegions();
        this.wandManager = new WandManager(plugin, this);
    }

    public void loadRegions(){
        for(String name : RegionData.getRegionList()){
            addBox(name);
        }
    }

    public void newBox(String name, String defaultProfiel, Location loc1, Location loc2){
        if(LocationData.getRegionNames().contains(name))return;
        LocationData.addBox(name, loc1, loc2);
        RegionData.addNewRegion(name, defaultProfiel);
        regions.add(new RegionBox(plugin, this, name));
    }
    public void addBox(String name){
        regions.add(new RegionBox(plugin, this, name));
    }
}
