package io.github.notze.redstoneswords;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.util.Pair;
import io.github.notze.util.Items;
import io.github.notze.util.Particle;
import io.github.notze.util.Utilities;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
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
	int flightTimer, waterWalkTimer, reflectionTimer;
	Boolean oldFlyBool; // levitation
	float oldFlySpeed; // levitation
	Block last; // water walking
	static List<Player> reflection = new ArrayList<Player>(); // reflection
	
	// the player
	Player player = null;
	
	// item player holds
	ItemStack handItem = null;
	
	// scroll of rebirth eggs and entityIDs
	enum spawnEggs {
		Creeper(50, "Creeper"),
		Skeleton(51, "Skeleton"),
		Spider(52, "Spider"),
		Zombie(54, "Zombie"),
		Slime(55, "Slime"),
		Ghast(56, "Ghast"),
		Pigman(57, "Pigman"),
		Enderman(58, "Enderman"),
		CaveSpider(59, "Cave Spider"),
		Silverfish(60, "Silverfish"),
		Blaze(61, "Blaze"),
		MagmaCube(62, "Magma Cube"),
		Bat(65, "Bat"),
		Witch(66, "Witch"),
		Endermite(67, "Endermite"),
		Guardian(68, "Guardian"),
		Pig(90, "Pig"),
		Sheep(91, "Sheep"),
		Cow(92, "Cow"),
		Chicken(93, "Chicken"),
		Squid(94, "Squid"),
		Wolf(95, "Wolf"),
		Mooshroom(96, "Mooshroom"),
		Ocelot(98, "Ocelot"),
		Horse(100, "EntityHorse"),
		Rabbit(101, "Rabbit"),
		Villager(120, "Villager");
		
		private int id;
		private String name;

		private spawnEggs(int id, String name){
			this.id = id;
			this.name = name;
		}
	}
	
	// list of items that can be right-clicked without any ability activations
	@SuppressWarnings("serial")
	List<Material> usables = new ArrayList<Material>(){{
		add(Material.CHEST);
		add(Material.STONE_BUTTON);
		add(Material.WOOD_BUTTON);
		add(Material.LEVER);
		add(Material.ENCHANTMENT_TABLE);
		add(Material.WORKBENCH);
		add(Material.FURNACE);
		add(Material.BURNING_FURNACE);
		add(Material.ANVIL);
		add(Material.ACACIA_DOOR);
		add(Material.BIRCH_DOOR);
		add(Material.DARK_OAK_DOOR);
		add(Material.IRON_DOOR);
		add(Material.JUNGLE_DOOR);
		add(Material.SPRUCE_DOOR);
		add(Material.TRAP_DOOR);
		add(Material.WOOD_DOOR);
		add(Material.WOODEN_DOOR);
		add(Material.REDSTONE_COMPARATOR);
		add(Material.DIODE);
	}};
	
	
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
	 * adds some effects as abilities for items
	 * 
	 * @param e
	 * 		PlayerInteractEvent
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e){
		player = e.getPlayer();
		handItem = player.getItemInHand();
		
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			
			consumeRedstoneOre(e);
			
		}else if(e.getAction().equals(Action.RIGHT_CLICK_AIR)){
			
			
		
		}
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)
				|| e.getAction().equals(Action.RIGHT_CLICK_AIR)){
			
			scrolls(e);
			
			if(isUsableBlock(e.getClickedBlock()))
				return;
				
			franciska(e);
			swordAbilities(e);
		
		}
	}
	
	/**
	 * Adds effects for items
	 * 
	 * @param e
	 * 		PlayerInteractEntityEvent
	 */
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent e){
		player = e.getPlayer();
		handItem = player.getItemInHand();
		
		scrollsOnEntity(e);	
	}
	
	/**
	 * Adds effects for items when a player caused it
	 * 
	 * @param e
	 * 		EntityDamageByEntityEvent
	 */
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent e){
		if(e.getDamager() instanceof Player){
		
			player = (Player) e.getDamager();
			handItem = player.getItemInHand();
			
			torch(e);
			
		}
	}
	
	/**
	 * Adds effects for items when a player is the victim
	 * 
	 * @param e
	 * 		EntityDamageEvent
	 */
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
		
			reflect(e);
			
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
		handItem = e.getPlayer().getItemInHand();
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
	 * give back experience when the sword breaks
	 * 
	 * @param e
	 * 		PlayerItemBreakEvent
	 */
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent e){
		player = e.getPlayer();
		handItem = player.getItemInHand();
		
		Utilities.destroyRedstoneSword(player, handItem);
	}
	
	/**
	 * reflect damage done by arrows
	 * 
	 * @param e
	 * 		PlayerInteractEntityEvent
	 */
	private void reflect(EntityDamageEvent e){
		if(e.getCause().equals(DamageCause.PROJECTILE)){
			EntityDamageByEntityEvent newE = (EntityDamageByEntityEvent) e;
			Entity projectile = newE.getDamager();
			if(projectile instanceof Arrow){
				Arrow arrow = (Arrow) projectile;
				if(arrow.getShooter() instanceof LivingEntity){
					LivingEntity damager = (LivingEntity) arrow.getShooter();
					Player damagedPlayer = (Player) e.getEntity();
					if(reflection.contains(damagedPlayer)){
						damager.damage(e.getDamage());
						e.setCancelled(true);
					}
				}
			}
		}	
	}
	
	/**
	 * Set mobs on fire when hit with a torch
	 * 
	 * @param e
	 * 		PlayerInteractEntityEvent
	 */
	private void torch(EntityDamageByEntityEvent e){
		if(handItem.getType().equals(Material.TORCH)){
			if(e.getEntity() instanceof LivingEntity){
				((LivingEntity) e.getEntity()).setFireTicks(20 * RedstoneSwords.torchFireTime);
			}
		}
	}
	
	/**
	 * Check if the clicked block has a usage
	 * 
	 * @param b
	 * 		Block 
	 * @return
	 * 		true if block has usage e.g. a button
	 */
	private boolean isUsableBlock(Block b){
		if(b == null) return false; // air block causes nullpointerexception in next step
		Material m = b.getType();
		for(Material mat : usables)
			if(mat.equals(m))
				return true;
		
		return false;
	}
	
	@SuppressWarnings("deprecation")
	private void scrollsOnEntity(PlayerInteractEntityEvent e){
		// scroll of rebirth
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollRebirthName)))
			if(e.getRightClicked() instanceof LivingEntity){
				
				LivingEntity mob = (LivingEntity) e.getRightClicked();
				String mobName = mob.getType().getName();
				for(spawnEggs egg : spawnEggs.values())
					if(mobName.equals(egg.name)){
									
						decreaseStack(player, handItem);
						Particle.smoke.apply(player, 0.2, 100, 1);
						mob.remove();
						player.getServer().dispatchCommand(
								player.getServer().getConsoleSender(), 
								"give "
								+ player.getName() 
								+ " spawn_egg 1 " 
								+ egg.id
						);
						break;
					}
			}
		
		// scroll of poison
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollPoisonName)))
			if(e.getRightClicked() instanceof LivingEntity){
				
				LivingEntity mob = (LivingEntity) e.getRightClicked();
				mob.addPotionEffect(new PotionEffect(
						PotionEffectType.POISON, 
						RedstoneSwords.poisonTime*20, 
						1));
				
				decreaseStack(player, handItem);
				Particle.smoke.apply(player, 0.2, 100, 1);
			}
		
		// TODO
		// scroll of command
		
	}
	
	private void consumeRedstoneOre(PlayerInteractEvent e){
		if(Utilities.isRedstoneSword(handItem)){
			Block b = e.getClickedBlock();
			if(b.getType().equals(Material.REDSTONE_ORE) || b.getType().equals(Material.GLOWING_REDSTONE_ORE)){
				b.setType(Material.AIR);
				Utilities.increaseLore(handItem, Items.redstoneLore, RedstoneSwords.redstoneOreAmount);
				e.setCancelled(true); // normally redstone changes to glowing redstone after right click
				return;
			}
		}
	}
	
	private void swordAbilities(PlayerInteractEvent e){
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
	}
	
	@SuppressWarnings("deprecation")
	private void scrolls(PlayerInteractEvent e){
		// scroll of respiration
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollRespirationName))){
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, RedstoneSwords.respirationTime*20, 1));
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
			
		}
		
		// scroll of jumping
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollJumpName))){
			
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, RedstoneSwords.jumpTime*20, 5));
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
		}
		
		// scroll of healing
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollHealName))){
			
			player.setHealth(player.getMaxHealth());				
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
		}
		
		// scroll of levitation
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollLevitationName))){
			
			flightTimer = RedstoneSwords.flightTime*20;
			oldFlyBool = player.getAllowFlight();
			oldFlySpeed = player.getFlySpeed();
			player.setAllowFlight(true);
			player.setFlySpeed(RedstoneSwords.flightSpeed);
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
			
			new BukkitRunnable(){
				public void run(){
					
					if(flightTimer==0){
						player.setFlying(false);
						player.setAllowFlight(oldFlyBool);
						player.setFlySpeed(oldFlySpeed);
						cancel();
					}else{
						player.setFlying(true);
						if(flightTimer%100 == 0)
							player.sendMessage("Levitation wears off in " + flightTimer/20 + " seconds.");
						flightTimer--;
					}
				}
			}.runTaskTimer(redstoneSwords, 0, 1);
		}
		
		// scroll of fireball
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollFireballName))){
			
			Vector offset = player.getLocation().getDirection().multiply(2);
			Location spawnLoc = player.getEyeLocation().add(offset);
			player.getWorld().spawn(spawnLoc, Fireball.class);
			player.getWorld().playEffect(spawnLoc, Effect.BLAZE_SHOOT, 1);
			
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
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
			Particle.smoke.apply(player, 0.2, 100, 1);
		}
		
		// scroll of bound sword
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollBoundSwordName))){
			// TODO
			// give player sword with unique id
			// remove sword after delay (even from inventory or laying around or stored in a chest etc.)
			
//			final ItemStack boundSword = new ItemStack(Material.DIAMOND_SWORD);
//			final Item boundSwordItem = player.getWorld().dropItemNaturally(player.getLocation(), boundSword);
//			
//			decreaseStack(player, handItem);
//			Particle.smoke.apply(player, 0.2, 100, 1);
//			new BukkitRunnable(){
//				public void run(){
//					redstoneSwords.getLogger().info("removeing shit");
//					boundSwordItem.remove();
//					// TODO aufgesammeltes item, item in kiste oder sonstwo
//				}
//			}.runTaskLater(redstoneSwords, 20*10); //TODO timer nutzen
		}
		
		// scroll of water walking
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollWaterName))){
			waterWalkTimer = RedstoneSwords.waterWalkTime*20;
			last = player.getLocation().getBlock();
			
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
			
			new BukkitRunnable(){
				public void run(){
					
					if(waterWalkTimer%200 == 0)
						player.sendMessage("Water walking wears off in " + waterWalkTimer/20 + " seconds.");
					
					waterWalkTimer--;
					if(waterWalkTimer <= 0)
						cancel();
					
					Location loc = player.getLocation();
					if(last!=null && (loc.getBlockX()!=last.getX() || loc.getBlockZ()!=last.getZ())){
						last.setType(Material.AIR);
					
						Block feet = loc.getBlock();
						Block underneath = loc.clone().add(0, -1, 0).getBlock();
						Block above = loc.clone().add(0, 1, 0).getBlock();
						
						if(!above.getType().equals(Material.STATIONARY_WATER) ){// case player underwater
							// case player swimming on surface
							if(feet.getType().equals(Material.STATIONARY_WATER)){
								above.setType(Material.WATER_LILY);
								last=above;
								player.teleport(loc.clone().add(0, 1.2, 0));// teleport onto lilypad
							}else if(underneath.getType().equals(Material.STATIONARY_WATER)){ // case player above surface
								feet.setType(Material.WATER_LILY);
								last=feet;
							}
						}
					}
				}
			}.runTaskTimer(redstoneSwords, 0, 1);
		}
		
		// scroll of shield
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollShieldName))){
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.ABSORPTION, 
					RedstoneSwords.shieldTime*20, 
					1));
			
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
		}
		
		// scroll of night vision
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollNightVisionName))){
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.NIGHT_VISION, 
					RedstoneSwords.nightVisionTime*20, 
					1));
			
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
		}
		
		// scroll of attack
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollAttackName))){
			player.addPotionEffect(new PotionEffect(
					PotionEffectType.INCREASE_DAMAGE, 
					RedstoneSwords.attackTime*20, 
					RedstoneSwords.attackBonus));
			
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
		}
		
		// scroll of recall
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollRecallName))){
			if(player.getBedSpawnLocation() == null){
				player.sendMessage("There is no location for this recall!");
			}else{
				player.teleport(player.getBedSpawnLocation());
				decreaseStack(player, handItem);
				Particle.smoke.apply(player, 0.2, 100, 1);
			}
		}
		
		// scroll of reflection
		if(Utilities.scrollsEqual(handItem, Items.getScroll(Items.scrollReflectName))){
			reflectionTimer = RedstoneSwords.reflectionTime;
			reflection.add(player);
			
			decreaseStack(player, handItem);
			Particle.smoke.apply(player, 0.2, 100, 1);
			
			new BukkitRunnable(){
				public void run(){
					if(reflectionTimer <= 0){
						reflection.remove(player);
					}else{
						if(reflectionTimer%5 == 0)
							player.sendMessage("Reflection wears off in " + reflectionTimer + " seconds.");
						reflectionTimer--;
					}
				}
			}.runTaskTimer(redstoneSwords, 0, 20);
		}
		
		//TODO
		// scroll of telekinesis
		
	}
	
	@SuppressWarnings("serial")
	private void franciska(PlayerInteractEvent e){
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
	
	
	
	
}
