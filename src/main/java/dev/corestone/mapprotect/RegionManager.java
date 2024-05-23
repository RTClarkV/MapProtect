package dev.corestone.mapprotect;

import dev.corestone.mapprotect.commands.MapDeleteCommand;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.commands.WandManager;
import dev.corestone.mapprotect.regions.RegionState;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;

public class RegionManager {
    private MapProtect plugin;
    private WandManager wandManager;

    private MapDeleteCommand mapDeleteCommand;

    private ArrayList<RegionBox> regions = new ArrayList<>();

    public RegionManager(MapProtect plugin){
        this.plugin = plugin;
        loadRegions();
        this.wandManager = new WandManager(plugin, this);
        this.mapDeleteCommand = new MapDeleteCommand(plugin, this);
    }

    public void loadRegions(){
        for(String name : plugin.getRegionData().getRegionList()){
            addRegion(name);
        }
    }

    public void createNewRegion(String name, String defaultProfile, Location loc1, Location loc2){
        plugin.getRegionData().addRegion(name, defaultProfile);
        if(loc1.getX() > loc2.getX()) loc1.setX(loc1.getX()+1); //the East side of the map will be 1 block short without the +1.
        if(loc2.getX() > loc1.getX()) loc2.setX(loc2.getX()+1);
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
        RegionBox deleteBox = null;
        for(RegionBox regionBox : regions){
            if(regionBox.getName().equals(name)){
                deleteBox = regionBox;
                regionBox.setState(RegionState.DELETED);
            }
        }
        regions.remove(deleteBox);
    }
    public void removeDefault(String name){
        plugin.getDefaultData().remove("default-profiles."+name);
        plugin.getDefaultData().getDefaultList().remove(name);
    }
    public RegionBox getRegion(String name){
        for(RegionBox regionBox : regions){
            if(regionBox.getName().equals(name)){
                return regionBox;
            }
        }
        return null;
    }
    public ArrayList<RegionBox> getRegionList(){
        return regions;
    }
    public void shutDown(){
        for(RegionBox regionBox : regions){
            regionBox.setState(RegionState.DELETED);
        }
        regions.clear();
    }
    public void startUp(){
        loadRegions();
    }
}
