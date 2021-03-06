package io.github.notze.redstoneswords.util;

import io.github.notze.redstoneswords.RedstoneSwords;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class Utilities {

	/**
	 * remove redstone sword and give back xp
	 * 
	 * @param player
	 * 		Player
	 * @param handItem
	 * 		item the player holds
	 */
	public static void destroyRedstoneSword(Player player, ItemStack handItem){
		if(!(Utilities.isRedstoneSword(handItem))) return;
		
		int exp = Utilities.increaseLore(handItem, Items.expLore, 0);
		if(RedstoneSwords.reclaimExpFactor == 0){
			exp = 0;
		}else{
			exp /= RedstoneSwords.reclaimExpFactor;
		}
		
		player.giveExp(exp);
		player.getInventory().remove(handItem);
	}
	
	/**
	 * Checks if the given item is a bound item.
	 * 
	 * @param item
	 * 		ItemStack to check
	 * @return
	 * 		true if it is a bound item, false else
	 */
	public static boolean isBoundItem(ItemStack item){
		if(item == null) return false;
		if(!(item.hasItemMeta())) return false;
		ItemMeta im = item.getItemMeta();
		if(!(im.hasLore())) return false;
		for(ItemStack boundItem : Items.boundItems){
			ItemMeta bim = boundItem.getItemMeta();
			if(bim.getDisplayName().equals(im.getDisplayName())
					&& bim.getLore().equals(im.getLore()))
				return true;
		}
		return false;
	}
	
	/**
	 * Checks if the given bound items are the same.
	 * 
	 * @param i1
	 * 		ItemStack to check
	 * @param i2
	 * 		second ItemStack to check
	 * @return
	 * 		true if it bound items of same type, false otherwise
	 */
	public static boolean boundItemsEqual(ItemStack i1, ItemStack i2){
		if(i1==null || i2==null) return false;
		if(!(i1.hasItemMeta() && i2.hasItemMeta())) return false;
		ItemMeta im1 = i1.getItemMeta();
		ItemMeta im2 = i2.getItemMeta();
		if(!(im1.hasLore() && im2.hasLore())) return false;
		if(im1.getDisplayName().equals(im2.getDisplayName())
				&& im1.getLore().equals(im2.getLore()))
			return true;
		return false;
	}
	
	public static boolean scrollsEqual(ItemStack scroll1, ItemStack scroll2){
		if(!(scroll1.hasItemMeta() && scroll2.hasItemMeta())) return false;
		if(!(scroll1.getType().equals(scroll2.getType()))) return false;
		if(!((scroll1.getItemMeta().hasDisplayName()
				&& scroll2.getItemMeta().hasDisplayName())))
			return false;
		if(!(scroll1.getItemMeta().getDisplayName()
				.equals(scroll2.getItemMeta().getDisplayName())))
			return false;
		
		return true;
	}
	
	/**
	 * check whether an item is our sword or not.
	 * 
	 * @param sword
	 * 		the item(Stack) to check
	 * @return
	 * 		true if it is the redstone sword
	 */
	public static boolean isRedstoneSword(ItemStack sword){
		if(!(sword.hasItemMeta())) return false; // rs has meta
		ItemMeta swordMeta = sword.getItemMeta();
		if(!(swordMeta.hasDisplayName()) // check for right name
				|| !(swordMeta.getDisplayName().equals(Items.swordName))) 
			return false;
		if(!(swordMeta.hasLore())) return false; // check for lore
		List<String> lores = swordMeta.getLore();
		for(int i=0; i<Items.loreLength; i++) // check for right lore
			if(!(lores.get(i).contains(Items.lores.get(i))))
				return false;
		
		return true;
	}
	
	/**
	 * increase a counter in the lore
	 * 
	 * @param sword
	 * 		the redstone sword
	 * @param someLore
	 * 		a lore, only use lores from Values.class
	 * @param amount
	 * 		amount to increase
	 * @return
	 * 		the increased value
	 */
	public static int increaseLore(ItemStack sword, String someLore, int amount){
		ItemMeta swordMeta = sword.getItemMeta();
		List<String> lores = swordMeta.getLore();
		List<String> newLores = new ArrayList<String>(Items.loreLength);
		int newValue = 0;
		
		for(String lore : lores){
			if(lore.contains(someLore)){
				int old = Integer.parseInt(lore.substring(someLore.length()));
				newValue = old + amount;
				if(newValue < 0) return -1;
				newLores.add(someLore + newValue);
			}else{
				newLores.add(lore);
			}
		}
		
		swordMeta.setLore(newLores);
		sword.setItemMeta(swordMeta);
		return newValue;
	}
	
	/**
	 * set a counter in the lore
	 * 
	 * @param sword
	 * 		the redstone sword
	 * @param someLore
	 * 		a lore, only use lores from Values.class
	 * @param amount
	 * 		amount to increase
	 * @return 
	 * @return
	 * 		the increased value
	 */
	public static void setLore(ItemStack sword, String someLore, int amount){
		ItemMeta swordMeta = sword.getItemMeta();
		List<String> lores = swordMeta.getLore();
		List<String> newLores = new ArrayList<String>(Items.loreLength);
		int newValue = amount;
		
		for(String lore : lores){
			if(lore.contains(someLore)){
				newLores.add(someLore + newValue);
			}else{
				newLores.add(lore);
			}
		}
		
		swordMeta.setLore(newLores);
		sword.setItemMeta(swordMeta);
	}
	
	/**
	 * return the value of a lore as string (Mode: Nothing -> Nothing)
	 * 
	 * @param sword
	 * 		the redstone sword
	 * @param someLore
	 * 		a lore, only use lores from Values.class
	 * @return
	 * 		the value as string
	 */
	public static String getLoreValueAsString(ItemStack sword, String someLore){
		ItemMeta swordMeta = sword.getItemMeta();
		List<String> lores = swordMeta.getLore();
		
		for(String lore : lores)
			if(lore.contains(someLore))
				return lore.substring(someLore.length());
				
		return null;
	}
	
	/**
	 * checks whether two recipes are the same or not
	 * 
	 * @param r1
	 * 		first recipe
	 * @param r2
	 * 		second recipe
	 * @return
	 * 		true if recipes are equal
	 */
	public static boolean recipesEqual(Recipe recipe1, Recipe recipe2){
		// only implemented for shaped recipes
		if(!(recipe1 instanceof ShapedRecipe)) return false;
		if(!(recipe2 instanceof ShapedRecipe)) return false;
		ShapedRecipe r1 = (ShapedRecipe) recipe1;
		ShapedRecipe r2 = (ShapedRecipe) recipe2;
		
		// true if same instance
		if(r1 == r2) return true;
		
		// false if only one is null
		if(r1 == null || r2 == null) return false;
		
		// false if results don't match
		if(!(r1.getResult().equals(r2.getResult()))) return false;
		
		return match(r1, r2);
	}
	
	/**
	 * match the ingredients of two recipes
	 * 
	 * @param r1
	 * 		first recipe
	 * @param r2
	 * 		second recipe
	 * @return
	 * 		true if ingredients are equal
	 */
	private static boolean match(ShapedRecipe r1, ShapedRecipe r2){
		ItemStack[] ingredients1 = recipeToArray(r1);
		ItemStack[] ingredients2 = recipeToArray(r2);
		
		// false if ingredient count don't match
		if(ingredients1.length != ingredients2.length) return false;
		
		// true if all ingredients match
		for(int i=0; i<ingredients1.length; i++)
			if(!(ingredients1[i].equals(ingredients2[i])))
				return false;
		
		return true;
	}
	
	/**
	 * converts a recipe to an array of its ingredients
	 * @param recipe
	 * 		the recipe
	 * @return
	 * 		an array of ingredients
	 */
	private static ItemStack[] recipeToArray(ShapedRecipe recipe){
		String[] shape = recipe.getShape();
		Map<Character,ItemStack> map = recipe.getIngredientMap();
		ItemStack[] recipeArray = new ItemStack[9];
		int slot = 0;
		
		for(String s : shape){
			for(char c : s.toCharArray()){
				recipeArray[slot] = map.get(c);
				slot++;
			}
		}
		
		return recipeArray;
	}
	
}
