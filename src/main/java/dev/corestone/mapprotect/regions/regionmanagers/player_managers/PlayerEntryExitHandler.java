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
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayerEntryExitHandler implements RegionHandler, Listener {

    private MapProtect plugin;
    private RegionBox region;
    private BukkitScheduler scheduler;
    private HashMap<UUID, Location> playerLocations = new HashMap<>();
    private HashMap<UUID, Integer> failCounter = new HashMap<>();
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
    private boolean playDenySound;

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

        this.playDenySound = (boolean) plugin.getLanguageData().getConfig().getBoolean("play-entry-exit-deny-sound");

        if(plugin.getRegionData().getMasterData(region.getName(), "entry-sound") != null){
            this.entrySound = (Sound) Sound.valueOf(((String) plugin.getRegionData().getMasterData(region.getName(), "entry-sound")).toUpperCase());
        }
        if(plugin.getRegionData().getMasterData(region.getName(), "exit-sound") != null){
            this.exitSound = (Sound) Sound.valueOf(((String) plugin.getRegionData().getMasterData(region.getName(), "exit-sound")).toUpperCase());
        }

        playerLocations.clear();
        region.getPlayersInside().clear();
        for(Player player : Bukkit.getOnlinePlayers()){
            playerLocations.put(player.getUniqueId(), player.getLocation());
            if(region.getBox().contains(player.getLocation().toVector())){
                region.addPlayer(player.getUniqueId());
            }
        }
        runTasks();
    }
    public void runTasks() {
        scheduler.runTaskTimerAsynchronously(plugin, (task) -> {
            if (region.getState() == RegionState.DELETED) {
                task.cancel();
            }
            if (region.getState() == RegionState.IDLE) return;
            //when player enters while entry is false.
            for (UUID uuid : playerLocations.keySet()) {
                Player player = Bukkit.getPlayer(uuid);
                if (!entry && region.getBox().contains(player.getLocation().toVector()) && !region.getBox().contains(playerLocations.get(uuid).toVector())) {
                    player.sendMessage(Colorize.format(entryDenyMessage));
                    //Bukkit.getPlayer(uuid).teleport(playerLocations.get(uuid));
                    if (playerLocations.get(uuid).getY() >= region.getBox().getMaxY()) {
                        scheduler.runTask(plugin, ()->{
                            player.teleport(playerLocations.get(uuid));
                        });
                    }
                    if (playDenySound) player.playSound(playerLocations.get(uuid), Sound.ENTITY_PHANTOM_FLAP, 1, 5);
                    Vector vector = playerLocations.get(uuid).toVector().clone().subtract(player.getLocation().toVector());
                    vector.multiply(.4);
                    vector.setY(vector.clone().getY() + 0.2);
                    if (vector.getY() > 0.4) vector.setY(0.4);
                    if(Objects.requireNonNull(player).isInsideVehicle()) player.getVehicle().setVelocity(vector);
                    player.setVelocity(vector);
                    if(!failCounter.containsKey(uuid)) failCounter.put(uuid, 1);
                    if(failCounter.containsKey(uuid)) failCounter.replace(uuid, failCounter.get(uuid)+1);
                    return;
                }
                //when the player exits while exit is false.
                if (!region.getBox().contains(player.getLocation().toVector()) && region.getBox().contains(playerLocations.get(uuid).toVector()) && !exit) {
                    player.sendMessage(Colorize.format(exitDenyMessage));
                    if (playDenySound)
                        player.playSound(playerLocations.get(uuid), Sound.ENTITY_PHANTOM_FLAP, 1, 5);
                    Vector vector = playerLocations.get(uuid).toVector().clone().subtract(player.getLocation().toVector());
                    vector.multiply(.4);
                    vector.setY(vector.clone().getY() + 0.2);
                    if (vector.getY() > 0.4) vector.setY(0.4);
                    if(Objects.requireNonNull(player).isInsideVehicle()) player.getVehicle().setVelocity(vector);
                    player.setVelocity(vector);
                    if(!failCounter.containsKey(uuid)) failCounter.put(uuid, 1);
                    if(failCounter.containsKey(uuid)) failCounter.replace(uuid, failCounter.get(uuid)+1);
                    return;
                }
                if (entrySound != null && region.getBox().contains(player.getLocation().toVector()) && !region.getBox().contains(playerLocations.get(uuid).toVector())) {
                    if (entryExitTitles) player.sendMessage(Colorize.format(entryGreeting));
                    if (entryExitSounds) player.playSound(playerLocations.get(uuid), entrySound, 1, 1);
                }
                if (exitSound != null && region.getBox().contains(playerLocations.get(uuid).toVector()) && !region.getBox().contains(player.getLocation().toVector())) {
                    if (entryExitTitles) player.sendMessage(Colorize.format(exitFairwell));
                    if (entryExitSounds) player.playSound(playerLocations.get(uuid), exitSound, 1, 1);
                }
                if (region.getBox().contains(player.getLocation().toVector()) && !region.getPlayersInside().contains(uuid))
                    scheduler.runTask(plugin, ()->{
                        region.addPlayer(uuid);
                    });
                if (!region.getBox().contains(player.getLocation().toVector()) && region.getPlayersInside().contains(uuid))
                    scheduler.runTask(plugin, ()->{
                        region.removePlayer(uuid);
                    });
                playerLocations.replace(uuid, player.getLocation());
            }
        }, 0L, 5L);

        scheduler.runTaskTimerAsynchronously(plugin, (task)->{
            if (region.getState() == RegionState.DELETED) {
                task.cancel();
            }
            if (region.getState() == RegionState.IDLE) return;
            for(UUID uuid : failCounter.keySet()){
                if(failCounter.get(uuid) > 3){
                    scheduler.runTask(plugin, ()->{
                        Bukkit.getPlayer(uuid).teleport(playerLocations.get(uuid));
                    });
                    failCounter.remove(uuid);
                    return;
                }
                failCounter.replace(uuid, failCounter.get(uuid)-1);
                if(failCounter.get(uuid) <= 0) failCounter.remove(uuid);
            }
        }, 0L, 7L);

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
        region.removePlayer(e.getPlayer().getUniqueId());
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
        region.removePlayer(e.getPlayer().getUniqueId());
        playerLocations.remove(e.getPlayer().getUniqueId());
    }
    @EventHandler
    public void teleportHandler(PlayerTeleportEvent e){
        if(entry && exit)return;
        if(region.getState() == RegionState.IDLE)return;
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN){
            playerLocations.replace(e.getPlayer().getUniqueId(), e.getTo());
            return;
        }
        if(!entry && region.getBox().contains(e.getTo().toVector()) && !region.getPlayersInside().contains(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
        }
        if(!exit && !region.getBox().contains(e.getTo().toVector()) && region.getPlayersInside().contains(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(region.getState() == RegionState.IDLE)return;
        if(e.getClickedBlock() == null)return;
        if(e.getInteractionPoint() == null)return;
        if(!entry){
            if(!region.getBox().contains(playerLocations.get(e.getPlayer().getUniqueId()).toVector()) && region.getBox().contains(e.getInteractionPoint().toVector())) e.setCancelled(true);
        }
        if(!exit){
            if(region.getBox().contains(playerLocations.get(e.getPlayer().getUniqueId()).toVector()) && !region.getBox().contains(e.getInteractionPoint().toVector())) e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent e){
        if(region.getState() == RegionState.IDLE)return;
        if(!entry){
            if(!region.getBox().contains(playerLocations.get(e.getPlayer().getUniqueId()).toVector()) && region.getBox().contains(e.getRightClicked().getLocation().toVector()) && !region.getPlayersInside().contains(e.getPlayer().getUniqueId())) e.setCancelled(true);
        }
        if(!exit){
            if(region.getBox().contains(playerLocations.get(e.getPlayer().getUniqueId()).toVector()) && !region.getBox().contains(e.getRightClicked().getLocation().toVector()) && region.getPlayersInside().contains(e.getPlayer().getUniqueId())) e.setCancelled(true);
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
