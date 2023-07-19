package net.dev.bungeefriends.party;

import java.util.Collection;
import java.util.HashMap;

import net.dev.bungeefriends.utils.FileUtils;
import net.dev.bungeefriends.utils.PartyMessageUtils;
import net.dev.bungeefriends.utils.Utils;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

public class Party {

	private ProxiedPlayer leader;
	public HashMap<ProxiedPlayer, PartyRank> members = new HashMap<>();
	
	public Party(ProxiedPlayer leader) {
		this.leader = leader;
		
		members.put(leader, PartyRank.LEADER);
	}
	
	public void addMember(ProxiedPlayer member) {
		members.put(member, PartyRank.MEMBER);
	}

	public void promote(ProxiedPlayer p) {
		if(members.get(p) == PartyRank.MEMBER) {
			members.remove(p);
			members.put(p, PartyRank.MOD);
		} else if(members.get(p) == PartyRank.MOD) {
			setLeader(p);
		}
	}
	
	public void demote(ProxiedPlayer p) {
		if(members.get(p) == PartyRank.MOD) {
			members.remove(p);
			members.put(p, PartyRank.MEMBER);
		}
	}
	
	public void setLeader(ProxiedPlayer p) {
		members.remove(leader);
		members.put(leader, PartyRank.MOD);
		
		members.remove(p);
		members.put(p, PartyRank.LEADER);
		
		this.leader = p;
	}
	
	public void leaveParty(ProxiedPlayer p) {
		Configuration cfg = FileUtils.getConfig();
		
		for (ProxiedPlayer member : getMembers()) {
			member.sendMessage(Utils.getAsBaseComponent(PartyMessageUtils.prefix + cfg.getString("Messages.Party.Left").replace("%player%", p.getDisplayName())));
		}

		members.remove(p);
		
		if(leader == p) {
			for (ProxiedPlayer member : getMembers()) {
				member.sendMessage(Utils.getAsBaseComponent(PartyMessageUtils.prefix + cfg.getString("Messages.Party.Deleted")));
			}
			
			p.sendMessage(Utils.getAsBaseComponent(PartyMessageUtils.prefix + cfg.getString("Messages.Party.Deleted")));
			
			PartyManager.parties.remove(this);
		}
	}
	
	public ProxiedPlayer getLeader() {
		return leader;
	}
	
	public Collection<ProxiedPlayer> getMembers() {
		return members.keySet();
	}
	
	public boolean isMember(ProxiedPlayer p) {
		return (getMembers().contains(p));
	}
	
}
