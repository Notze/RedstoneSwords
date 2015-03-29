package io.github.notze.mc_redstoneSwords;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class Crafting implements Listener {
	
	// reference to main class
	RedstoneSwords redstoneSwords;
	
	ShapedRecipe rswordUpgrade;
	ShapedRecipe rswordUpgradeBlocks;
	
	/**
	 * Adds new recipes
	 * 
	 * @param redstoneSwords
	 * 		reference to main class
	 */
	@SuppressWarnings("deprecation")
	public Crafting(RedstoneSwords redstoneSwords){
		this.redstoneSwords = redstoneSwords;
		
		// crafting the redstone sword
		ItemStack rsword = Values.getInitSword();
		ShapedRecipe rswordRecipe = new ShapedRecipe(rsword);
		rswordRecipe.shape("r","r","s");
		rswordRecipe.setIngredient('r', Material.REDSTONE);
		rswordRecipe.setIngredient('s', Material.STICK);
		redstoneSwords.getServer().addRecipe(rswordRecipe);
		
		// upgrading the redstone sword
		ItemStack rswordUpgraded = new ItemStack(Values.swordMaterial);
		rswordUpgrade = new ShapedRecipe(rswordUpgraded);
		rswordUpgradeBlocks = new ShapedRecipe(rswordUpgraded);
		rswordUpgrade.shape("rrr","rwr","rrr");
		rswordUpgrade.setIngredient('r', Material.REDSTONE);
		rswordUpgrade.setIngredient('w', Values.swordMaterial, -1);
		rswordUpgradeBlocks.shape("rrr","rwr","rrr");
		rswordUpgradeBlocks.setIngredient('r', Material.REDSTONE_BLOCK);
		rswordUpgradeBlocks.setIngredient('w', Values.swordMaterial, -1);
		
		// add all recipes
		redstoneSwords.getServer().addRecipe(rswordUpgrade);
		redstoneSwords.getServer().addRecipe(rswordUpgradeBlocks);
	}
	
	/**
	 * set the right result on update crafting
	 * 
	 * @param e
	 * 		PrepareItemCraftEvent
	 */
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent e){
		if(!(Toolbox.recipesEqual(e.getRecipe(), rswordUpgrade) 
				|| Toolbox.recipesEqual(e.getRecipe(), rswordUpgradeBlocks))) return;
		
		ItemStack output = null;
		ItemStack[] inventory = e.getInventory().getContents();
		for(ItemStack item : inventory){
			if(Toolbox.isRedstoneSword(item)){
				output = item.clone();
				break;
			}
		}
		if(output != null){
			int upgradeValue = 8; // using redstoneDust
			if(e.getInventory().getItem(1).getType() == Material.REDSTONE_BLOCK) 
				upgradeValue = 72; // using redstoneBlocks
			
			Toolbox.increaseLore(output, Values.redstoneLore, upgradeValue);
		}
		e.getInventory().setResult(output);
	}
	
}
