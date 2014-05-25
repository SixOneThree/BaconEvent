package io.github.SixOneThree.BaconEvent;

import io.github.SixOneThree.BaconEvent.commands.BaconEventCommandHub;
import io.github.SixOneThree.BaconEvent.commands.BaconEventCommandMaxHealth;
import io.github.SixOneThree.BaconEvent.commands.BaconEventCommandQuadsmite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class BaconEvent extends JavaPlugin implements Listener {
	private static BaconEvent instance;
	
	public void HubItemHelper(Player player) {
		ItemStack MinigameCompass = new ItemStack (Material.COMPASS);
		ItemStack HubClock = new ItemStack (Material.WATCH);
		ItemStack EOEPlayerVisToggle = new ItemStack (Material.EYE_OF_ENDER);
		
		ItemMeta MinigameCompassMeta = MinigameCompass.getItemMeta();
		ItemMeta HubClockMeta = HubClock.getItemMeta();
		ItemMeta EOEPlayerVisToggleMeta = EOEPlayerVisToggle.getItemMeta();

		ArrayList<String> UndroppableLore = new ArrayList<String>();
		UndroppableLore.add(ChatColor.GOLD + "Undroppable Item");
		
		MinigameCompassMeta.setDisplayName(ChatColor.AQUA + "Minigame Selector");
		HubClockMeta.setDisplayName(ChatColor.AQUA + "Hub Selector");
		EOEPlayerVisToggleMeta.setDisplayName(ChatColor.AQUA + "Hide Players");
		
		MinigameCompassMeta.setLore(UndroppableLore);
		HubClockMeta.setLore(UndroppableLore);
		EOEPlayerVisToggleMeta.setLore(UndroppableLore);
		
		MinigameCompass.setItemMeta(MinigameCompassMeta);
		HubClock.setItemMeta(HubClockMeta);
		EOEPlayerVisToggle.setItemMeta(EOEPlayerVisToggleMeta);
		
/*		TOP ROW: 9,10,11,12,13,14,15,16,17
		MIDDLE ROW: 18,19,20,21,22,23,24,25,26
		BOTTOM ROW: 27,28,29,30,31,32,33,34,35
		HOTBAR: 0,1,2,3,4,5,6,7,8*/
		
		player.getInventory().clear();
		player.getInventory().setItem(8, MinigameCompass);
		player.getInventory().setItem(7, HubClock);
		player.getInventory().setItem(6, EOEPlayerVisToggle);
		player.getInventory().setHeldItemSlot(0);
		player.setMaxHealth(20);
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.setLevel(0);
	}
	
	public void HubTeleportHelper(Player player) {
		World world = Bukkit.getWorld("flatland");
		Location spawn = new Location(world, -79.5, 4, 249.5);
		player.teleport(spawn);
	}
	
	public void MinigameHelper(final Player player, String minigame) {
		if (minigame == "DrawMyThing") {
			Boolean worldfound = false;
			Integer lobbynumber = 1;
			for (World possibleworld : Bukkit.getWorlds()) {
				if (possibleworld.getName().startsWith("DMTLobby")) {
					Integer population = possibleworld.getPlayers().size();
					lobbynumber += 1;
					if (population < 9) {
						worldfound = true;
						Location DMTLobbyLocation = new Location(possibleworld, 0, 64, 0);
						player.teleport(DMTLobbyLocation);
						break;
					}
				}
			}
			if (!worldfound) {
				final Integer finallobbynumber = lobbynumber;
				player.sendMessage(ChatColor.RED + "A lobby for Draw My Thing was not found, you will be teleported when it is finished generating.");
				WorldCreator c = new WorldCreator("DMTLobby" + lobbynumber.toString());
				c.copy(Bukkit.getWorld("DrawMyThingLobbyTemplate"));
				c.createWorld();
				Bukkit.getScheduler().scheduleSyncDelayedTask(BaconEvent.getInstance(), new Runnable() {
					public void run() {
						Bukkit.unloadWorld("DrawMyThingLobbyTemplate", true);
						Bukkit.unloadWorld(Bukkit.getWorld("DMTLobby" + finallobbynumber), false);
					}
				}, 200L);
				Bukkit.getScheduler().scheduleSyncDelayedTask(BaconEvent.getInstance(), new Runnable() {
					public void run() {
						player.sendMessage(ChatColor.RED + "World being converted to lobby...");
//						copyWorld(Bukkit.getWorld("DrawMyThingLobbyTemplate").getWorldFolder(), Bukkit.getWorld("DMTLobby" + finallobbynumber.toString()).getWorldFolder());
						copyWorld(Bukkit.getWorldContainer(), Bukkit.getWorldContainer());
					}
				}, 400L);
				Bukkit.getScheduler().scheduleSyncDelayedTask(BaconEvent.getInstance(), new Runnable() {
					public void run() {
						Bukkit.getServer().createWorld(new WorldCreator("DrawMyThingLobbyTemplate"));
						Bukkit.getServer().createWorld(new WorldCreator("DMTLobby" + finallobbynumber));
					}
				}, 600L);
				Bukkit.getScheduler().scheduleSyncDelayedTask(BaconEvent.getInstance(), new Runnable() {
					public void run() {
						World newworld = Bukkit.getWorld("DMTLobby" + finallobbynumber.toString());
						Location newworldlocation = new Location(newworld, 0, 64, 0);
						player.teleport(newworldlocation);
					}
				}, 800L);
			}
		}
	}
	
	public void copyWorld(File source, File target){
	    try {
	        ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
	        if(!ignore.contains(source.getName())) {
	            if(source.isDirectory()) {
	                if(!target.exists())
	                target.mkdirs();
	                String files[] = source.list();
	                for (String file : files) {
	                    File srcFile = new File(source, file);
	                    File destFile = new File(target, file);
	                    copyWorld(srcFile, destFile);
	                }
	            } else {
	                InputStream in = new FileInputStream(source);
	                OutputStream out = new FileOutputStream(target);
	                byte[] buffer = new byte[1024];
	                int length;
	                while ((length = in.read(buffer)) > 0)
	                    out.write(buffer, 0, length);
	                in.close();
	                out.close();
	            }
	        }
	    } catch (IOException e) {
	 
	    }
	}
	
	@Override
	public void onEnable() {
		instance = this;
		getCommand("quadsmite").setExecutor(new BaconEventCommandQuadsmite());
		getCommand("maxhealth").setExecutor(new BaconEventCommandMaxHealth());
		getCommand("hub").setExecutor(new BaconEventCommandHub());
		
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(ChatColor.GREEN + "Server (Re)Loaded! Teleported to HUB!");
			HubTeleportHelper(player);
		}
	}
	
	public static BaconEvent getInstance() {
		return instance;
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Bacon Event has been unloaded!");
	}
	
	@EventHandler
	public void OnPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		HubTeleportHelper(player);
		HubItemHelper(player);
	}

	@EventHandler
	public void OnPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		
		HubTeleportHelper(player);
		HubItemHelper(player);
	}
	
	@EventHandler
	public void OnPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		if (Bukkit.getWorld("flatland") == player.getWorld()) {
			event.getDrops().clear();
		}
	}
	
	@EventHandler
	public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
		Player player = event.getPlayer();
		if (Bukkit.getWorld("flatland") == player.getWorld()) {
			if (player.getGameMode() == GameMode.CREATIVE) return;
			event.setCancelled(true);
			player.setAllowFlight(false);
			player.setFlying(false);
			player.setVelocity(player.getLocation().getDirection().multiply(1.5).setY(1));
		} else {
			if (player.getGameMode() == GameMode.SURVIVAL) {
				player.setAllowFlight(false);
				player.setFlying(false);
			}
		}
	}
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		if (Bukkit.getWorld("flatland") == player.getWorld()) {
			if ((player.getGameMode() != GameMode.CREATIVE) && (player.getLocation().subtract(0, 1, 0).getBlock().getType() != Material.AIR) && (!player.isFlying())) {
				player.setAllowFlight(true);
			}
			if (player.getLocation().getBlockY() < 0) {
				HubTeleportHelper(player);
			}
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Location teleportedfrom = event.getFrom();
		Location teleportedto = event.getTo();
		World oldworld = teleportedfrom.getWorld();
		World world = teleportedto.getWorld();
		Player player = event.getPlayer();
		if (Bukkit.getWorld("flatland") == world) {
			if (Bukkit.getWorld("flatland" )!= oldworld) {
				HubItemHelper(player);
			}
		} else {
			if (Bukkit.getWorld("flatland") == oldworld) {
				player.getInventory().clear();
			}
		}
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Action action = event.getAction();
		Player player = event.getPlayer();
		ItemStack itemstack = event.getItem();
		ArrayList<String> UndroppableLore = new ArrayList<String>();
		UndroppableLore.add(ChatColor.GOLD + "Undroppable Item");
		if ((action == Action.RIGHT_CLICK_AIR) && (itemstack.hasItemMeta())) {
			if (itemstack.getItemMeta().getLore().equals(UndroppableLore)) {
				if ((itemstack.getType() == Material.COMPASS) && (itemstack.hasItemMeta())) {
					openGUI(player, "Compass");	
				}
				if ((itemstack.getType() == Material.WATCH) && (itemstack.hasItemMeta())) {
					if (player.isSneaking()) {
						HubTeleportHelper(player);
					} else {
						openGUI(player, "Clock");
					}
				}
				if (itemstack.getType() == Material.EYE_OF_ENDER) {
					String displayname = itemstack.getItemMeta().getDisplayName();
					displayname = ChatColor.stripColor(displayname);
					if (displayname.equalsIgnoreCase("Hide Players")) {
						ItemMeta EOEPlayerVisiblityTogglerMeta = itemstack.getItemMeta();
						EOEPlayerVisiblityTogglerMeta.setDisplayName(ChatColor.AQUA + "Show Players");
						itemstack.setItemMeta(EOEPlayerVisiblityTogglerMeta);
						for (Player other : Bukkit.getOnlinePlayers()) {
							if (other != player) {
								player.hidePlayer(other);
							}
						}
					} else {
						ItemMeta EOEPlayerVisiblityTogglerMeta = itemstack.getItemMeta();
						EOEPlayerVisiblityTogglerMeta.setDisplayName(ChatColor.AQUA + "Hide Players");
						itemstack.setItemMeta(EOEPlayerVisiblityTogglerMeta);
						for (Player other : Bukkit.getOnlinePlayers()) {
							if (other != player) {
								player.showPlayer(other);
							}
						}
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if ((ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Please Select Minigame")) || (ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Choose Hub Location"))) {
			Player player = (Player) event.getWhoClicked();
			event.setCancelled(true);
		
			if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR || !event.getCurrentItem().hasItemMeta()) {
				player.closeInventory();
				return;
			}
			
			if (ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Please Select Minigame")) {
				switch (event.getCurrentItem().getType()) {
				case BOOK_AND_QUILL:
					player.closeInventory();
					MinigameHelper(player, "DrawMyThing");
					break;
				case DIAMOND_PICKAXE:
					player.closeInventory();
					player.sendMessage(String.format("%sSurvival %sis not yet complete.", ChatColor.RED, ChatColor.WHITE));
					break;
				default:
					player.closeInventory();
					break;
				}
			}
			if (ChatColor.stripColor(event.getInventory().getName()).equalsIgnoreCase("Choose Hub Location")) {
				World hubworld = Bukkit.getWorld("flatland");
				Location centerhub = new Location(hubworld, -79.5, 4, 249.5);
				Location lapishub = new Location(hubworld, -6.5, 4, 249.5);
				Location redstonehub = new Location(hubworld, -79.5, 4, 324);
				Location diamondhub = new Location(hubworld, -154.5, 4, 249.5);
				Location goldhub = new Location(hubworld, -79.5, 4, 174.5);
				diamondhub.setYaw(90);
				goldhub.setYaw(180);
				lapishub.setYaw(-90);
				switch (event.getCurrentItem().getType()) {
				case LAPIS_BLOCK:
					player.closeInventory();
					player.teleport(lapishub);
					break;
				case REDSTONE_BLOCK:
					player.closeInventory();
					player.teleport(redstonehub);
					break;
				case EMERALD_BLOCK:
					player.closeInventory();
					player.teleport(centerhub);
					break;
				case GOLD_BLOCK:
					player.closeInventory();
					player.teleport(goldhub);
					break;
				case DIAMOND_BLOCK:
					player.closeInventory();
					player.teleport(diamondhub);
					break;
				default:
					player.closeInventory();
					break;
				}
			}
		} else return;
	}
	
	@EventHandler
	public void PlayerDropItemEvent(PlayerDropItemEvent event) { //FIXME Refer to onInventoryClick
		Player player = event.getPlayer();
		Item rawitemdrop = event.getItemDrop();
		ItemStack itemdrop = rawitemdrop.getItemStack();
		
		ArrayList<String> UndroppableLore = new ArrayList<String>();
		UndroppableLore.add(ChatColor.GOLD + "Undroppable Item");
		
		if (itemdrop.hasItemMeta()) {
			if (itemdrop.getItemMeta().getLore().equals(UndroppableLore)) {
				event.setCancelled(true);
				player.sendMessage(ChatColor.RED + "You may not drop this item!");
			}
		}
	}
	
	private void openGUI(Player player, String tool) {
		Inventory gameinv = Bukkit.createInventory(null, 9, ChatColor.BOLD + "" + ChatColor.GOLD + "Please Select Minigame");
		Inventory hubinv = Bukkit.createInventory(null, 9, ChatColor.BOLD + "" + ChatColor.RED + "Choose Hub Location");
		
		// gameinv ItemStacks
		ItemStack DrawMyThing = new ItemStack (Material.BOOK_AND_QUILL);
		ItemStack Survival = new ItemStack (Material.DIAMOND_PICKAXE);
		// hubinv ItemStacks
		ItemStack MinigamesOption = new ItemStack (Material.LAPIS_BLOCK);
		ItemStack SurvivalGamesOption = new ItemStack (Material.REDSTONE_BLOCK);
		ItemStack HubOption = new ItemStack (Material.EMERALD_BLOCK);
		ItemStack ShopOption = new ItemStack (Material.GOLD_BLOCK);
		ItemStack StaffOption = new ItemStack (Material.DIAMOND_BLOCK);
		
		// gameinv ItemMetas
		ItemMeta DrawMyThingMeta = DrawMyThing.getItemMeta();
		ItemMeta SurvivalMeta = Survival.getItemMeta();
		// hubinv ItemMetas
		ItemMeta MinigamesOptionMeta = MinigamesOption.getItemMeta();
		ItemMeta SurvivalGamesOptionMeta = SurvivalGamesOption.getItemMeta();
		ItemMeta HubOptionMeta = HubOption.getItemMeta();
		ItemMeta ShopOptionMeta = ShopOption.getItemMeta();
		ItemMeta StaffOptionMeta = StaffOption.getItemMeta();

		// gameinv setDisplayNames
		DrawMyThingMeta.setDisplayName(ChatColor.GREEN + "Draw My Thing!");
		SurvivalMeta.setDisplayName(ChatColor.RED + "Survival");
		// hubinv setDisplayNames
		MinigamesOptionMeta.setDisplayName(ChatColor.BLUE + "Minigames");
		SurvivalGamesOptionMeta.setDisplayName(ChatColor.RED + "Survival Games");
		HubOptionMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "Hub");
		ShopOptionMeta.setDisplayName(ChatColor.GOLD + "Shop");
		StaffOptionMeta.setDisplayName(ChatColor.AQUA + "Staff");
		
		// gameinv setItemMetas
		DrawMyThing.setItemMeta(DrawMyThingMeta);
		Survival.setItemMeta(SurvivalMeta);
		// hubinv setItemMetas
		MinigamesOption.setItemMeta(MinigamesOptionMeta);
		SurvivalGamesOption.setItemMeta(SurvivalGamesOptionMeta);
		HubOption.setItemMeta(HubOptionMeta);
		ShopOption.setItemMeta(ShopOptionMeta);
		StaffOption.setItemMeta(StaffOptionMeta);
		
		// gameinv setItems
		gameinv.setItem(0, DrawMyThing);
		gameinv.setItem(8, Survival);
		// hubinv setItems
		hubinv.setItem(0, MinigamesOption);
		hubinv.setItem(2, SurvivalGamesOption);
		hubinv.setItem(4, HubOption);
		hubinv.setItem(6, ShopOption);
		hubinv.setItem(8, StaffOption);
		
		if (tool == "Compass") {
			player.openInventory(gameinv);
		}
		if (tool == "Clock") {
			player.openInventory(hubinv);
		}
	}
}
