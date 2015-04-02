package io.github.notze.redstoneswords;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.util.Pair;
import io.github.notze.util.Items;
import io.github.notze.util.Utilities;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Events implements Listener{

	// reference to main class
	RedstoneSwords redstoneSwords;
	
	// scroll timers and variables
	int flightTimer;
	Boolean oldStat;
	
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
	@SuppressWarnings({ "deprecation", "serial" })
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		final Player player = e.getPlayer();
		ItemStack handItem = player.getItemInHand();
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			if(Utilities.isRedstoneSword(handItem)){
				
				Block b = e.getClickedBlock();
				if(b.getType().equals(Material.REDSTONE_ORE) || b.getType().equals(Material.GLOWING_REDSTONE_ORE)){
					b.setType(Material.AIR);
					Utilities.increaseLore(handItem, Items.redstoneLore, RedstoneSwords.redstoneOreAmount);
					e.setCancelled(true); // normally redstone changes to glowing redstone after right click
				}
				
			}
		}// end of right click block
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
			
			// sword ability
			if(Utilities.isRedstoneSword(handItem)){
				String mode = Utilities.getLoreValueAsString(handItem, Items.modeLore);
				
				// change mode
				if(player.isSneaking()){
					
					ItemMeta im = handItem.getItemMeta();
					String nextMode = null;
					List<String> lores = im.getLore();
					List<String> newLores = new ArrayList<String>(Items.loreLength);
					
					// find next mode
					Iterator<String> it = Items.modes.iterator();
					while(it.hasNext())
						if(it.next().equals(mode) && it.hasNext())
							nextMode = it.next();
					if(nextMode == null) nextMode = Items.modes.get(0);
					
					// set next mode
					for(String lore : lores){
						if(lore.contains(Items.modeLore)){
							newLores.add(Items.modeLore + nextMode);
						}else{
							newLores.add(lore);
						}
					}
					im.setLore(newLores);
					handItem.setItemMeta(im);
					player.sendMessage("[Mode] " + nextMode);
					
				}else if(mode.equals(Items.teleportModeLore)){
					// teleport
					Block b = player.getTargetBlock((Set<Material>) null, 200);
					if(b.getType().equals(Material.AIR)) return;
					Location loc = b.getLocation();
					Vector offset = new Vector(0.5,1,0.5);
					Location target = loc.clone().add(offset);
					
					// make shure that there is enough space for the player
					for(int i=1; i<=2; i++)
						if(loc.add(new Vector(0,1,0)).getBlock().getType() != Material.AIR)
							return;
					
					int rest = Utilities.increaseLore(handItem, Items.enderLore, -RedstoneSwords.teleportCost);
					if(rest != -1){
						player.teleport(target.setDirection(player.getLocation().getDirection()));
					}else{
						player.sendMessage("Not enough enderpearls!");
					}
				}else if(mode.equals(Items.boostModeLore)){
					// boost
					int rest = Utilities.increaseLore(handItem, Items.redstoneLore, -RedstoneSwords.boostCost);
					if(rest != -1){
						player.addPotionEffect(new PotionEffect(
								PotionEffectType.SPEED, 
								RedstoneSwords.speedBoostTime*10, 
								RedstoneSwords.speedBoost*10));
					}else{
						player.sendMessage("Not enough redstone!");
					}
				}
			}
			
		}// end of right click air
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				|| e.getAction().equals(Action.RIGHT_CLICK_AIR)){
			
			// scroll of respiration
			if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollRespirationName))){
				
				player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, RedstoneSwords.respirationTime*20, 1));
				decreaseStack(player, handItem);
			}
			
			// scroll of rebirth
			if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollRebirthName))){
				
				//TODO functionallity
				
				decreaseStack(player, handItem);
			}
			
			// scroll of jumping
			if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollJumpName))){
				
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, RedstoneSwords.jumpTime*20, 5));
				decreaseStack(player, handItem);
			}
			
			// scroll of healing
			if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollHealName))){
				
				player.setHealth(player.getMaxHealth());				
				decreaseStack(player, handItem);
			}
			
			// scroll of levitation
			if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollLevitationName))){
				
				flightTimer = RedstoneSwords.flightTime;
				oldStat = player.getAllowFlight();
				player.setAllowFlight(true);
				decreaseStack(player, handItem);
				
				new BukkitRunnable(){
					public void run(){
						
						if(flightTimer==0){
							player.setFlying(false);
							player.setAllowFlight(oldStat);
							cancel();
						}else{
							player.setFlying(true);
							player.sendMessage("Levitation wears off in " + flightTimer + " seconds.");
							flightTimer--;
						}
					}
				}.runTaskTimer(redstoneSwords, 0, 20);
			}
			
			// scroll of fireball
			if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollFireballName))){
				
				Vector offset = player.getLocation().getDirection().multiply(2);
				Location spawnLoc = player.getEyeLocation().add(offset);
				player.getWorld().spawn(spawnLoc, Fireball.class);
				player.getWorld().playEffect(spawnLoc, Effect.BLAZE_SHOOT, 1);
				
				decreaseStack(player, handItem);
			}
			
			// scroll of growth
			if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollCropName))){
				
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
			
			// franciska
			List<Pair<Material,Integer>> mats = new ArrayList<Pair<Material,Integer>>(){{
				add(new Pair<Material,Integer>(Material.WOOD_AXE, RedstoneSwords.woodAxeDmg));
				add(new Pair<Material,Integer>(Material.IRON_AXE, RedstoneSwords.stoneAxeDmg));
				add(new Pair<Material,Integer>(Material.GOLD_AXE, RedstoneSwords.goldAxeDmg));
				add(new Pair<Material,Integer>(Material.DIAMOND_AXE, RedstoneSwords.diamondAxeDmg));
			}};
			for(final Pair<Material,Integer> mat : mats){
				if(handItem.getType() == mat.getKey()){
					Location loc = player.getEyeLocation();
					Vector direction = loc.getDirection().multiply(1.1D); // get direction and flying speed
					
					final Item drop = player.getWorld().dropItem(loc, handItem);
					drop.setVelocity(direction);
					
					decreaseStack(player, handItem);
					
					new BukkitRunnable(){
						@Override
						public void run(){
							if(drop.isOnGround()) cancel(); // only deal dmg with impact
							for(Entity nearbyEntity : drop.getNearbyEntities(0,0,0)) // only affect exactly hit enemies 
								if(nearbyEntity instanceof LivingEntity)
									if(!(nearbyEntity == player)){ // don't hurt yourself!
										LivingEntity nearbyLivingEntity = (LivingEntity) nearbyEntity;
										nearbyLivingEntity.damage(mat.getValue(), player);
										cancel(); // exit after one entity got hit
									}
						}
					}.runTaskTimer(redstoneSwords, 0, 1); // start immediately since you can't hurt yourself
				}
			}
			
		}// end of right click block/air
	}
	
	@SuppressWarnings("deprecation")
	private void decreaseStack(Player player, ItemStack handItem){
		int newAmount = handItem.getAmount()-1;
		if(newAmount == 0){
			ItemStack[] inv = player.getInventory().getContents();
			for(int i=0; i<inv.length; i++)
				if(handItem.equals(inv[i])){
					inv[i] = null;
					break;
				}
			player.getInventory().setContents(inv);
		}else{
			handItem.setAmount(handItem.getAmount()-1);
		}
		player.updateInventory();
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
			int value = Utilities.increaseLore(handItem, Items.expLore, 0);
			if(value < e.getPlayer().getTotalExperience()){
				value = Utilities.increaseLore(handItem, Items.expLore, e.getAmount());
				e.setAmount(0); // no xp for the player
				int newLevel = toLevel(value,0);
				Utilities.setLore(handItem, Items.lvlLore, newLevel);
				handItem.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, Math.floorDiv(newLevel,RedstoneSwords.expFactor));
			}
		}
	}
	
	/**
	 * calculate the level by total xp (vanilla formular)
	 * 
	 * @param total
	 * 		total amount of experience
	 * @param level
	 * 		initial 0
	 * @return
	 * 		the level
	 */
	static int toLevel(int total, int level){
        int need;
        if(level <= 15){
            need = 7 + 2*level;
        }else if(level <= 30){
            need = 37 + 5*(level-15);
        }else{
            need = 107 + 9*(level-30);
        }
        if(total<need) return level;
        return toLevel(total-need, level+1);
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
