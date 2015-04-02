package io.github.notze.recipes;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;
import io.github.notze.redstoneswords.RedstoneSwords;
import io.github.notze.util.Items;
import io.github.notze.util.Utilities;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class Crafting implements Listener {
	
	// reference to main class
	RedstoneSwords redstoneSwords;
	
	ShapedRecipe rswordUpgrade;
	ShapedRecipe rswordUpgradeBlocks;
	ShapedRecipe rswordUpgradeEnder;
	
	/**
	 * Adds new recipes
	 * 
	 * @param redstoneSwords
	 * 		reference to main class
	 */
	@SuppressWarnings({ "deprecation", "serial" })
	public Crafting(RedstoneSwords redstoneSwords){
		this.redstoneSwords = redstoneSwords;
		
		List<Pair<String,Material>> scrolls = new ArrayList<Pair<String,Material>>(){{
			add(new Pair<String,Material>(Items.scrollFireballName, Material.SULPHUR));
			add(new Pair<String,Material>(Items.scrollCropName, Material.WHEAT));
			add(new Pair<String,Material>(Items.scrollLevitationName, Material.FEATHER));
			add(new Pair<String,Material>(Items.scrollJumpName, Material.SLIME_BALL));
			add(new Pair<String,Material>(Items.scrollRespirationName, Material.RAW_FISH));
			add(new Pair<String,Material>(Items.scrollRebirthName, Material.EGG));
			add(new Pair<String,Material>(Items.scrollHealName, Material.APPLE));
		}};
		
		for(Pair<String,Material> scroll : scrolls){
			ItemStack scrollItem = Items.getScroll(scroll.getKey());
			redstoneSwords.getServer().addRecipe(
					new ShapelessRecipe(scrollItem)
					.addIngredient(Material.PAPER)
					.addIngredient(Material.INK_SACK)
					.addIngredient(Material.FEATHER)
					.addIngredient(scroll.getValue()));
		}
		
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
		rswordUpgrade.shape("rrr","rwr","rrr");
		rswordUpgrade.setIngredient('r', Material.REDSTONE);
		rswordUpgrade.setIngredient('w', Items.swordMaterial, -1);
		redstoneSwords.getServer().addRecipe(rswordUpgrade);
		
		rswordUpgradeBlocks = new ShapedRecipe(rswordUpgraded);
		rswordUpgradeBlocks.shape("rrr","rwr","rrr");
		rswordUpgradeBlocks.setIngredient('r', Material.REDSTONE_BLOCK);
		rswordUpgradeBlocks.setIngredient('w', Items.swordMaterial, -1);
		redstoneSwords.getServer().addRecipe(rswordUpgradeBlocks);
		
		rswordUpgradeEnder = new ShapedRecipe(rswordUpgraded);
		rswordUpgradeEnder.shape("rrr","rwr","rrr");
		rswordUpgradeEnder.setIngredient('r', Material.ENDER_PEARL);
		rswordUpgradeEnder.setIngredient('w', Items.swordMaterial, -1);
		redstoneSwords.getServer().addRecipe(rswordUpgradeEnder);
		
	}
	
	/**
	 * set the right result on update crafting
	 * 
	 * @param e
	 * 		PrepareItemCraftEvent
	 */
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent e){
		if(Utilities.recipesEqual(e.getRecipe(), rswordUpgrade)){
			increaseValueOnOutput(e.getInventory(), Items.redstoneLore, 8);
		}else if(Utilities.recipesEqual(e.getRecipe(), rswordUpgradeBlocks)){
			increaseValueOnOutput(e.getInventory(), Items.redstoneLore, 72);
		}else if(Utilities.recipesEqual(e.getRecipe(), rswordUpgradeEnder)){
			increaseValueOnOutput(e.getInventory(), Items.enderLore, 8);
		}
	}
	
	private void increaseValueOnOutput(CraftingInventory inv, String lore, int amount){
		ItemStack output = null;
		ItemStack[] inventory = inv.getContents();
		for(ItemStack item : inventory){
			if(Utilities.isRedstoneSword(item)){
				output = item.clone();
				break;
			}
		}
			
		Utilities.increaseLore(output, lore, amount);
		inv.setResult(output);
	}
	
}
