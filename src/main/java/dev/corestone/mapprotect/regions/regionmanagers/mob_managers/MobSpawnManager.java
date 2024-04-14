package dev.corestone.mapprotect.regions.regionmanagers.mob_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import org.bukkit.GameRule;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

import java.util.ArrayList;
import java.util.UUID;

public class MobSpawnManager implements Listener, RegionHandler {
    private MapProtect plugin;
    private RegionBox region;
    private boolean monsterSpawn;
    private boolean mobSpawn;
    private boolean peacefulMobSpawn;
    private ArrayList<EntityType> unspawnableMobs = new ArrayList<>();
    private boolean inverseUnspawnableMobs;

    public MobSpawnManager(MapProtect plugin, RegionBox region){
        this.region = region;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

        this.mobSpawn = (boolean) plugin.getRegionData().getMobData(region.getName(), "mob-spawn");
        this.peacefulMobSpawn = (boolean) plugin.getRegionData().getMobData(region.getName(), "friendly-mob-spawn");
        this.monsterSpawn = (boolean) plugin.getRegionData().getMobData(region.getName(), "monster-spawn");
        this.inverseUnspawnableMobs = (boolean) plugin.getRegionData().getMobData(region.getName(), "inverse-unspawnable-mobs");
        for(EntityType entityType : EntityType.values()){
            for(String ent : plugin.getRegionData().getStringArrayMobData(region.getName(), "unspawnable-mobs")){
                EntityType listEnt = EntityType.valueOf(ent.toUpperCase());
                if(!inverseUnspawnableMobs && !unspawnableMobs.contains(listEnt)) unspawnableMobs.add(listEnt);
                if(inverseUnspawnableMobs && !unspawnableMobs.contains(entityType)) unspawnableMobs.add(entityType);
                if(inverseUnspawnableMobs) unspawnableMobs.remove(listEnt);
            }
        }
    }
    @EventHandler
    public void onSpawn(EntitySpawnEvent event){
        if(!region.getBox().contains(event.getLocation().toVector()))return;
        if(event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.CUSTOM || event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.COMMAND
            || event.getEntity().getEntitySpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG)return;
        if(!mobSpawn){
            event.setCancelled(true);
        }
        if((event.getEntity() instanceof Monster) && !monsterSpawn){
            event.setCancelled(true);
        }
        if(!(event.getEntity() instanceof Monster) && !peacefulMobSpawn){
            event.setCancelled(true);
        }
        if(unspawnableMobs.contains(event.getEntity().getType())){
            event.setCancelled(true);
        }
    }

    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void playerEntry(UUID uuid) {

    }

    @Override
    public void playerExit(UUID uuid) {

    }
}
