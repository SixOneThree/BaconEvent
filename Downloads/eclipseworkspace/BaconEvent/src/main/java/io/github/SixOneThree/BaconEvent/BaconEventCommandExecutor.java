package io.github.SixOneThree.BaconEvent;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaconEventCommandExecutor implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("quadsmite")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by players!");
				return true;
			} else {
				Player player = (Player) sender;
				if (player.hasPermission("baconevent.quadsmite")) {
					Location location1 = player.getLocation();
					Location location2 = player.getLocation();
					Location location3 = player.getLocation();
					Location location4 = player.getLocation();
					location1.setX(location1.getX() + 5);
					location1.setZ(location1.getZ() + 5);
					location2.setX(location1.getX() + 5);
					location2.setZ(location1.getZ() - 5);
					location3.setX(location1.getX() - 5);
					location3.setZ(location1.getZ() + 5);
					location4.setX(location1.getX() - 5);
					location4.setZ(location1.getZ() - 5);
					player.getWorld().strikeLightning(location1);
					player.getWorld().strikeLightning(location2);
					player.getWorld().strikeLightning(location3);
					player.getWorld().strikeLightning(location4);
					return true;
				}
			}
			return true;
		}
		return false;
	}
}