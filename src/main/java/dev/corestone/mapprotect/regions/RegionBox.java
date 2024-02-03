package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;
import dev.corestone.mapprotect.data.LocationData;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.UUID;

public class RegionBox implements Listener, RegionInterface {

    private MapProtect plugin;
    private RegionManager manager;
    private BlockManager blockManager;
    private BoundingBox box;


    private ArrayList<UUID> players = new ArrayList<>();

    private String name;

    public RegionBox(MapProtect plugin, RegionManager manager, String name){
        this.plugin = plugin;
        this.name = name;
        this.manager = manager;
        this.blockManager = new BlockManager(plugin, this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.box = LocationData.getBox(name);
    }

    @Override
    public ArrayList<UUID> getPlayers() {
        return players;
    }

    @Override
    public void clearBlocks() {
        blockManager.removeAllBlocks();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public BoundingBox getBox() {
        return box;
    }
}
