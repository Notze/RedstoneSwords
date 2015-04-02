package io.github.notze.util;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public enum Particle {
	explode("explode"),
	largeexplode("largeexplode"),
	hugeexplosion("hugeexplosion"),
	fireworksSpark("fireworksSpark"),
	bubble("bubble"),
	splash("splash"),
	wake("wake"),
	suspended("suspended"),
	depthsuspend("depthsuspend"),
	crit("crit"),
	magicCrit("magicCrit"),
	smoke("smoke"),
	largesmoke("largesmoke"),
	spell("spell"),
	instantSpell("instantSpell"),
	mobSpell("mobSpell"),
	mobSpellAmbient("mobSpellAmbient"),
	witchMagic("witchMagic"),
	dripWater("dripWater"),
	dripLava("dripLava"),
	angryVillager("angryVillager"),
	happyVillager("happyVillager"),
	townaura("townaura"),
	note("note"),
	portal("portal"),
	enchantmenttable("enchantmenttable"),
	flame("flame"),
	lava("lava"),
	footstep("footstep"),
	cloud("cloud"),
	reddust("reddust"),
	snowballpoof("snowballpoof"),
	snowshovel("snowshovel"),
	slime("slime"),
	heart("heart"),
	barrier("barrier"),
	droplet("droplet"),
	take("take"),
	mobappearance("mobappearance");
	
	private String name;

	private Particle(String name){
		this.name = name;
	}
	
	public void apply(Player player, double speed, int amount, int range){
		Location loc = player.getLocation();
		
		player.getServer().dispatchCommand(
				player.getServer().getConsoleSender(), 
				"particle "
				+ this.name + " " 
				+ loc.getX() + " " 
				+ loc.getY() + " " 
				+ loc.getZ() + " 0 0 0 "
				+ speed + " "
				+ amount + " "
				+ range
		);
	}
}
