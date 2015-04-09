package io.github.notze.util;

import io.github.notze.redstoneswords.RedstoneSwords;

import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class Items{
		
	// Materials
	public static Material swordMaterial = RedstoneSwords.swordMaterial;
	public static Material scrollMaterial = Material.PAPER;
	
	// Names
	public static String swordName = "Redstone Sword";
	public static String scrollFireballName = "Scroll of Fireball";
	public static String scrollCropName = "Scroll of Growth";
	public static String scrollLevitationName = "Scroll of Levitation";
	public static String scrollJumpName = "Scroll of Jumping";
	public static String scrollRespirationName = "Scroll of Respiration";
	public static String scrollRebirthName = "Scroll of Rebirth";
	public static String scrollHealName = "Scroll of Healing";
	public static String scrollWaterName = "Scroll of Water Walking";
	public static String scrollRecallName = "Scroll of Recall";
	public static String scrollShieldName = "Scroll of Shield";
	public static String scrollBoundSwordName = "Scroll of Bound Sword";
	public static String scrollCommandName = "Scroll of Commanding";
	public static String scrollPoisonName = "Scroll of Poisoning";
	public static String scrollNightVisionName = "Scroll of Night Eye";
	public static String scrollReflectName = "Scroll of Reflection";
	public static String scrollAttackName = "Scroll of Fortify Attack";
	
	// Scroll identification
	@SuppressWarnings("serial")
	static List<Pair<String,Enchantment>> scrolls = new ArrayList<Pair<String,Enchantment>>(){{
		add(new Pair<String,Enchantment>(scrollFireballName, Enchantment.ARROW_DAMAGE));
		add(new Pair<String,Enchantment>(scrollCropName, Enchantment.ARROW_FIRE));
		add(new Pair<String,Enchantment>(scrollLevitationName, Enchantment.ARROW_INFINITE));
		add(new Pair<String,Enchantment>(scrollJumpName, Enchantment.ARROW_KNOCKBACK));
		add(new Pair<String,Enchantment>(scrollRespirationName, Enchantment.DAMAGE_ALL));
		add(new Pair<String,Enchantment>(scrollRebirthName, Enchantment.DAMAGE_ARTHROPODS));
		add(new Pair<String,Enchantment>(scrollHealName, Enchantment.DAMAGE_UNDEAD));
		add(new Pair<String,Enchantment>(scrollWaterName, Enchantment.DEPTH_STRIDER));
		add(new Pair<String,Enchantment>(scrollRecallName, Enchantment.DIG_SPEED));
		add(new Pair<String,Enchantment>(scrollShieldName, Enchantment.DURABILITY));
		add(new Pair<String,Enchantment>(scrollBoundSwordName, Enchantment.FIRE_ASPECT));
		add(new Pair<String,Enchantment>(scrollCommandName, Enchantment.KNOCKBACK));
		add(new Pair<String,Enchantment>(scrollPoisonName, Enchantment.LOOT_BONUS_BLOCKS));
		add(new Pair<String,Enchantment>(scrollNightVisionName, Enchantment.LOOT_BONUS_MOBS));
		add(new Pair<String,Enchantment>(scrollReflectName, Enchantment.LUCK));
		add(new Pair<String,Enchantment>(scrollAttackName, Enchantment.OXYGEN));
	}};
	
	// Lores
	public static String enderLore = "Enderpearl: ";
	public static String redstoneLore = "Redstone: ";
	public static String expLore = "Experience: ";
	public static String lvlLore = "Level: ";
	public static String modeLore = "Mode: ";
		public static String noneModeLore = "Nothing";
		public static String boostModeLore = "Speedboost";
		public static String teleportModeLore = "Teleport";
		@SuppressWarnings("serial")
		public static List<String> modes = new ArrayList<String>(){{
			add(noneModeLore);
			add(boostModeLore);
			add(teleportModeLore);
		}};
		
	@SuppressWarnings("serial")
	public static List<String> lores = new ArrayList<String>(){{
		add(enderLore);
		add(redstoneLore);
		add(expLore);
		add(lvlLore);
		add(modeLore); // always last item
	}};
	public static int loreLength = lores.size();
	
	/**
	 * Returns the Scroll matching the give name
	 * 
	 * @param name
	 * 		Name of scroll.
	 * @return
	 * 		The magic scroll.
	 */
	public static ItemStack getScroll(String name){
		ItemStack outputScroll = null;
		
		for(Pair<String,Enchantment> scroll : scrolls){
			String scrollName = scroll.getKey();
			if(scrollName.equals(name)){
				
				outputScroll = new ItemStack(scrollMaterial);
				ItemMeta im = outputScroll.getItemMeta();
				
				im.setDisplayName(scrollName);
				im.addEnchant(scroll.getValue(), 0, true);
				im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
				
				outputScroll.setItemMeta(im);
				break;
			}
		}
		return outputScroll;
	}
	
	/**
	 * Returns a newly crafted Redstone Sword
	 * 
	 * @return
	 * 		The Redstone Sword.
	 */
	public static ItemStack getInitSword(){
		ItemStack sword = new ItemStack(swordMaterial);
		ItemMeta initMeta = sword.getItemMeta();
		
		initMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		initMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		
		initMeta.addEnchant(Enchantment.DAMAGE_ALL, 0, true);
		
		initMeta.setDisplayName(swordName);
		
		List<String> outputLores = new ArrayList<String>();
		for(int i=0; i<loreLength-1; i++){
			outputLores.add(lores.get(i)+"0");
		}
		outputLores.add(lores.get(loreLength-1) + noneModeLore);
				
		initMeta.setLore(outputLores);
		sword.setItemMeta(initMeta);
		
		return sword;
	}

}
