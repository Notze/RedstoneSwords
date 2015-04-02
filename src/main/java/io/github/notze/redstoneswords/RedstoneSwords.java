package io.github.notze.redstoneswords;

import io.github.notze.recipes.Crafting;
import io.github.notze.recipes.Repair;
import io.github.notze.recipes.Smelting;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedstoneSwords extends JavaPlugin {

	static FileConfiguration config;
	public static Material swordMaterial;
	public static int boostCost, teleportCost, redstoneOreAmount, speedBoost, 
	speedBoostTime, expFactor, reclaimExpFactor, growthRadius, woodAxeDmg, 
	stoneAxeDmg, ironAxeDmg, goldAxeDmg, diamondAxeDmg, flightTime, jumpTime,
	respirationTime;
	public static boolean keepEnchantmentsOnRepair;
	
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {
		initialiseConfig(); // should be ALWAYS the first here
		
		Crafting crafting = new Crafting(this);
		Events events = new Events(this);
		Repair repair = new Repair(this);
		Smelting smelting = new Smelting(this);
		
		getServer().getPluginManager().registerEvents(repair, this); // repair comes first
		getServer().getPluginManager().registerEvents(crafting, this); // special recipes override repairs
		getServer().getPluginManager().registerEvents(events, this); 
	
		// main commands
		this.getCommand("rshelp").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("rs").setExecutor(new RedstoneSwordsCommandExecutor(this));
		
		// Bonus commands
		this.getCommand("rsclearinventory").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("rsstoreinventory").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("test").setExecutor(new RedstoneSwordsCommandExecutor(this));
	}
	
	@Override
	public void onDisable() {}
	
	// initialise ConfigurationFile
	private void initialiseConfig(){
		this.saveDefaultConfig();
		config = getConfig();

		swordMaterial = Material.getMaterial(config.getString("sword.material"));
		boostCost = config.getInt("sword.cost.boost");
		teleportCost = config.getInt("sword.cost.teleport");
		redstoneOreAmount =  config.getInt("sword.redstoneOreAmount");
		speedBoost =  config.getInt("sword.multiplier.boost");
		speedBoostTime =  config.getInt("sword.duration.boost");
		expFactor =  config.getInt("sword.cost.exp");
		reclaimExpFactor =  config.getInt("reclaimExpFactor");
		keepEnchantmentsOnRepair =  config.getBoolean("keepEnchantmentsOnRepair");
		
		growthRadius = config.getInt("scroll.growthRadius");
		flightTime = config.getInt("scroll.flightTime");
		jumpTime = config.getInt("scroll.jumpTime");
		respirationTime = config.getInt("scroll.respirationTime");
		
		woodAxeDmg = config.getInt("thrownAxeDamage.wood");
		stoneAxeDmg = config.getInt("thrownAxeDamage.stone");
		ironAxeDmg = config.getInt("thrownAxeDamage.iron");
		goldAxeDmg = config.getInt("thrownAxeDamage.gold");
		diamondAxeDmg = config.getInt("thrownAxeDamage.diamond");
	}
	
}
