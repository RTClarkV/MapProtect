package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;
import org.bukkit.event.Listener;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.UUID;

public class RegionBox implements Listener, RegionInterface {

    private MapProtect plugin;
    private RegionManager manager;
    private BlockManager blockManager;
    private BoundingBox box;
    private RegionState state;
    private boolean blockBreakTimerActive;
    private boolean playerPvp;
    private boolean monsterSpawn;
    private boolean creeperDamage;

//    block-break-timer: 30
//    block-break-timer-active: true
//    player-pvp: true
//    mob-spawn: true
//    monster-spawn: true
//    creeper-damage: false

    private ArrayList<UUID> players = new ArrayList<>();

    private String name;

    public RegionBox(MapProtect plugin, RegionManager manager, String name){
        this.plugin = plugin;
        this.name = name;
        this.manager = manager;
        this.blockManager = new BlockManager(plugin, this);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.box = plugin.getLocationData().getBox(name);
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
