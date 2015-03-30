package io.github.notze.redstoneswords;

import io.github.notze.util.Items;
import io.github.notze.util.Utilities;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Events implements Listener{

	// reference to main class
	RedstoneSwords redstoneSwords;
	
	/**
	 * nothing really
	 * 
	 * @param redstoneSwords
	 * 		instance of main class
	 */
	public Events(RedstoneSwords redstoneSwords) {
		this.redstoneSwords = redstoneSwords;
	}
	
	/**
	 * adds a right-click effect to the redstone sword
	 * 
	 * @param e
	 * 		PlayerInteractEvent
	 */
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR))) return;
		Player player = e.getPlayer();
		ItemStack handItem = player.getItemInHand();
		
		// sword ability
		if(Utilities.isRedstoneSword(handItem)){
		
			int rest = Utilities.increaseLore(handItem, Items.redstoneLore, -RedstoneSwords.redstoneFactor);
			if(rest != -1){
				player.addPotionEffect(new PotionEffect(
						PotionEffectType.SPEED, 
						RedstoneSwords.speedBoostTime*10, 
						RedstoneSwords.speedBoost*10));
			}else{
				player.sendMessage("Not enough redstone!");
			}
		
		}
		
		// scroll of fireball
		if(Utilities.scrollsEqual(handItem, Items.getScrollFireball())){
			
			Vector offset = player.getLocation().getDirection().multiply(2);
			Location spawnLoc = player.getEyeLocation().add(offset);
			player.getWorld().spawn(spawnLoc, Fireball.class);
			
			decreaseStack(player, handItem);
		}
		
		// scroll of growth
		if(Utilities.scrollsEqual(handItem, Items.getScrollCrop())){
			
			Location loc = player.getLocation();
			
			int r = RedstoneSwords.growthRadius;
			int h = 1;

			int x1 = loc.getBlockX()-r;
			int y1 = loc.getBlockY()-h;
			int z1 = loc.getBlockZ()-r;

			int x2 = loc.getBlockX()+r;
			int y2 = loc.getBlockY()+h;
			int z2 = loc.getBlockZ()+r;
			
			for(int i=x1; i<=x2; i++){
				for(int j=y1; j<=y2; j++){
					for(int k=z1; k<=z2; k++){
			
						Block b = player.getWorld().getBlockAt(i, j, k);
						if(b.getTypeId() == 59){ // id of wheat
							b.setTypeIdAndData(59, (byte) 7, false);
						}else if(b.getTypeId() == 141){ // id of carrots
							b.setTypeIdAndData(141, (byte) 7, false);
						}else if(b.getTypeId() == 142){ // id of potatoes
							b.setTypeIdAndData(142, (byte) 7, false);
						}
						
					}
				}
			}		
			
			
			decreaseStack(player, handItem);
		}
		
		
		
		
	}
	
	private void decreaseStack(Player player, ItemStack handItem){
		int newAmount = handItem.getAmount()-1;
		if(newAmount == 0){
			player.getInventory().remove(handItem);
		}else{
			handItem.setAmount(handItem.getAmount()-1);
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
		if(Utilities.isRedstoneSword(handItem)){
			int value = Utilities.increaseLore(handItem, Items.expLore, e.getAmount());
			e.setAmount(0); // no xp for the player
			
			handItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, Math.floorDiv(value,RedstoneSwords.expFactor));
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
		Player player = e.getPlayer();
		ItemStack handItem = player.getItemInHand();
		
		Utilities.destroyRedstoneSword(player, handItem);
	}
	
}
