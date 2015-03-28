package io.github.notze.mc_redstoneSwords;

import java.util.List;
import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class RedstoneSwords extends JavaPlugin implements Listener {

	@Override
	public void onEnable() {		
		recipes();
		
		getServer().getPluginManager().registerEvents(this, this);
		
		this.getCommand("rshelp").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("rs").setExecutor(new RedstoneSwordsCommandExecutor(this));
		
		// Bonus commands
		this.getCommand("rsclearinventory").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("rsstoreinventory").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("test").setExecutor(new RedstoneSwordsCommandExecutor(this));
	}
	
	@Override
	public void onDisable() {}
	
	
	ItemStack rswordUpgraded = new ItemStack(Material.WOOD_SWORD);
	ShapedRecipe rswordUpgrade = new ShapedRecipe(rswordUpgraded);
	ShapedRecipe rswordUpgradeBlocks = new ShapedRecipe(rswordUpgraded);
	
	public void recipes(){
		ItemStack rsword = new ItemStack(Material.WOOD_SWORD);
		ItemMeta im = rsword.getItemMeta();
		List<String> lores = new ArrayList<String>();
		lores.add("Redstone: 0");
		lores.add("Experience: 0");
		im.setLore(lores);
		im.setDisplayName("Redstone Sword");
		im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
		im.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		rsword.setItemMeta(im);
		
		ShapedRecipe rswordRecipe = new ShapedRecipe(rsword);
		rswordRecipe.shape("r","r","s");
		rswordRecipe.setIngredient('r', Material.REDSTONE);
		rswordRecipe.setIngredient('s', Material.STICK);
		
		getServer().addRecipe(rswordRecipe);
		
		
		rswordUpgrade.shape("rrr","rwr","rrr");
		rswordUpgrade.setIngredient('r', Material.REDSTONE);
		rswordUpgrade.setIngredient('w', Material.WOOD_SWORD);
		rswordUpgradeBlocks.shape("rrr","rwr","rrr");
		rswordUpgradeBlocks.setIngredient('r', Material.REDSTONE_BLOCK);
		rswordUpgradeBlocks.setIngredient('w', Material.WOOD_SWORD);
		
		getServer().addRecipe(rswordUpgrade);
		getServer().addRecipe(rswordUpgradeBlocks);
	}
	
	@EventHandler
	public void onPlayerExpChange(PlayerExpChangeEvent e){
		
		ItemStack handItem = e.getPlayer().getItemInHand();
		if(!(handItem.hasItemMeta())) return;
		ItemMeta im = e.getPlayer().getItemInHand().getItemMeta();
		if(!(im.hasDisplayName())) return;
		String itemName = im.getDisplayName();
		if(!(im.hasLore())) return;
		List<String> lores = im.getLore();
		if(itemName.equals("Redstone Sword")){
			
			String redstoneString = lores.get(0);
			String expValueString = lores.get(1).substring(12);
			lores.clear();
			
			int newExpValue = Integer.parseInt(expValueString) + e.getAmount();
			
			lores.add(redstoneString);
			lores.add("Experience: "+newExpValue);
			im.setLore(lores);
			
			im.addEnchant(Enchantment.DAMAGE_ALL, newExpValue/2, true);
			
			handItem.setItemMeta(im);
			
			e.setAmount(0);
		}
	}
	
	@EventHandler
	public void onPlayerItemBreak(PlayerItemBreakEvent e){
		
		ItemStack handItem = e.getPlayer().getItemInHand();
		if(!(handItem.hasItemMeta())) return;
		ItemMeta im = e.getPlayer().getItemInHand().getItemMeta();
		if(!(im.hasDisplayName())) return;
		String itemName = im.getDisplayName();
		if(!(im.hasLore())) return;
		List<String> lores = im.getLore();
		if(itemName.equals("Redstone Sword")){
			
			String expValueString = lores.get(1).substring(12);
			
			e.getPlayer().setTotalExperience(e.getPlayer().getTotalExperience() + Integer.parseInt(expValueString));
		}
	}
	
	@EventHandler
	public void onPrepareItemCraft(PrepareItemCraftEvent e){
		
		if(e.getInventory().getResult().equals(rswordUpgraded)){
			ItemStack output = null;
			ItemStack[] inventory = e.getInventory().getContents();
			for(ItemStack item : inventory){
				if(item.hasItemMeta() && item.getItemMeta().hasLore()){
					output = item.clone();
					break;
				}
			}
			if(output != null){
				ItemMeta im = output.getItemMeta();
				List<String> lores = im.getLore();
				String redstoneValueString = lores.get(0).substring(10);
				String expString = lores.get(1);
				
				int upgradeValue = 8; // using redstoneDust
				if(e.getInventory().getItem(1).getType() == Material.REDSTONE_BLOCK) 
					upgradeValue = 72; // using redstoneBlocks
				int value = Integer.parseInt(redstoneValueString) + upgradeValue;
				
				String newLore = "Redstone: " + value;
				lores.clear();
				lores.add(newLore);
				lores.add(expString);
				im.setLore(lores);
				
				im.addEnchant(Enchantment.DAMAGE_ALL, value/100, true);
				output.setItemMeta(im);
			}
			e.getInventory().setResult(output);
		}
	}
}
