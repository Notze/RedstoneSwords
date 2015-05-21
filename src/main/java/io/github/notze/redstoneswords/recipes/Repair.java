package io.github.notze.redstoneswords.recipes;

import io.github.notze.redstoneswords.RedstoneSwords;
import io.github.notze.redstoneswords.util.Utilities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;

public class Repair implements Listener{
	
	RedstoneSwords redstoneSwords;
	
	// map of all repairable tools and their needed material
	@SuppressWarnings("serial")
	Map<Material,Material> repairMap = new HashMap<Material,Material>(){{
		put(Material.FISHING_ROD, Material.STRING);
		put(Material.BOW, Material.STRING);
		put(Material.WOOD_SWORD, Material.LOG);
		put(Material.WOOD_PICKAXE, Material.LOG);
		put(Material.WOOD_SPADE, Material.LOG);
		put(Material.WOOD_AXE, Material.LOG);
		put(Material.WOOD_HOE, Material.LOG);
		put(Material.STONE_SWORD, Material.COBBLESTONE);
		put(Material.STONE_PICKAXE, Material.COBBLESTONE);
		put(Material.STONE_SPADE, Material.COBBLESTONE);
		put(Material.STONE_AXE, Material.COBBLESTONE);
		put(Material.STONE_HOE, Material.COBBLESTONE);
		put(Material.LEATHER_HELMET, Material.LEATHER);
		put(Material.LEATHER_CHESTPLATE, Material.LEATHER);
		put(Material.LEATHER_LEGGINGS, Material.LEATHER);
		put(Material.LEATHER_BOOTS, Material.LEATHER);
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
		put(Material.DIAMOND_SWORD, Material.DIAMOND);
		put(Material.DIAMOND_PICKAXE, Material.DIAMOND);
		put(Material.DIAMOND_SPADE, Material.DIAMOND);
		put(Material.DIAMOND_AXE, Material.DIAMOND);
		put(Material.DIAMOND_HOE, Material.DIAMOND);
		put(Material.DIAMOND_HELMET, Material.DIAMOND);
		put(Material.DIAMOND_CHESTPLATE, Material.DIAMOND);
		put(Material.DIAMOND_LEGGINGS, Material.DIAMOND);
		put(Material.DIAMOND_BOOTS, Material.DIAMOND);
	}};
	
	
	/**
	 * adds repair recipes
	 * 
	 * @param redstoneSwords
	 * 		instance of main class
	 */
	@SuppressWarnings("deprecation")
	public Repair(RedstoneSwords redstoneSwords){
		this.redstoneSwords = redstoneSwords;
				
		// add all recipes from repairMap
		Iterator<Entry<Material, Material>> it = repairMap.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<Material,Material> repairItem = (Entry<Material, Material>) it.next();
			
			redstoneSwords.getServer().addRecipe(
					new ShapelessRecipe(new ItemStack(repairItem.getKey()))
					.addIngredient(repairItem.getKey(), -1)
					.addIngredient(repairItem.getValue(), -1)
			);
		}
	}
	
	/**
	 * keeps enchantments on repair
	 * 
	 * @param e
	 * 		PrepareItemCraftEvent
	 */
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent e){
		
		Material resultType = e.getInventory().getResult().getType();
		if(repairMap.containsKey(resultType)){
			
			ItemStack output = null;
			ItemStack[] inventory = e.getInventory().getContents();
			
			for(int slot=1; slot<inventory.length; slot++){ // start at slot 1, slot 0 ist the output
				if(RedstoneSwords.keepEnchantmentsOnRepair){
					if(inventory[slot].getType() == resultType){
						output = inventory[slot].clone();
						break;
					}
				}else{
					if(Utilities.isRedstoneSword(inventory[slot])){
						output = inventory[slot].clone();
						break;
					}
				}
			}
			if(output != null){
				output.setDurability((short) 0);
				e.getInventory().setResult(output);
			}
		}
	}

}
