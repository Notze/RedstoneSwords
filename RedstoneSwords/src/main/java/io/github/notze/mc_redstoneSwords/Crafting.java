package io.github.notze.mc_redstoneSwords;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Crafting implements Listener {
	
	// reference to main class
	RedstoneSwords redstoneSwords;
	
	ItemStack rswordUpgraded = new ItemStack(Values.swordMaterial);
	
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
		ShapedRecipe rswordUpgrade = new ShapedRecipe(rswordUpgraded);
		ShapedRecipe rswordUpgradeBlocks = new ShapedRecipe(rswordUpgraded);
		rswordUpgrade.shape("rrr","rwr","rrr");
		rswordUpgrade.setIngredient('r', Material.REDSTONE);
		rswordUpgrade.setIngredient('w', Material.WOOD_SWORD, -1);
		rswordUpgradeBlocks.shape("rrr","rwr","rrr");
		rswordUpgradeBlocks.setIngredient('r', Material.REDSTONE_BLOCK);
		rswordUpgradeBlocks.setIngredient('w', Material.WOOD_SWORD, -1);
		
		// add all recipes
		redstoneSwords.getServer().addRecipe(rswordUpgrade);
		redstoneSwords.getServer().addRecipe(rswordUpgradeBlocks);
		redstoneSwords.getLogger().info("Recipes enabled!");
	}
	
	/**
	 * adds a right-click effect to the redstone sword
	 * 
	 * @param e
	 * 		PlayerInteractEvent
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		redstoneSwords.getLogger().info("onPlayerInteract envoced!");
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR))) return;
		redstoneSwords.getLogger().info("looks like right click air");
		ItemStack handItem = e.getPlayer().getItemInHand();
		if(isRedstoneSword(handItem)){
		
			int rest = increaseLore(handItem, Values.redstoneLore, -Values.redstoneFactor);
			if(rest != -1){
				redstoneSwords.getLogger().info("Effect applied!");
				e.getPlayer().addPotionEffect(new PotionEffect(
						PotionEffectType.SPEED, 
						Values.speedBoostTime*10, 
						Values.speedBoost*10));
			}else{
				e.getPlayer().sendMessage("Not enough redstone!");
			}
		
		}
	}
	
	/**
	 * let the redstone sword consume experience
	 * 
	 * @param e
	 * 		PlayerExpChangeEvent
	 */
	@EventHandler
	public void onPlayerExpChange(PlayerExpChangeEvent e){
		
		// check if player is holding the redstone sword
		ItemStack handItem = e.getPlayer().getItemInHand();
		if(isRedstoneSword(handItem)){
			int value = increaseLore(handItem, Values.expLore, e.getAmount());
			e.setAmount(0); // no xp for the player
			
			handItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, Math.floorDiv(value,Values.expFactor));
		}
	}
	
	/**
	 * give back experience when the sword breaks
	 * 
	 * @param e
	 * 		PlayerItemBreakEvent
	 */
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent e){
		ItemStack handItem = e.getPlayer().getItemInHand();
		if(!(isRedstoneSword(handItem))) return;
			
		int exp = increaseLore(handItem, Values.expLore, 0);
		if(Values.reclaimExpFactor == 0){
			exp = 0;
		}else{
			exp /= Values.reclaimExpFactor;
		}
		redstoneSwords.getLogger().info("Stored XP: " + exp);
		
		e.getPlayer().giveExp(exp);
	}
	
	/**
	 * set the right result on update crafting
	 * 
	 * @param e
	 * 		PrepareItemCraftEvent
	 */
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent e){
		
		if(e.getInventory().getResult().equals(rswordUpgraded)){
			ItemStack output = null;
			ItemStack[] inventory = e.getInventory().getContents();
			for(ItemStack item : inventory){
				if(isRedstoneSword(item)){
					output = item.clone();
					break;
				}
			}
			if(output != null){
				int upgradeValue = 8; // using redstoneDust
				if(e.getInventory().getItem(1).getType() == Material.REDSTONE_BLOCK) 
					upgradeValue = 72; // using redstoneBlocks
				
				increaseLore(output, Values.redstoneLore, upgradeValue);
			}
			e.getInventory().setResult(output);
		}
	}
	
	
	/**
	 * check whether an item is our sword or not.
	 * 
	 * @param sword
	 * 		the item(Stack) to check
	 * @return
	 * 		true if it is the redstone sword
	 */
	private boolean isRedstoneSword(ItemStack sword){
		if(!(sword.hasItemMeta())) return false; // rs has meta
		ItemMeta swordMeta = sword.getItemMeta();
		if(!(swordMeta.hasDisplayName()) // check for right name
				|| !(swordMeta.getDisplayName().equals(Values.swordName))) 
			return false;
		if(!(swordMeta.hasLore())) return false; // check for lore
		List<String> lores = swordMeta.getLore();
		for(int i=0; i<Values.loreLength; i++) // check for right lore
			if(!(lores.get(i).contains(Values.lores.get(i))))
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
	private int increaseLore(ItemStack sword, String someLore, int amount){
		ItemMeta swordMeta = sword.getItemMeta();
		List<String> lores = swordMeta.getLore();
		List<String> newLores = new ArrayList<String>(Values.loreLength);
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
	
}
