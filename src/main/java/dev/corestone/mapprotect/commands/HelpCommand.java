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

        sender.sendMessage(Colorize.format(PlayerMessage.commandUsage));
        sender.sendMessage("");
        sender.sendMessage(Colorize.format(PlayerMessage.mpWandUsage));
        sender.sendMessage(Colorize.format(PlayerMessage.mpReload));
        sender.sendMessage(Colorize.format(PlayerMessage.mpCreateCmdUsage));
        sender.sendMessage(Colorize.format(PlayerMessage.mpRemoveMapUsage));
        sender.sendMessage(Colorize.format(PlayerMessage.mpCreateDefault));
        sender.sendMessage(Colorize.format(PlayerMessage.mpRemoveDefault));
        sender.sendMessage(Colorize.format(PlayerMessage.mpTeleportPlayer));
        sender.sendMessage(Colorize.format(PlayerMessage.mpTeleport));
        sender.sendMessage(Colorize.format(PlayerMessage.mpSetMapSpawn));
        sender.sendMessage("");

        return true;
    }
}
