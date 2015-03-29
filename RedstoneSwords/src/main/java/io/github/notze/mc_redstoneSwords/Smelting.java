package io.github.notze.mc_redstoneSwords;

import org.bukkit.Material;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;

public class Smelting{
	
	// reference to main class
	RedstoneSwords redstoneSwords;
	
	public Smelting(RedstoneSwords redstoneSwords){
		this.redstoneSwords = redstoneSwords;
		
		FurnaceRecipe rottenFleshToLeather = new FurnaceRecipe(
				new ItemStack(Material.LEATHER),
				Material.ROTTEN_FLESH);
		
		redstoneSwords.getServer().addRecipe(rottenFleshToLeather);
	}

}
