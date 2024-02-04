package dev.corestone.mapprotect.commands;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.RegionManager;
import dev.corestone.mapprotect.utilities.Colorize;
import dev.corestone.mapprotect.utilities.PlayerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DefaultProfileCommand implements CommandExecutor, TabExecutor {
    private MapProtect plugin;
    private RegionManager manager;

    public DefaultProfileCommand(MapProtect plugin, RegionManager manager){
        this.plugin = plugin;
        this.manager = manager;
        plugin.getCommand("mpmakedefault").setExecutor(this);
        plugin.getCommand("mpmakedefault").setExecutor(this);
        plugin.getCommand("mpremovedefault").setExecutor(this);
        plugin.getCommand("mpremovedefault").setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return true;
        Player player = (Player) sender;
        ArrayList<String> argsList = new ArrayList<>();
        for(int j = 0; args.length > j; j++){
            argsList.add(args[j]);
        }
        if(command.getName().equalsIgnoreCase("mpmakedefault")){
            if(argsList.isEmpty() || argsList.size() < 2) player.sendMessage(Colorize.format(PlayerMessage.noArgs));

            String defaultProfileName = args[0];
            String existingProfile = args[1];
            if(plugin.getRegionData().getRegionList().contains(existingProfile)){
                //Make Static method for this
                return true;
            }
            if(plugin.getRegionData().getDefaultProfileList().contains(defaultProfileName)){
                return true;
            }
            plugin.getRegionData().addDefaultProfile(defaultProfileName, existingProfile);
        }
        if(command.getName().equalsIgnoreCase("mpremovedefault")){
            if(argsList.isEmpty()) player.sendMessage(Colorize.format(PlayerMessage.noArgs));
            plugin.getRegionData().removeDefaultProfile(args[0]);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1){
            return plugin.getRegionData().getRegionList();
        }
        return new ArrayList<>();
    }
}
