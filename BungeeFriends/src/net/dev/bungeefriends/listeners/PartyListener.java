package net.dev.bungeefriends.listeners;

import java.util.concurrent.TimeUnit;

import net.dev.bungeefriends.BungeeFriends;
import net.dev.bungeefriends.party.Party;
import net.dev.bungeefriends.party.PartyManager;
import net.dev.bungeefriends.utils.FileUtils;
import net.dev.bungeefriends.utils.PartyMessageUtils;
import net.dev.bungeefriends.utils.Utils;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PartyListener implements Listener {

	@EventHandler
	public void onServerSwitch(ServerSwitchEvent e) {
		ProxiedPlayer p = e.getPlayer();
		
		if(PartyManager.isInParty(p)) {
			Party party = PartyManager.getPlayerParty(p);
			
			if(party.getLeader() == p) {
				for (ProxiedPlayer member : party.getMembers()) {
					if(member != p) {
						member.connect(p.getServer().getInfo());
					}
					
					member.sendMessage(Utils.getAsBaseComponent(PartyMessageUtils.prefix + FileUtils.getConfig().getString("Messages.Party.ConnectToServer").replace("%serverName%", p.getServer().getInfo().getName())));
				}
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerDisconnectEvent e) {
		ProxiedPlayer p = e.getPlayer();
		
		BungeeCord.getInstance().getScheduler().schedule(BungeeFriends.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if((p == null) || !(p.isConnected())) {
					if(PartyManager.isInParty(p)) {
						PartyManager.getPlayerParty(p).leaveParty(p);
					}
				}
			}
		}, 1, TimeUnit.SECONDS);
	}
	
}
