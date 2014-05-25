package io.github.SixOneThree.BaconEvent.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaconEventCommandHub implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] split) {
		if (cmd.getName().equalsIgnoreCase("hub")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by players!");
				return true;
			} else {
				Player player = (Player) sender;
				World world = Bukkit.getWorld("flatland");
				Location spawn = new Location(world, -79.5, 4, 249.5);
				player.teleport(spawn);
				return true;
			}
		}
		return true;
	}
}
