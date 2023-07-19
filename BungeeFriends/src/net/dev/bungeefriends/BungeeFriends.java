package net.dev.bungeefriends;

import net.dev.bungeefriends.commands.FriendCommand;
import net.dev.bungeefriends.commands.MSGCommand;
import net.dev.bungeefriends.commands.PartyChatCommand;
import net.dev.bungeefriends.commands.PartyCommand;
import net.dev.bungeefriends.commands.ReplyCommand;
import net.dev.bungeefriends.listeners.ChannelListener;
import net.dev.bungeefriends.listeners.FriendListener;
import net.dev.bungeefriends.listeners.PartyListener;
import net.dev.bungeefriends.sql.MySQL;
import net.dev.bungeefriends.utils.FileUtils;
import net.dev.bungeefriends.utils.Utils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;

public class BungeeFriends extends Plugin {

	private static BungeeFriends instance;
	
	public static BungeeFriends getInstance() {
		return instance;
	}
	
	public static MySQL mysql;
	
	@Override
	public void onEnable() {
		instance = this;
		
		FileUtils.setupFiles();
		
		Configuration cfg = FileUtils.getConfig();
		
		FileUtils.saveFile(cfg);
		
		mysql = new MySQL(cfg.getString("MySQL.host"), cfg.getInt("MySQL.port"), cfg.getString("MySQL.database"), cfg.getString("MySQL.user"), cfg.getString("MySQL.password"));
	
		PluginManager pm = BungeeCord.getInstance().getPluginManager();
		
		if(FileUtils.getConfig().getBoolean("Settings.UseFriendFeatures")) {
			pm.registerCommand(this, new FriendCommand("friend"));
			pm.registerCommand(this, new MSGCommand("msg"));
			pm.registerCommand(this, new ReplyCommand("r"));
			
			pm.registerListener(this, new FriendListener());
		}
		
		if(FileUtils.getConfig().getBoolean("Settings.UsePartyFeatures")) {
			pm.registerCommand(this, new PartyCommand("party"));
			pm.registerCommand(this, new PartyChatCommand("pc"));
			
			pm.registerListener(this, new PartyListener());
		}
		
		pm.registerListener(this, new ChannelListener());
		ProxyServer.getInstance().registerChannel("BungeeCommands");
		
		Utils.sendConsole("§8[§aBungeeFriends§8] §7PluginState§8: §aENABLED");
	}
	
	@Override
	public void onDisable() {
		mysql.disconnect();
		
		Utils.sendConsole("§8[§aBungeeFriends§8] §7PluginState§8: §cDISABLED");
	}
	
}
