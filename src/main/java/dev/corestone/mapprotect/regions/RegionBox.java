package dev.corestone.mapprotect.regions;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;

import dev.corestone.mapprotect.regions.regionmanagers.block_managers.BlockBurnHandler;
import dev.corestone.mapprotect.regions.regionmanagers.block_managers.BlockHandler;
import dev.corestone.mapprotect.regions.regionmanagers.block_managers.ExplosionDamageHandler;
import dev.corestone.mapprotect.regions.regionmanagers.block_managers.WaterLavaFlowHandler;
import dev.corestone.mapprotect.regions.regionmanagers.mob_managers.MobGriefHandler;
import dev.corestone.mapprotect.regions.regionmanagers.mob_managers.MobSpawnManager;
import dev.corestone.mapprotect.regions.regionmanagers.player_managers.*;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.UUID;


public class RegionBox implements Listener {

    private MapProtect plugin;
    private RegionManager manager;
    private BoundingBox box;
    private RegionState state;

    private String name;
    private BukkitScheduler scheduler;
    private ArrayList<RegionHandler> regionHandlers = new ArrayList<>();
    private ArrayList<UUID> playersInside = new ArrayList<>();

    public RegionBox(MapProtect plugin, RegionManager manager, String name){

        this.plugin = plugin;
        this.name = name;
        this.manager = manager;
        this.scheduler = plugin.getServer().getScheduler();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.box = plugin.getLocationData().getBox(name);
        state = RegionState.valueOf(plugin.getRegionData().getConfig().getString("regions."+name+".map-master.map-state").toUpperCase());

        //create managers

        //player managers
        regionHandlers.add(new PlayerFallDamageHandler(plugin, this));
        regionHandlers.add(new PlayerEntryExitHandler(plugin, this));
        regionHandlers.add(new PlayerPvP(plugin, this));
        regionHandlers.add(new PlayerInteractHandler(plugin, this));
        regionHandlers.add(new PotionEffectHandler(plugin, this));
        regionHandlers.add(new PlayerHurtHandler(plugin, this));
        regionHandlers.add(new UnusableItemsHandler(plugin, this));
        regionHandlers.add(new MapGameModeHandler(plugin, this));

        //block managers
        regionHandlers.add(new BlockBurnHandler(plugin, this));
        regionHandlers.add(new BlockHandler(plugin, this));
        regionHandlers.add(new ExplosionDamageHandler(plugin, this));
        regionHandlers.add(new WaterLavaFlowHandler(plugin, this));

        //mob managers
        regionHandlers.add(new MobSpawnManager(plugin, this));
        regionHandlers.add(new MobGriefHandler(plugin, this));
        //other logic

    }
    public void addPlayer(UUID uuid){
        playersInside.add(uuid);
        for(RegionHandler regionHandler : regionHandlers){
            regionHandler.playerEntry(uuid);
        }
    }
    public void removePlayer(UUID uuid){
        playersInside.remove(uuid);
        for(RegionHandler regionHandler : regionHandlers){
            regionHandler.playerExit(uuid);
        }
    }
    public ArrayList<UUID> getPlayersInside(){
        return playersInside;
    }
    public void setState(RegionState state) {
            this.state = state;
            switch (state){
                case ACTIVE:
                    break;
                case IDLE:
                    break;
                case LOCKED:
                    break;
                case DELETED:
                    for(RegionHandler handler : regionHandlers){
                        handler.delete();
                    }
                    HandlerList.unregisterAll(this);
                    break;
            }
    }

    public void shutDown() {
        HandlerList.unregisterAll(this);
        setState(RegionState.DELETED);
    }

    public RegionState getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public BoundingBox getBox() {
        return box;
    }
}
