package dev.corestone.mapprotect;

import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionInterface;
import dev.corestone.mapprotect.commands.WandManager;
import dev.corestone.mapprotect.regions.RegionState;
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
        //HandlerList.unregisterAll(wandManager);
    }

    public void loadRegions(){
        for(String name : plugin.getRegionData().getRegionList()){
            addRegion(name);
        }
    }

    public void createNewRegion(String name, String defaultProfile, Location loc1, Location loc2){
        plugin.getRegionData().addRegion(name, defaultProfile);
        plugin.getLocationData().addBox(name, loc1, loc2);
        regions.add(new RegionBox(plugin, this, name));
    }
    public void createNewDefaultProfile(String name, String existingProfile){

    }
    public void addRegion(String name){
        regions.add(new RegionBox(plugin, this, name));
    }
    public void removeRegion(String name){
        plugin.getRegionData().removeRegion(name);
        plugin.getLocationData().removeBox(name);
        plugin.getDefaultData().remove(name);
        for(RegionInterface regionBox : regions){
            if(regionBox.getName().equalsIgnoreCase(name)){
                regionBox.setState(RegionState.IDLE);
                regions.remove(regionBox);
            }
        }
    }
//    public void reloadRegions(){
//        shutDown();
//        loadRegions();
//    }
    public void shutDown(){
        for(RegionInterface regionInterface : regions){
            regionInterface.setState(RegionState.IDLE);
            regions.remove(regionInterface);
        }
    }
}
