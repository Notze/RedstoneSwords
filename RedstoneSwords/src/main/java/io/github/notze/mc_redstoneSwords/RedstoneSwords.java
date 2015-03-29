package io.github.notze.mc_redstoneSwords;

import org.bukkit.plugin.java.JavaPlugin;

public final class RedstoneSwords extends JavaPlugin {

	@Override
	public void onEnable() {
		Crafting crafting = new Crafting(this);
		Events events = new Events(this);
		Repair repair = new Repair(this);
		
		getServer().getPluginManager().registerEvents(repair, this); // repair comes first
		getServer().getPluginManager().registerEvents(crafting, this); // special recipes override repairs
		getServer().getPluginManager().registerEvents(events, this); 
		
		this.getCommand("rshelp").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("rs").setExecutor(new RedstoneSwordsCommandExecutor(this));
		
		// Bonus commands
		this.getCommand("rsclearinventory").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("rsstoreinventory").setExecutor(new RedstoneSwordsCommandExecutor(this));
		this.getCommand("test").setExecutor(new RedstoneSwordsCommandExecutor(this));
	}
	
	@Override
	public void onDisable() {}
	
}
