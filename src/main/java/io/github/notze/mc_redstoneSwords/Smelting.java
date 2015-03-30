package io.github.notze.mc_redstoneSwords;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class Smelting{
	
	// reference to main class
	RedstoneSwords redstoneSwords;
	
	@SuppressWarnings("serial")
	Map<Material,Material> smeltMap = new HashMap<Material,Material>(){{
		put(Material.IRON_SWORD, Material.IRON_INGOT);
		put(Material.IRON_PICKAXE, Material.IRON_INGOT);
		put(Material.IRON_SPADE, Material.IRON_INGOT);
		put(Material.IRON_AXE, Material.IRON_INGOT);
		put(Material.IRON_HOE, Material.IRON_INGOT);
		put(Material.IRON_HELMET, Material.IRON_INGOT);
		put(Material.IRON_CHESTPLATE, Material.IRON_INGOT);
		put(Material.IRON_LEGGINGS, Material.IRON_INGOT);
		put(Material.IRON_BOOTS, Material.IRON_INGOT);
		put(Material.GOLD_SWORD, Material.GOLD_INGOT);
		put(Material.GOLD_PICKAXE, Material.GOLD_INGOT);
		put(Material.GOLD_SPADE, Material.GOLD_INGOT);
		put(Material.GOLD_AXE, Material.GOLD_INGOT);
		put(Material.GOLD_HOE, Material.GOLD_INGOT);
		put(Material.GOLD_HELMET, Material.GOLD_INGOT);
		put(Material.GOLD_CHESTPLATE, Material.GOLD_INGOT);
		put(Material.GOLD_LEGGINGS, Material.GOLD_INGOT);
		put(Material.GOLD_BOOTS, Material.GOLD_INGOT);
	}};
	
	
	public Smelting(RedstoneSwords redstoneSwords){
		this.redstoneSwords = redstoneSwords;
		
		// add all recipes from smeltMap
		Iterator<Entry<Material, Material>> it = smeltMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Material,Material> smeltItem = (Entry<Material, Material>) it.next();
			
			redstoneSwords.getServer().addRecipe(
				new FurnaceRecipe(
						new ItemStack(smeltItem.getValue()),
						smeltItem.getKey())
			);
		}
		
		// rotten flesh to leather
		FurnaceRecipe rottenFleshToLeather = new FurnaceRecipe(
				new ItemStack(Material.LEATHER),
				Material.ROTTEN_FLESH);
		redstoneSwords.getServer().addRecipe(rottenFleshToLeather);
	}

}
