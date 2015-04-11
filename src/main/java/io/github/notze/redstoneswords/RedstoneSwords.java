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
	respirationTime, torchFireTime, waterWalkTime, poisonTime, nightVisionTime,
	attackTime, attackBonus, shieldTime, reflectionTime, commandTime, 
	boundSwordTime, boundPickaxeTime, boundAxeTime, boundHoeTime, boundShovelTime;
	public static boolean keepEnchantmentsOnRepair, scrollLevitationDisabled, 
	scrollJumpDisabled, scrollGrowthDisabled, scrollWaterwalkDisabled, 
	scrollRespirationDisabled, scrollShieldDisabled, scrollNightvisionDisabled, 
	scrollAttackDisabled, scrollPoisonDisabled, scrollReflectionDisabled, 
	scrollCommandDisabled, boundDisabled, throwAxeDisabled, scrollRebirthDisabled, 
	scrollHealDisabled, scrollFireballDisabled, scrollRecallDisabled, 
	checkForUpdates, checkForBetaUpdates;
	public static float flightSpeed;
	
	@SuppressWarnings("unused")
	@Override
	public void onEnable() {
		initialiseConfig(); // should be ALWAYS the first here
		
		Crafting crafting = new Crafting(this);
		Events events = new Events(this);
		Repair repair = new Repair(this);
		Smelting smelting = new Smelting(this);
		UpdateChecker updateChecker = new UpdateChecker(this);
		
		getServer().getPluginManager().registerEvents(repair, this); // repair comes first
		getServer().getPluginManager().registerEvents(crafting, this); // special recipes override repairs
		getServer().getPluginManager().registerEvents(events, this); 
		getServer().getPluginManager().registerEvents(updateChecker, this); // for joining players
	
		// main commands
		this.getCommand("rshelp").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("rs").setExecutor(new RedstoneSwordsCommandExecutor(this));
		
		// Bonus commands
		this.getCommand("rsclearinventory").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("rsstoreinventory").setExecutor(new RedstoneSwordsCommandExecutor(this));
		
		// Update Checker
		updateChecker.execute();
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
		torchFireTime = config.getInt("torchFireTime");
		
		growthRadius = config.getInt("scroll.growth.range");
		flightTime = config.getInt("scroll.levitation.duration");
		flightSpeed = (float) config.getDouble("scroll.levitation.speed");
		jumpTime = config.getInt("scroll.jump.duration");
		respirationTime = config.getInt("scroll.respiration.duration");
		waterWalkTime = config.getInt("scroll.waterwalk.duration");
		poisonTime = config.getInt("scroll.poison.duration");
		nightVisionTime = config.getInt("scroll.nightvision.duration");
		attackTime = config.getInt("scroll.attack.duration");
		attackBonus = config.getInt("scroll.attack.bonus");
		shieldTime = config.getInt("scroll.shield.duration");
		reflectionTime = config.getInt("scroll.reflection.duration");
		commandTime = config.getInt("scroll.command.duration");
		boundSwordTime = config.getInt("scroll.bound.sword.duration");
		boundPickaxeTime = config.getInt("scroll.bound.pickaxe.duration");
		boundShovelTime = config.getInt("scroll.bound.shovel.duration");
		boundAxeTime = config.getInt("scroll.bound.axe.duration");
		boundHoeTime = config.getInt("scroll.bound.hoe.duration");
		
		woodAxeDmg = config.getInt("thrownAxeDamage.wood");
		stoneAxeDmg = config.getInt("thrownAxeDamage.stone");
		ironAxeDmg = config.getInt("thrownAxeDamage.iron");
		goldAxeDmg = config.getInt("thrownAxeDamage.gold");
		diamondAxeDmg = config.getInt("thrownAxeDamage.diamond");
		
		scrollLevitationDisabled = config.getBoolean("scroll.levitation.disabled");
		scrollJumpDisabled = config.getBoolean("scroll.jump.disabled");
		scrollGrowthDisabled = config.getBoolean("scroll.growth.disabled");
		scrollWaterwalkDisabled = config.getBoolean("scroll.waterwalk.disabled");
		scrollRespirationDisabled = config.getBoolean("scroll.respiration.disabled");
		scrollShieldDisabled = config.getBoolean("scroll.shield.disabled");
		scrollNightvisionDisabled = config.getBoolean("scroll.nightvision.disabled");
		scrollAttackDisabled = config.getBoolean("scroll.attack.disabled");
		scrollPoisonDisabled = config.getBoolean("scroll.poison.disabled");
		scrollReflectionDisabled = config.getBoolean("scroll.reflection.disabled");
		scrollCommandDisabled = config.getBoolean("scroll.command.disabled");
		scrollRebirthDisabled = config.getBoolean("scroll.rebirth.disabled");
		scrollHealDisabled = config.getBoolean("scroll.heal.disabled");
		scrollFireballDisabled = config.getBoolean("scroll.fireball.disabled");
		scrollRecallDisabled = config.getBoolean("scroll.recall.disabled");
		boundDisabled = config.getBoolean("scroll.bound.disabled");
		throwAxeDisabled = config.getBoolean("thrownAxeDamage.disabled");
		
		checkForUpdates = config.getBoolean("updatechecker.enabled");
		checkForBetaUpdates = config.getBoolean("updatechecker.includebeta");
	}
	
}
