package io.github.notze.util;

import io.github.notze.redstoneswords.RedstoneSwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
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
	public static String redstoneLore = "Redstone: ";
	public static String expLore = "Experience: ";
	@SuppressWarnings("serial")
	public static HashMap<Integer,String> lores = new HashMap<Integer,String>(){{
		put(0,redstoneLore);
		put(1,expLore);
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
		
		initMeta.setDisplayName(swordName);
		
		List<String> outputLores = new ArrayList<String>();
		for(int i=0; i<loreLength; i++){
			outputLores.add(lores.get(i)+"0");
		}
		
		initMeta.setLore(outputLores);
		sword.setItemMeta(initMeta);
		
		return sword;
	}

}
