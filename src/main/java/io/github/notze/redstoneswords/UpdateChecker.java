package io.github.notze.redstoneswords;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateChecker implements Listener {

	// instance of main class
	RedstoneSwords redstoneSwords;
	
	// logger of main class
	Logger logger;
	
	// latest version + download link
	private String version = null;
	private String link = null;
	
	// link to rss file feed of this plugin
	private String urlString = "http://dev.bukkit.org/bukkit-plugins/redstone-swords/files.rss";
	private URL filesFeed;
	
	/**
	 * Sets reference to main class and initializes the feed.
	 * 
	 * @param redstoneSwords
	 * 		Instance of main class.
	 */
	public UpdateChecker(RedstoneSwords redstoneSwords){
		this.redstoneSwords = redstoneSwords;
		this.logger	= redstoneSwords.getLogger();
		try {
			filesFeed = new URL(urlString);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			logger.info("Unable to check for updates!");
		}
	}
	
	/**
	 * Read the url, check the latest version, and print an update message if needed.
	 */
	public void execute() {
		if(RedstoneSwords.checkForUpdates){
			try {
				InputStream in = filesFeed.openConnection().getInputStream();
				// build xml file from input stream
				Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
				
				// get the first peace of html code enclosed by <item></item>
				Node latestFile = doc.getElementsByTagName("item").item(0);
				NodeList childs = latestFile.getChildNodes();
				
				this.version = childs.item(1).getTextContent().substring(15).replaceAll("-?[a-z]* .*", "");
				this.link = childs.item(3).getTextContent();
				
				if(!(redstoneSwords.getDescription().getVersion().equals(this.version))){
					logger.info("A newer Version of Redstone Swords is available! (" + this.version +")");
					logger.info("get it from: " + this.link);
				}
				
				in.close();
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("Unable to check for updates!");
			}
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		sendMessageToPlayer(e.getPlayer());
	}
	
	/**
	 * Sends a message about a newer version to a player.
	 * 
	 * @param player
	 * 		the player to receive this message.
	 */
	private void sendMessageToPlayer(Player player){
		if(this.version != null){
			if(!(redstoneSwords.getDescription().getVersion().equals(this.version))){
				player.sendMessage(ChatColor.YELLOW + "A newer Version of Redstone Swords is available! (" + this.version +")");
				player.sendMessage("get it from: " + this.link);
			}
		}
	}

}
