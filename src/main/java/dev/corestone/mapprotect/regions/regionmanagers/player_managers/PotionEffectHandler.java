package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.HashMap;
import java.util.UUID;

public class PotionEffectHandler implements RegionHandler {
    private MapProtect plugin;
    private RegionBox region;
    private BukkitScheduler scheduler;
    private boolean potionEffectsAllowed;
    private HashMap<PotionEffectType, Integer> potionEffects = new HashMap<>();

    public PotionEffectHandler(MapProtect plugin, RegionBox region){
        this.region = region;
        this.plugin = plugin;
        this.scheduler = plugin.getServer().getScheduler();
        this.potionEffectsAllowed = (boolean) plugin.getRegionData().getPlayerData(region.getName(), "enable-potion-effects");
        for(String effect : plugin.getRegionData().getConfig().getConfigurationSection("regions."+region.getName()+".player-managers.potion-effects").getKeys(false)){
            if(PotionEffectType.getByName(effect.toUpperCase()) == null){
                plugin.getServer().getConsoleSender().sendMessage(Colorize.format("&b&lMapProtect &4&lError! &cYou must have a correct potion effect in map " + region.getName()) + "&c. "+ effect + " is not a valid potion effect.");
                plugin.getServer().getConsoleSender().sendMessage(Colorize.format("&cFor correct potion effect types, visit: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/potion/PotionEffectType.html"));
                break;
            }
            potionEffects.put(PotionEffectType.getByName(effect.toUpperCase()), plugin.getRegionData().getConfig().getInt("regions."+region.getName()+".player-managers.potion-effects."+effect));
        }
        potionChecker();
    }
    private void potionChecker(){
        scheduler.runTaskTimer(plugin, (task)->{
            if(region.getState() == RegionState.DELETED){
                task.cancel();
            }
            if(!potionEffectsAllowed)return;
            if(region.getPlayersInside().isEmpty())return;
            for(UUID uuid : region.getPlayersInside()){
                addPotionEffects(Bukkit.getPlayer(uuid));
            }
        }, 0L, 20L);
    }
    public void addPotionEffects(Player player){
        for(PotionEffectType potionEffectType : potionEffects.keySet()){
            player.addPotionEffect(potionEffectType.createEffect(420, potionEffects.get(potionEffectType)));
        }
    }
    public void clearPotionEffects(Player player){
        for(PotionEffectType potionEffectType : potionEffects.keySet()){
            player.removePotionEffect(potionEffectType);
        }
    }

    @Override
    public void delete() {
    }

    @Override
    public void playerEntry(UUID uuid) {
        if(!potionEffectsAllowed)return;
        addPotionEffects(Bukkit.getPlayer(uuid));
    }

    @Override
    public void playerExit(UUID uuid) {
        if(!potionEffectsAllowed)return;
        clearPotionEffects(Bukkit.getPlayer(uuid));
    }
}
