package dev.corestone.mapprotect.commands;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.regions.RegionBox;
import dev.corestone.mapprotect.utilities.Colorize;
import dev.corestone.mapprotect.utilities.PlayerMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MapTeleportCommand implements CommandExecutor, TabCompleter {

    private MapProtect plugin;

    public MapTeleportCommand(MapProtect plugin){
        this.plugin = plugin;
        plugin.getCommand("mpteleport").setExecutor(this);
        plugin.getCommand("mpteleport").setTabCompleter(this);

        plugin.getCommand("mpteleportplayer").setExecutor(this);
        plugin.getCommand("mpteleportplayer").setTabCompleter(this);

        plugin.getCommand("mpsetmapspawn").setExecutor(this);
        plugin.getCommand("mpsetmapspawn").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        ArrayList<String> argsList = new ArrayList<>();
        for(int j = 0; args.length > j; j++){
            argsList.add(args[j]);
        }
        if(argsList.isEmpty()){
            sender.sendMessage(Colorize.format(PlayerMessage.noArgs));
            return true;
        }
        if(command.getName().equalsIgnoreCase("mpteleport") || command.getName().equalsIgnoreCase("mptp")){
            if(!(sender instanceof Player))return true;
            Player player = (Player)sender;
            String mapName = args[0];
            if(!plugin.getRegionData().getRegionList().contains(mapName)){
                player.sendMessage(Colorize.format(PlayerMessage.noArgs));
                return true;
            }
            player.sendMessage(Colorize.format("&3Teleporting you to &b" + mapName +"&3."));
            player.teleport(plugin.getLocationData().getMapSpawn(mapName));
        }
        if(command.getName().equalsIgnoreCase("mpteleportplayer")){
            if(argsList.size() != 2){
                sender.sendMessage(Colorize.format(PlayerMessage.noArgs));
                return true;
            }
            String playerTP = args[0];
            String mapName = args[1];
            if(!plugin.getRegionData().getRegionList().contains(mapName)){
                sender.sendMessage(Colorize.format(PlayerMessage.noArgs));
                return true;
            }
            if(playerTP.equalsIgnoreCase("@a")){
                for(Player player1 : Bukkit.getOnlinePlayers()){
                    player1.teleport(plugin.getLocationData().getMapSpawn(mapName));
                }
                sender.sendMessage(Colorize.format("&3Teleproting everyone to &b" + mapName+"&3."));
                return true;
            }
            if(!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(playerTP))){
                sender.sendMessage(Colorize.format(PlayerMessage.noArgs));
                return true;
            }

            Bukkit.getPlayer(playerTP).teleport(plugin.getLocationData().getMapSpawn(mapName));
            sender.sendMessage(Colorize.format("&3Teleproting &b" + playerTP + "&3 to &b" + mapName + "&3."));
        }
        if(command.getName().equalsIgnoreCase("mpsetmapspawn") || command.getName().equalsIgnoreCase("mpsetspawn")){
            if(!(sender instanceof Player))return true;
            Player player = (Player) sender;
            String mapName = args[0];
            if(argsList.size() != 1){
                player.sendMessage(Colorize.format(PlayerMessage.noArgs));
            }
            if(!plugin.getRegionData().getRegionList().contains(mapName)){
                player.sendMessage(Colorize.format(PlayerMessage.noArgs));
                return true;
            }
            if(!plugin.getManager().getRegion(mapName).getBox().contains(player.getLocation().toVector())){
                player.sendMessage(Colorize.format("&cYou are not currently inside the region &b" + mapName + "&c."));
                player.sendMessage(Colorize.format(PlayerMessage.noArgs));
                return true;
            }
            player.sendMessage(Colorize.format("&3Setting the spawn location of &b" + mapName + "&3 at your location."));
            plugin.getLocationData().setSpawnLoc(mapName, player.getLocation());
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(sender instanceof Player))return null;
        Player player = (Player) sender;
        if(command.getName().equalsIgnoreCase("mpteleport") || command.getName().equalsIgnoreCase("mptp")){
            return plugin.getRegionData().getRegionList();
        }
        if(command.getName().equalsIgnoreCase("mpteleportplayer")){
            if(strings.length == 1){
                ArrayList<String> list = new ArrayList<>();
                for(Player player2 : Bukkit.getOnlinePlayers()){
                    list.add(player2.getName());
                }
                list.add("@a");
                return list;
            }
            if (strings.length == 2){
                return plugin.getRegionData().getRegionList();
            }

        }
        if(command.getName().equalsIgnoreCase("mpsetspawn") || command.getName().equalsIgnoreCase("mpsetmapspawn")){
            ArrayList<String> maps = new ArrayList<>();
            for(RegionBox regionBox : plugin.getManager().getRegionList()){
                if(regionBox.getBox().contains(player.getLocation().toVector())){
                    maps.add(regionBox.getName());
                }
            }
            return maps;
        }
        return null;
    }
}
