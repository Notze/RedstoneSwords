package io.github.notze.mc_redstoneSwords;

import org.bukkit.plugin.java.JavaPlugin;

public final class RedstoneSwords extends JavaPlugin {

	@Override
	public void onEnable() {
		getLogger().info("onEnable invoced!");
		Crafting crafting = new Crafting(this);
		getLogger().info("Constructor of Crafting should be invoced!");
		
		getServer().getPluginManager().registerEvents(crafting, this);
		
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
