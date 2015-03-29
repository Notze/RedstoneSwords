package io.github.notze.mc_redstoneSwords;

import org.bukkit.enchantments.Enchantment;
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
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		if(!(e.getAction().equals(Action.RIGHT_CLICK_AIR))) return;
		ItemStack handItem = e.getPlayer().getItemInHand();
		if(Toolbox.isRedstoneSword(handItem)){
		
			int rest = Toolbox.increaseLore(handItem, Values.redstoneLore, -RedstoneSwords.redstoneFactor);
			if(rest != -1){
				e.getPlayer().addPotionEffect(new PotionEffect(
						PotionEffectType.SPEED, 
						RedstoneSwords.speedBoostTime*10, 
						RedstoneSwords.speedBoost*10));
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
		if(Toolbox.isRedstoneSword(handItem)){
			int value = Toolbox.increaseLore(handItem, Values.expLore, e.getAmount());
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
		
		Toolbox.destroyRedstoneSword(player, handItem);
	}
	
}
