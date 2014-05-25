package io.github.SixOneThree.BaconEvent.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaconEventCommandMaxHealth implements CommandExecutor {
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] split) {
		if (cmd.getName().equalsIgnoreCase("maxhealth")) {
			if (!(sender instanceof Player)) {
				if (!(split.length == 2)) {
					sender.sendMessage("Usage: /maxhealth <player> <maxhealth>");
					return true;
				} else {
					Player target = (Bukkit.getServer().getPlayer(split[0]));
					if (target == null) {
						sender.sendMessage(split[0] + " is not online!");
						return true;
					} else {
						Double MaxHealth = Double.parseDouble(split[1]);
						target.setMaxHealth(MaxHealth);
						target.setHealth(MaxHealth);
						return true;
					}
				}
			} else {
				Player player = (Player) sender;
				if (player.hasPermission("baconevent.maxhealth")) {
					if (!(split.length == 2)) {
						player.sendMessage("Usage: /maxhealth <player> <maxhealth>");
						return true;
					} else {
						Player target = (Bukkit.getServer().getPlayer(split[0]));
						if (target == null) {
							sender.sendMessage(split[0] + " is not online!");
							return true;
						} else {
							Double MaxHealth = Double.parseDouble(split[1]);
							target.setMaxHealth(MaxHealth);
							target.setHealth(MaxHealth);
							return true;
						}
					}
				}
			}
			return true;
		}
		return false;
	}
}
