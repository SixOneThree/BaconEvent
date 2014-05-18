package io.github.SixOneThree.BaconEvent;

import org.bukkit.plugin.java.JavaPlugin;

public final class BaconEvent extends JavaPlugin {
	private static BaconEvent instance;
	@Override
	public void onEnable() {
		instance = this;
		getCommand("quadsmite").setExecutor(new BaconEventCommandExecutor());
		getCommand("maxhealth").setExecutor(new BaconEventCommandExecutor());
	}
	
	public static BaconEvent getInstance() {
		return instance;
	}
	
	@Override
	public void onDisable() {
		getLogger().info("onDisable has been invoked!");
	}
}
