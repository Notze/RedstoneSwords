package io.github.notze.mc_redstoneSwords;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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
		if (cmd.getName().equalsIgnoreCase("rshelp")){
			sender.sendMessage("You can use the following commands:\n"
					+ "/rshelp - show this help\n"
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
					Toolbox.destroyRedstoneSword(player, handItem);
					return true;
				}
			}
			
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
			
		
		if (cmd.getName().equalsIgnoreCase("test")){
			if (!(sender instanceof Player)){
				sender.sendMessage("This command can only be run by a player.");
				return true;
			}else{
				
				Location loc = ((Player)sender).getLocation();
				int length = Integer.parseInt(args[0]);

				generateCube(loc, length);
				
			}
		}
		
		
		
		return false;
	}

	
	/**
	 * test functions
	 */
	
    public void generateCube(Location loc, int length) {
        // Set one corner of the cube to the given location.
        // Uses getBlockN() instead of getN() to avoid casting to an int later.
        int x1 = loc.getBlockX(); 
        int y1 = loc.getBlockY();
        int z1 = loc.getBlockZ();
     
        // Figure out the opposite corner of the cube by taking the corner and adding length to all coordinates.
        int x2 = x1 + length;
        int y2 = y1 + length;
        int z2 = z1 + length;
     
        World world = loc.getWorld();
     
        // Loop over the cube in the x dimension.
        for (int xPoint = x1; xPoint <= x2; xPoint++) { 
            // Loop over the cube in the y dimension.
            for (int yPoint = y1; yPoint <= y2; yPoint++) {
                // Loop over the cube in the z dimension.
                for (int zPoint = z1; zPoint <= z2; zPoint++) {
                    // Get the block that we are currently looping over.
                    Block currentBlock = world.getBlockAt(xPoint, yPoint, zPoint);
                    // Set the block to type 57 (Diamond block!)
                    currentBlock.setType(Material.DIAMOND_BLOCK);
                }
            }
        }
    }
	
}
