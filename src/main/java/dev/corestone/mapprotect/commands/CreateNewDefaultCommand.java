package dev.corestone.mapprotect.commands;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.utilities.Colorize;
import dev.corestone.mapprotect.utilities.PlayerMessage;
import org.apache.logging.log4j.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CreateNewDefaultCommand implements CommandExecutor, TabCompleter {

    private MapProtect plugin;

    public CreateNewDefaultCommand(MapProtect plugin){
        this.plugin = plugin;
        plugin.getCommand("mpcreatedefault").setExecutor(this);
        plugin.getCommand("mpcreatedefault").setTabCompleter(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        ArrayList<String> argsList = new ArrayList<>();
        for(int j = 0; args.length > j; j++){
            argsList.add(args[j]);
        }
        if(argsList.isEmpty()){
            sender.sendMessage(Colorize.format(PlayerMessage.noArgs));
        }
        String defaultName = args[0];
        String existingProfile = args[1];

        if(plugin.getDefaultData().getDefaultList().contains(defaultName)){
            sender.sendMessage(Colorize.format("&cA default with that name already exists."));
            sender.sendMessage(Colorize.format(PlayerMessage.mpCreateDefault));
            return true;
        }
        if(!plugin.getRegionData().getRegionList().contains(existingProfile)){
            sender.sendMessage(Colorize.format("&cAn existing region with that name does not exist."));
            sender.sendMessage(Colorize.format(PlayerMessage.mpCreateDefault));
            return true;
        }
        plugin.getDefaultData().createNewDefaultProfile(defaultName, existingProfile);
        sender.sendMessage(Colorize.format("&3Creating new default &l&b" + defaultName + "&3 from profile &l&b" + existingProfile));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(args.length >= 2){
            return plugin.getRegionData().getRegionList();
        }
        return null;
    }
}
