package main;

import armorstandnotnice.Minion;
import armorstandnotnice.TimeStarter;
import commands.SpawnArmorStand;
import listener.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import chestrecipeloading.RecipeLoader;
import commands.OpenInventory;

import java.util.ArrayList;

public class Main extends JavaPlugin{
	
	public static Main plugin;
	
	public void onEnable() {
		
		plugin = this;
		
		print("Plugin started");

		new RecipeLoader();
		
		getCommand("inv").setExecutor(new OpenInventory());
		getCommand("minion").setExecutor(new SpawnArmorStand());
		
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new InventoryChestRecipeCheck(), this);
		pm.registerEvents(new InventoryClickCheck(), this);
		pm.registerEvents(new OpenCraftingInventory(), this);
		pm.registerEvents(new SetMinion(), this);
		pm.registerEvents(new AsClickCheck(), this);
		pm.registerEvents(new ArmorStandArmorChange(), this);

		loadAsFromConfig();
		
	}
	public static void print(String text) {
		System.out.println("[Minions] " + text);
	}
	public static void loadAsFromConfig() {
		if(getPlugin().getConfig() != null) {
			for (int i = 0; i < getPlugin().getConfig().getInt("Minions.number"); i++) {
				if(getPlugin().getConfig().get("Minion.block." + i +  ".x") != null) {
					double x = (double) getPlugin().getConfig().get("Minion.block." + i + ".x");
					double y = (double) getPlugin().getConfig().get("Minion.block." + i + ".y");
					double z = (double) getPlugin().getConfig().get("Minion.block." + i + ".z");

					World w = Bukkit.getWorld((String) getPlugin().getConfig().get("Minion.block." + i + ".world"));

					Location locs = new Location(w,x, y, z);

					Cow aw = (Cow) w.spawnEntity(locs, EntityType.COW);

					ArrayList<Entity> entitiess = new ArrayList<>(aw.getNearbyEntities(0, 1, 0));
					aw.remove();

					for (Entity e: entitiess) {

						if(e instanceof ArmorStand) {
							ArmorStand as = (ArmorStand) e;
							if(as.isSmall()) {
								if(as.getHelmet() != null) {
									Minion.stands.add(as);
									print("Number of minions: " + Minion.stands.size());
								}
							}
						}
					}
				}
			}
			for (ArmorStand as: Minion.stands) {

				new TimeStarter(as);

			}
		}
	}
	public static Main getPlugin() {
		return plugin;
	}

}
