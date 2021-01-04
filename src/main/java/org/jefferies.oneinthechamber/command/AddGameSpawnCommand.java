package org.jefferies.oneinthechamber.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jefferies.gamelibs.utils.command.Command;
import org.jefferies.oneinthechamber.OITCGamemode;

import java.util.UUID;

public class AddGameSpawnCommand extends Command {

    private OITCGamemode gamemode;

    public AddGameSpawnCommand(OITCGamemode gamemode){
        super("addgamespawn");
        addPermission("oitc.admin");
        this.gamemode = gamemode;
    }

    @Override
    public void onExecute(Player player, String[] args) {
        gamemode.getSpawns().add(player.getLocation());
        gamemode.getConfiguration().setLocation(UUID.randomUUID().toString(), player.getLocation());
        player.sendMessage(ChatColor.GREEN + "You have added a one in the chamber spawn location.");
    }
}
