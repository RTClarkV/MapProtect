package dev.corestone.mapprotect.commands;

import dev.corestone.mapprotect.MapProtect;
import dev.corestone.mapprotect.utilities.Colorize;
import dev.corestone.mapprotect.utilities.PlayerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HelpCommand implements CommandExecutor {
    private MapProtect plugin;

    public HelpCommand(MapProtect plugin){
        plugin.getCommand("mphelp").setExecutor(this);
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player))return true;

        Player player = (Player)sender;
        player.sendMessage(Colorize.format(PlayerMessage.commandUsage));
        player.sendMessage("");
        player.sendMessage(Colorize.format(PlayerMessage.mpWandUsage));
        player.sendMessage(Colorize.format(PlayerMessage.mpReload));
        player.sendMessage(Colorize.format(PlayerMessage.mpCreateCmdUsage));
        player.sendMessage(Colorize.format(PlayerMessage.mpRemoveMapUsage));
        player.sendMessage(Colorize.format(PlayerMessage.mpCreateDefault));
        player.sendMessage(Colorize.format(PlayerMessage.mpRemoveDefault));
        player.sendMessage(Colorize.format(PlayerMessage.mpTeleportPlayer));
        player.sendMessage(Colorize.format(PlayerMessage.mpTeleport));
        player.sendMessage(Colorize.format(PlayerMessage.mpSetMapSpawn));
        player.sendMessage("");

        return true;
    }
}
