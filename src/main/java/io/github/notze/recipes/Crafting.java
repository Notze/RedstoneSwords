package io.github.notze.recipes;

import io.github.notze.redstoneswords.RedstoneSwords;
import io.github.notze.util.Items;
import io.github.notze.util.Utilities;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

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
		
		// scroll of growth
		ItemStack scrollCrop= Items.getScrollCrop();
		redstoneSwords.getServer().addRecipe(
				new ShapelessRecipe(scrollCrop)
				.addIngredient(Material.PAPER)
				.addIngredient(Material.INK_SACK)
				.addIngredient(Material.FEATHER)
				.addIngredient(Material.WHEAT));
		
		// scroll of fireball
		ItemStack scrollFireball = Items.getScrollFireball();
		redstoneSwords.getServer().addRecipe(
				new ShapelessRecipe(scrollFireball)
				.addIngredient(Material.PAPER)
				.addIngredient(Material.INK_SACK)
				.addIngredient(Material.FEATHER)
				.addIngredient(Material.SULPHUR));
		
		// crafting the redstone sword
		ItemStack rsword = Items.getInitSword();
		ShapedRecipe rswordRecipe = new ShapedRecipe(rsword);
		rswordRecipe.shape("r","r","s");
		rswordRecipe.setIngredient('r', Material.REDSTONE);
		rswordRecipe.setIngredient('s', Material.STICK);
		redstoneSwords.getServer().addRecipe(rswordRecipe);
		
		// upgrading the redstone sword
		ItemStack rswordUpgraded = new ItemStack(Items.swordMaterial);
		rswordUpgrade = new ShapedRecipe(rswordUpgraded);
		rswordUpgradeBlocks = new ShapedRecipe(rswordUpgraded);
		rswordUpgrade.shape("rrr","rwr","rrr");
		rswordUpgrade.setIngredient('r', Material.REDSTONE);
		rswordUpgrade.setIngredient('w', Items.swordMaterial, -1);
		rswordUpgradeBlocks.shape("rrr","rwr","rrr");
		rswordUpgradeBlocks.setIngredient('r', Material.REDSTONE_BLOCK);
		rswordUpgradeBlocks.setIngredient('w', Items.swordMaterial, -1);
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
		if(!(Utilities.recipesEqual(e.getRecipe(), rswordUpgrade) 
				|| Utilities.recipesEqual(e.getRecipe(), rswordUpgradeBlocks))) return;
		
		ItemStack output = null;
		ItemStack[] inventory = e.getInventory().getContents();
		for(ItemStack item : inventory){
			if(Utilities.isRedstoneSword(item)){
				output = item.clone();
				break;
			}
		}
		if(output != null){
			int upgradeValue = 8; // using redstoneDust
			if(e.getInventory().getItem(1).getType() == Material.REDSTONE_BLOCK) 
				upgradeValue = 72; // using redstoneBlocks
			
			Utilities.increaseLore(output, Items.redstoneLore, upgradeValue);
		}
		e.getInventory().setResult(output);
	}
	
}
