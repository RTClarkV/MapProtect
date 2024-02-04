package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class BlockManager implements Listener {
    private MapProtect plugin;
    private RegionBox regionBox;
    private BukkitScheduler scheduler;
    private int blockBreakTimer;
    private List<Block> blockList = new ArrayList<>();
    private List<Material> breakableBlocks = new ArrayList<>();
    private List<Material> placeableBlocks = new ArrayList<>();
    private NamespacedKey blockKey;
    public BlockManager(MapProtect plugin, RegionBox regionBox) {
        this.plugin = plugin;
        this.regionBox = regionBox;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.scheduler = plugin.getServer().getScheduler();
        this.breakableBlocks = plugin.getRegionData().getBreakableBlocks(regionBox.getName());
        this.placeableBlocks = plugin.getRegionData().getPlaceableBlocks(regionBox.getName());
        this.blockBreakTimer = plugin.getRegionData().getBlockBreakTimer(regionBox.getName());
        this.blockKey = new NamespacedKey(plugin, regionBox.getName()+"-key");

    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent e){
        if(!regionBox.getBox().contains(e.getBlock().getLocation().toVector()))return;
        if(!breakableBlocks.contains(e.getBlock().getType())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(!regionBox.getBox().contains(e.getBlock().getLocation().toVector()))return;
    }

    public void removeAllBlocks(){
        for(Block block : blockList){
            block.breakNaturally();
        }
    }
}
