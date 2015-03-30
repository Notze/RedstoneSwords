package io.github.notze.util;

import io.github.notze.redstoneswords.RedstoneSwords;

import java.util.ArrayList;
import java.util.List;

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
	static String scrollFireballName = "Scroll of Fireball";
	static String scrollCropName = "Scroll of Growth";
	static String swordName = "Redstone Sword";
	
	// Lores
	public static String enderLore = "Enderpearl: ";
	public static String redstoneLore = "Redstone: ";
	public static String expLore = "Experience: ";
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
		add(modeLore); // always last item
	}};
	public static int loreLength = lores.size();
	
	
	public static ItemStack getScrollFireball(){
		ItemStack scroll = new ItemStack(scrollMaterial);
		ItemMeta im = scroll.getItemMeta();
		
		im.setDisplayName(scrollFireballName);
		
		scroll.setItemMeta(im);
		return scroll;
	}
	
	public static ItemStack getScrollCrop(){
		ItemStack scroll = new ItemStack(scrollMaterial);
		ItemMeta im = scroll.getItemMeta();
		
		im.setDisplayName(scrollCropName);
		
		scroll.setItemMeta(im);
		return scroll;
	}
	
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
