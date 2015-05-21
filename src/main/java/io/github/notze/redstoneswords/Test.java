package io.github.notze.redstoneswords;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class Test {

	//TODO just a test now
	public static void test(Player p){
		
		final Server server = p.getServer();
		final UUID id = p.getUniqueId();
		
		
		
//			rocket.setFireTicks(200);
		
		
		new BukkitRunnable(){
			Player p = server.getPlayer(id);
			Location loc = p.getLocation();
			
			Firework rocket = (Firework) p.getWorld().spawnEntity(loc, EntityType.FIREWORK);
			@SuppressWarnings("unused")
			Boolean foo = rocket.setPassenger(p);
			@SuppressWarnings("unused")
			FireworkMeta fm = rocket.getFireworkMeta();
//			fm.setPower(20);
//			rocket.setFireworkMeta(fm);
			
			@Override
			public void run() {
				rocket.setVelocity(loc.getDirection());
			}
			
		}.runTaskTimer(RedstoneSwords.instance, 0, 200);
	}

}
