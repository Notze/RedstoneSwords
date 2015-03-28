package io.github.notze.mc_redstoneSwords;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
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
				String valueString = lores.get(0).substring(10);
				int upgradeValue = 8; // using redstoneDust
				if(e.getInventory().getItem(1).getType() == Material.REDSTONE_BLOCK) 
					upgradeValue = 72; // using redstoneBlocks
				int value = Integer.parseInt(valueString) + upgradeValue;
				String newLore = "Redstone: " + value;
				lores.clear();
				lores.add(newLore);
				im.setLore(lores);
				im.addEnchant(Enchantment.DAMAGE_ALL, value/100, true);
				output.setItemMeta(im);
			}
			e.getInventory().setResult(output);
		}
	}
}
