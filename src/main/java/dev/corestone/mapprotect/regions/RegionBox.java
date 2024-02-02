package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.UUID;

public class RegionBox implements Listener {

    private MapProtect plugin;
    private RegionManager manager;
    private BlockManager blockManager;


    private ArrayList<UUID> players = new ArrayList<>();

    private String name;

    public RegionBox(MapProtect plugin, RegionManager manager, String name){
        this.plugin = plugin;
        this.name = name;
        this.manager = manager;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void clearAllBlocks(){

    }
}
