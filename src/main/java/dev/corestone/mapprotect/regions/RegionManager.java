package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.data.RegionData;

import java.util.ArrayList;

public class RegionManager {
    private MapProtect plugin;
    private RegionData regionData;

    private ArrayList<RegionBox> regions = new ArrayList<>();

    public RegionManager(MapProtect plugin){
        regionData = new RegionData(plugin);
        this.plugin = plugin;
        loadRegions();
    }

    public void loadRegions(){
        for(String name : regionData.getRegionList()){
            regions.add(new RegionBox(plugin, this, name));
        }
    }




}
