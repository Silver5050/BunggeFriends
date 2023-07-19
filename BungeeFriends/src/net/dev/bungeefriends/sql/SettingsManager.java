package net.dev.bungeefriends.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import net.dev.bungeefriends.BungeeFriends;

public class SettingsManager {

	public static void registerIfNeeded(UUID user) {
		if(!(isRegistered(user)))
			BungeeFriends.mysql.update("INSERT INTO FriendSettings (UUID, Requests, Notify, FriendChat, ServerJumping, PartyInvites) VALUES ('" + user.toString() + "', true, true, true, true, true)");
	}
	
	public static boolean isRegistered(UUID user) {
		ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user.toString() + "'");
		
		try {
			if(rs != null) {
				if(rs.next()) {
					return true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static void setGetRequests(UUID user, boolean b) {
		registerIfNeeded(user);
		
		BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET Requests = ? WHERE UUID = '" + user + "'", b);
	}

	public static boolean isGettingRequests(UUID user) {
		ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user.toString() + "'");
		
		if(rs != null) {
			try {
				if(rs.next()) {
					return rs.getBoolean("Requests");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		return true;
	}
	
	public static void setGetNotified(UUID user, boolean b) {
		registerIfNeeded(user);
		
		BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET Notify = ? WHERE UUID = '" + user + "'", b);
	}

	public static boolean isGettingNotified(UUID user) {
		ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user.toString() + "'");
		
		if(rs != null) {
			try {
				if(rs.next()) {
					return rs.getBoolean("Notify");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		return true;
	}
	
	public static void setUseFriendChat(UUID user, boolean b) {
		registerIfNeeded(user);
		
		BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET FriendChat = ? WHERE UUID = '" + user + "'", b);
	}

	public static boolean isUsingFriendChat(UUID user) {
		ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user.toString() + "'");
		
		if(rs != null) {
			try {
				if(rs.next()) {
					return rs.getBoolean("FriendChat");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		return true;
	}
	
	public static void setServerJuming(UUID user, boolean b) {
		registerIfNeeded(user);
		
		BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET ServerJumping = ? WHERE UUID = '" + user + "'", b);
	}

	public static boolean isAllowingServerJumping(UUID user) {
		ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user.toString() + "'");
		
		if(rs != null) {
			try {
				if(rs.next()) {
					return rs.getBoolean("ServerJumping");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		return true;
	}
	
	public static void setGetPartyInvites(UUID user, boolean b) {
		registerIfNeeded(user);
		
		BungeeFriends.mysql.updateWithBoolean("UPDATE FriendSettings SET PartyInvites = ? WHERE UUID = '" + user + "'", b);
	}

	public static boolean isGettigPartyInvites(UUID user) {
		ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM FriendSettings WHERE UUID = '" + user.toString() + "'");
		
		if(rs != null) {
			try {
				if(rs.next()) {
					return rs.getBoolean("PartyInvites");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		return true;
	}

}
