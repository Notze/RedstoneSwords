package io.github.notze.redstoneswords;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedstoneSwords extends JavaPlugin {

	static FileConfiguration config;
	static Material swordMaterial;
	static int redstoneFactor, redstoneOreAmount, speedBoost, 
	speedBoostTime, expFactor, reclaimExpFactor, growthRadius;
	static boolean keepEnchantmentsOnRepair;
	
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

		swordMaterial = Material.getMaterial(config.getString("swordMaterial"));
		redstoneFactor = config.getInt("redstoneFactor");
		redstoneOreAmount =  config.getInt("redstoneOreAmount");
		speedBoost =  config.getInt("speedBoost");
		speedBoostTime =  config.getInt("speedBoostTime");
		expFactor =  config.getInt("expFactor");
		reclaimExpFactor =  config.getInt("reclaimExpFactor");
		keepEnchantmentsOnRepair =  config.getBoolean("keepEnchantmentsOnRepair");
		growthRadius = config.getInt("scroll.growthRadius");
	}
	
}
