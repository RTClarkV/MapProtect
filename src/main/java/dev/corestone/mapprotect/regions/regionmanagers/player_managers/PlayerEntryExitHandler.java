package dev.corestone.mapprotect.regions.regionmanagers.player_managers;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.regions.RegionState;
import dev.corestone.mapprotect.regions.regionmanagers.RegionHandler;
import dev.corestone.mapprotect.utilities.Colorize;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitScheduler;

import java.awt.*;
import java.util.HashMap;
import java.util.UUID;

public class PlayerEntryExitHandler implements RegionHandler, Listener {

    private MapProtect plugin;
    private RegionBox region;
    private BukkitScheduler scheduler;
    private HashMap<UUID, Location> playerLocations = new HashMap<>();
    private boolean entry;
    private boolean exit;
    private String entryDenyMessage;
    private String exitDenyMessage;

    private String entryGreeting;
    private String exitFairwell;
    private Sound entrySound;
    private Sound exitSound;
    private boolean entryExitSounds;
    private boolean entryExitTitles;

    public PlayerEntryExitHandler(MapProtect plugin, RegionBox regionBox){
        this.region = regionBox;
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.scheduler = plugin.getServer().getScheduler();
        this.exit = (boolean) plugin.getRegionData().getPlayerData(region.getName(), "player-exit");
        this.entry = (boolean) plugin.getRegionData().getPlayerData(region.getName(), "player-entry");

        this.entryDenyMessage = plugin.getLanguageData().getLang("player-entry-deny");
        this.exitDenyMessage = plugin.getLanguageData().getLang("player-exit-deny");

        this.entryExitSounds = (boolean) plugin.getRegionData().getMasterData(region.getName(), "entry-exit-sounds");
        this.entryExitTitles = (boolean) plugin.getRegionData().getMasterData(region.getName(), "entry-exit-titles");

        this.entryGreeting = (String) plugin.getRegionData().getMasterData(region.getName(), "entry-title");
        this.exitFairwell = (String) plugin.getRegionData().getMasterData(region.getName(), "exit-title");

        if(plugin.getRegionData().getMasterData(region.getName(), "entry-sound") != null){
            this.entrySound = (Sound) Sound.valueOf(((String) plugin.getRegionData().getMasterData(region.getName(), "entry-sound")).toUpperCase());
        }
        if(plugin.getRegionData().getMasterData(region.getName(), "exit-sound") != null){
            this.exitSound = (Sound) Sound.valueOf(((String) plugin.getRegionData().getMasterData(region.getName(), "exit-sound")).toUpperCase());
        }

        playerLocations.clear();
        for(Player player : Bukkit.getOnlinePlayers()){
            playerLocations.put(player.getUniqueId(), player.getLocation());
        }
        if(!exit || !entry || entryExitTitles || entryExitSounds){
            runTasks();
        }
    }
    public void runTasks(){
        scheduler.runTaskTimerAsynchronously(plugin, (task)->{
            if(region.getState() == RegionState.DELETED){
                task.cancel();
            }
            if(region.getState() == RegionState.IDLE)return;
            for(UUID uuid : playerLocations.keySet()){
                //if(entry && exit)return;
                if(region.getBox().contains(Bukkit.getPlayer(uuid).getLocation().toVector()) && !region.getBox().contains(playerLocations.get(uuid).toVector()) && !entry){
                    scheduler.runTask(plugin, ()->{
                        Bukkit.getPlayer(uuid).sendMessage(Colorize.format(entryDenyMessage));
                        Bukkit.getPlayer(uuid).teleport(playerLocations.get(uuid));
                    });
                    return;
                }
                if(!region.getBox().contains(Bukkit.getPlayer(uuid).getLocation().toVector()) && region.getBox().contains(playerLocations.get(uuid).toVector()) && !exit){
                    scheduler.runTask(plugin, ()->{
                        Bukkit.getPlayer(uuid).sendMessage(Colorize.format(exitDenyMessage));
                        Bukkit.getPlayer(uuid).teleport(playerLocations.get(uuid).toLocation(Bukkit.getPlayer(uuid).getWorld()));
                    });
                    return;
                }
                if(entrySound != null && region.getBox().contains(Bukkit.getPlayer(uuid).getLocation().toVector()) && !region.getBox().contains(playerLocations.get(uuid).toVector())){
                    scheduler.runTask(plugin, ()->{
                        if(entryExitTitles) Bukkit.getPlayer(uuid).sendMessage(Colorize.format(entryGreeting));
                        if (entryExitSounds) Bukkit.getPlayer(uuid).playSound(playerLocations.get(uuid), entrySound, 1, 1);
                    });
                }
                if(exitSound != null && region.getBox().contains(playerLocations.get(uuid).toVector()) && !region.getBox().contains(Bukkit.getPlayer(uuid).getLocation().toVector())){
                    scheduler.runTask(plugin, ()->{
                        if(entryExitTitles) Bukkit.getPlayer(uuid).sendMessage(Colorize.format(exitFairwell));
                        if (entryExitSounds) Bukkit.getPlayer(uuid).playSound(playerLocations.get(uuid), exitSound, 1, 1);
                    });
                }
                playerLocations.replace(uuid, Bukkit.getPlayer(uuid).getLocation());
            }
        }, 0L, 10L);
    }
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        playerLocations.put(e.getPlayer().getUniqueId(), e.getPlayer().getLocation());
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        if(region.getBox().contains(e.getPlayer().getLocation().toVector()) && !entry){
            e.getPlayer().teleport(playerLocations.get(e.getPlayer().getUniqueId()));
        }
        if(!region.getBox().contains(e.getPlayer().getLocation().toVector()) && !exit){
            e.getPlayer().teleport(playerLocations.get(e.getPlayer().getUniqueId()));
        }
        playerLocations.remove(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void onKick(PlayerKickEvent e){
        if(region.getBox().contains(e.getPlayer().getLocation().toVector()) && !entry){
            e.getPlayer().teleport(playerLocations.get(e.getPlayer().getUniqueId()));
        }
        if(!region.getBox().contains(e.getPlayer().getLocation().toVector()) && !exit){
            e.getPlayer().teleport(playerLocations.get(e.getPlayer().getUniqueId()));
        }
        playerLocations.remove(e.getPlayer().getUniqueId());
    }

    @Override
    public void delete() {
        HandlerList.unregisterAll(this);
    }
}
