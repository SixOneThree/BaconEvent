package io.github.SixOneThree.BaconEvent;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaconEventCommandExecutor implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("trismite")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by players!");
				return false;
			} else {
				Player player = (Player) sender;
				if (player.hasPermission("baconevent.trismite")) {
					Location location = player.getLocation();
					location.setX(location.getX() + 5);
					player.getWorld().strikeLightning(location);
				}
			}
			return true;
		}
		return false;
	}
}