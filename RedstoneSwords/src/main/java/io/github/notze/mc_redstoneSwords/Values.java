package io.github.notze.mc_redstoneSwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class Values {
	
	private Values instance;
	
	private Material swordMaterial = Material.WOOD_SWORD;
	private String swordName = "Redstone Sword";
	
	private String redstoneLore = "Redstone: ";
	private String expLore = "Experience: ";
	private HashMap<Integer,String> lores = new HashMap<Integer,String>();
	private int loreLength = 2;
	
	private int redstoneFactor = 10;
	private int expFactor = 10;
	
	private ItemMeta initMeta;
	
	private Values(){
		lores.put(0, redstoneLore);
		lores.put(1, expLore);
	}
	
	public Values getInstance(){
		if(instance == null)
			instance = new Values();
		return instance;
	}
	
	public Material getSwordMaterial(){return swordMaterial;}
	public String getSwordName(){return swordName;}
	public String getRedstoneLore(){return redstoneLore;}
	public String getExpLore(){return expLore;}
	public HashMap<Integer,String> getLores(){return lores;}
	public int getLoreLength(){return loreLength;}
	public int getRedstoneFactor(){return redstoneFactor;}
	public int getExpFactor(){return expFactor;}
	
	public ItemMeta getInitMeta(){
		if(initMeta == null){
			ItemStack sword = new ItemStack(swordMaterial);
			ItemMeta initMeta = sword.getItemMeta();
			
			initMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
			initMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			
			initMeta.setDisplayName(swordName);
			
			List<String> outputLores = new ArrayList<String>();
			for(int i=0; i<loreLength; i++){
				outputLores.add(lores.get(i));
			}
			
			initMeta.setLore(outputLores);
		}
		return initMeta;
	}

}
