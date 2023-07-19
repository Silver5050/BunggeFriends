package net.dev.bungeefriends.listeners;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import net.dev.bungeefriends.BungeeFriends;
import net.dev.bungeefriends.sql.FriendManager;
import net.dev.bungeefriends.sql.SettingsManager;
import net.dev.bungeefriends.utils.FileUtils;
import net.dev.bungeefriends.utils.FriendMessageUtils;
import net.dev.bungeefriends.utils.Utils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerConnectEvent.Reason;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class FriendListener implements Listener {

	@EventHandler
	public void onJoin(ServerConnectEvent e) {
		ProxiedPlayer p = (ProxiedPlayer) e.getPlayer();
	
		if(!(e.isCancelled()) && (e.getReason() == Reason.JOIN_PROXY)) {
			if(!(FriendManager.isOnline(p.getUniqueId()))) {
				FriendManager.registerIfNeeded(p.getUniqueId());
				
				FriendManager.setOnline(p.getUniqueId(), true);
				
				for (UUID uuid : FriendManager.getFriends(p.getUniqueId())) {
					ProxiedPlayer friend = BungeeCord.getInstance().getPlayer(uuid);
					
					if(friend != null) {
						if(SettingsManager.isGettingNotified(uuid))
							friend.sendMessage(Utils.getAsBaseComponent(FriendMessageUtils.prefix + FileUtils.getConfig().getString("Messages.Friend.FriendOnline").replace("%player%", p.getDisplayName())));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerDisconnectEvent e) {
		ProxiedPlayer p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		String displayName = p.getDisplayName();
		
		BungeeCord.getInstance().getScheduler().schedule(BungeeFriends.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if((p == null) || !(p.isConnected())) {
					FriendManager.setOnline(uuid, false);
					
					for (UUID uuid : FriendManager.getFriends(uuid)) {
						ProxiedPlayer friend = BungeeCord.getInstance().getPlayer(uuid);
						
						if(friend != null) {
							if(SettingsManager.isGettingNotified(uuid))
								friend.sendMessage(Utils.getAsBaseComponent(FriendMessageUtils.prefix + FileUtils.getConfig().getString("Messages.Friend.FriendOffline").replace("%player%", displayName)));
						}
					}
				}
			}
		}, 2, TimeUnit.SECONDS);
	}
	
}
