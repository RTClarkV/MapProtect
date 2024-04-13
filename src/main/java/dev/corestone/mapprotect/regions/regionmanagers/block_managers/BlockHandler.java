package dev.corestone.mapprotect.regions.regionmanagers.block_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.UUID;

public class BlockHandler implements Listener, RegionHandler {
    private MapProtect plugin;
    private RegionBox region;
    private BukkitScheduler scheduler;
    private String buildDenyMessage;
    private PersistentDataContainer blockPlaceData;
    private int blockBreakTimer;
    private boolean build;
    private boolean blockBreakTimerActive;
    private boolean inverseBlockBreakList;
    private boolean inversePlaceableList;
    private boolean inverseBlockBreakIgnore;
    private boolean dropBlockItem;
    private ArrayList<Material> breakableBlocks = new ArrayList<>();
    private ArrayList<Material> placeableBlocks = new ArrayList<>();
    private ArrayList<Material> blockBreakIgnore = new ArrayList<>();
    private ArrayList<Block> blockBreakTimerTrack = new ArrayList<>();
    public BlockHandler(MapProtect plugin, RegionBox region){
        this.plugin = plugin;
        this.region = region;
        scheduler = plugin.getServer().getScheduler();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        buildDenyMessage = plugin.getLanguageData().getLang("player-build-deny");

        blockBreakTimerActive = (boolean) plugin.getRegionData().getBlockData(region.getName(), "block-break-timer-active");
        blockBreakTimer = (int) plugin.getRegionData().getBlockData(region.getName(), "block-break-timer");

        build = (boolean) plugin.getRegionData().getBlockData(region.getName(), "build");

        inverseBlockBreakIgnore = (boolean) plugin.getRegionData().getBlockData(region.getName(), "inverse-block-break-timer-ignore");
        inversePlaceableList = (boolean) plugin.getRegionData().getBlockData(region.getName(), "inverse-placeable-blocks");
        inverseBlockBreakList = (boolean) plugin.getRegionData().getBlockData(region.getName(), "inverse-breakable-blocks");
        dropBlockItem = (boolean) plugin.getRegionData().getBlockData(region.getName(), "drop-block-item");
        for(Material mat1 : Material.values()){
            for(String string : plugin.getRegionData().getStringArrayBlockData(region.getName(), "breakable-blocks")){
                Material material = Material.valueOf(string.toUpperCase());
                if(inverseBlockBreakList && !breakableBlocks.contains(mat1)) breakableBlocks.add(mat1);
                if(inverseBlockBreakList && breakableBlocks.contains(material)) breakableBlocks.remove(material);
                if(!inverseBlockBreakList && !breakableBlocks.contains(material)) breakableBlocks.add(material);
            }

            for(String string : plugin.getRegionData().getStringArrayBlockData(region.getName(), "placeable-blocks")){
                Material material = Material.valueOf(string.toUpperCase());
                if(inversePlaceableList && !placeableBlocks.contains(mat1)) placeableBlocks.add(mat1);
                if(inversePlaceableList && placeableBlocks.contains(material)) placeableBlocks.remove(material);
                if(!inversePlaceableList && !placeableBlocks.contains(material)) placeableBlocks.add(material);
            }

            for(String string : plugin.getRegionData().getStringArrayBlockData(region.getName(), "block-break-timer-ignore")){
                Material material = Material.valueOf(string.toUpperCase());
                if(inverseBlockBreakIgnore && !blockBreakIgnore.contains(mat1)) blockBreakIgnore.add(mat1);
                if(inverseBlockBreakIgnore && blockBreakIgnore.contains(material)) blockBreakIgnore.remove(material);
                if(!inverseBlockBreakIgnore && !blockBreakIgnore.contains(material)) blockBreakIgnore.add(material);
            }
        }
    }
    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(build)return;
        if(region.getState() == RegionState.IDLE)return;
        if(!region.getBox().contains(e.getBlock().getLocation().toVector()))return;
        if(e.getPlayer().hasPermission("mp.admin"))return;
        if(blockBreakTimerActive && blockBreakTimerTrack.contains(e.getBlock())){
            blockBreakTimerTrack.remove(e.getBlock());
            return;
        }
        if(!breakableBlocks.contains(e.getBlock().getType())){
            e.setCancelled(true);
            e.getPlayer().sendMessage(Colorize.format(buildDenyMessage));
            return;
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(build)return;
        if(region.getState() == RegionState.IDLE)return;
        if(!region.getBox().contains(e.getBlockPlaced().getLocation().toVector()))return;
        if(e.getPlayer().hasPermission("mp.admin"))return;
        if(blockBreakTimerActive && placeableBlocks.contains(e.getBlock().getType()) && !blockBreakIgnore.contains(e.getBlockPlaced().getType())){
            blockBreakTimer(e.getBlockPlaced());
            return;
        }
        if(!placeableBlocks.contains(e.getBlockPlaced().getType())){
            e.setCancelled(true);
            e.getPlayer().sendMessage(Colorize.format(buildDenyMessage));
            return;
        }
    }
    private void blockBreakTimer(Block block){
        blockBreakTimerTrack.add(block);
        scheduler.runTaskLater(plugin, ()->{
            if(!blockBreakTimerTrack.contains(block))return;
            blockBreakTimerTrack.remove(block);
            if(dropBlockItem) block.breakNaturally();
            if(!dropBlockItem) block.setType(Material.AIR);
            //BroadcastSound.playSound(block.getLocation(), Sound.BLOCK_STONE_BREAK, 1 ,1);
        }, 20L * blockBreakTimer);
    }
    @Override
    public void delete() {
        for(Block block : blockBreakTimerTrack){
            if(!blockBreakTimerTrack.contains(block))return;
            if(dropBlockItem) block.breakNaturally();
            if(!dropBlockItem) block.setType(Material.AIR);
            blockBreakTimerTrack.remove(block);
        }
        HandlerList.unregisterAll(this);
    }
    @Override
    public void playerEntry(UUID uuid) {

    }

    @Override
    public void playerExit(UUID uuid) {

    }

}
