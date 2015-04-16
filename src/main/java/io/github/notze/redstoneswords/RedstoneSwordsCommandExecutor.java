package io.github.notze.redstoneswords;

import io.github.notze.util.Items;
import io.github.notze.util.Utilities;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class RedstoneSwordsCommandExecutor implements CommandExecutor {

	@SuppressWarnings("unused")
	private RedstoneSwords redstoneSwords;

	public RedstoneSwordsCommandExecutor(RedstoneSwords redstoneSwords) {
		this.redstoneSwords = redstoneSwords;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rsadmininventory")){
			if (!(sender instanceof Player)){
				sender.sendMessage("This command can only be run by a player.");
				return true;
			}else{
				Player player = (Player) sender;
				player.openInventory(Items.getAdminInventory());
				return true;
			}
		}
		
		if (cmd.getName().equalsIgnoreCase("rshelp")){
			sender.sendMessage("You can use the following commands:\n"
					//TODO update it
					+ "/rshelp - show this help\n"
					+ "/rs destroy - destroy the redstonesword in your hand\n"
					+ "/rsclearinventory - remove all items in your inventory\n"
					+ "/rsstoreinventory - store your inventory in a chest\n"
					+ "");
			return true;
		}
		
		/**
		 * Redstone Sword
		 */
		
		if (cmd.getName().equalsIgnoreCase("rs")){
			
			if(args.length!=1){
				return false;
			}
			
			// destroy the sword to get back the xp
			if(args[0].equals("destroy")){
				if (!(sender instanceof Player)){
					sender.sendMessage("This command can only be run by a player.");
					return true;
				}else{
					Player player = (Player) sender;
					ItemStack handItem = player.getItemInHand();
					Utilities.destroyRedstoneSword(player, handItem);
					return true;
				}
			}
			
			// damage item in hand
			if(args[0].equals("damage")){
				if (!(sender instanceof Player)){
					sender.sendMessage("This command can only be run by a player.");
					return true;
				}else{
					Player player = (Player) sender;
					ItemStack handItem = player.getItemInHand();
					
					handItem.setDurability((short) 10000);
					
					return true;
				}
			}
			
//			// powerup sword for testing purpose
//			if(args[0].equals("cheat")){
//				if (!(sender instanceof Player)){
//					sender.sendMessage("This command can only be run by a player.");
//					return true;
//				}else{
//					Player player = (Player) sender;
//					ItemStack handItem = player.getItemInHand();
//					
//					for(int i=0; i<Items.loreLength-1; i++)
//						Utilities.increaseLore(handItem, Items.lores.get(i), 999999);
//					
//					return true;
//				}
//			}
						
			return false;
		}
		
		/**
		 * Additional commands
		 */
		
		
		if (cmd.getName().equalsIgnoreCase("rsclearinventory")){
			if (!(sender instanceof Player)){
				sender.sendMessage("This command can only be run by a player.");
				return true;
			}else{
				Player p = (Player) sender;
				PlayerInventory i = p.getInventory();
				i.clear();
				sender.sendMessage("Your inventory is now empty!");
				return true;
			}
		}
		
		
		if (cmd.getName().equalsIgnoreCase("rsstoreinventory")){
			if (!(sender instanceof Player)){
				sender.sendMessage("This command can only be run by a player.");
				return true;
			}else{
				Player player = (Player) sender;
				PlayerInventory playerInventory = player.getInventory();
				// Check if player has a chest in his inventory
				if(!(playerInventory.contains(Material.CHEST))){
					sender.sendMessage("You need a chest to do that!");
					return true;
				}
				ItemStack[] playerInventoryStack = playerInventory.getContents();
				Location loc = player.getLocation();
				Block block = loc.getBlock();
				if(block.getType() != Material.AIR){
					sender.sendMessage("You can't do that here!");
					return true;
				}
				block.setType(Material.CHEST);
				BlockState state = block.getState();
				Chest chest = (Chest) state;
				Inventory chestInventory = chest.getBlockInventory();
				
				// remove one chest from inventory
				for(ItemStack stack : playerInventoryStack){
					if(stack != null && stack.getType() == Material.CHEST){
						stack.setAmount(stack.getAmount()-1);
						break;
					}
				}
				
				// store inventory into chest
				for(int i=0; i<=chestInventory.getSize()-1; i++){
					chestInventory.setItem(i, playerInventoryStack[i+9]);
					playerInventoryStack[i+9] = null;
				}
				playerInventory.setContents(playerInventoryStack);
				
				sender.sendMessage("Your inventory is now stored in a chest!");
				return true;
			}
		}
			
		return false;
	}
}
