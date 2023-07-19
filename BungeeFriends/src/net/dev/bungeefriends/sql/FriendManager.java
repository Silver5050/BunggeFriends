package net.dev.bungeefriends.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import net.dev.bungeefriends.BungeeFriends;

public class FriendManager {

	public static boolean isRequestOpen(UUID uuid, UUID requested) {
		return getRequests(requested).contains(uuid);
	}
	
	public static ArrayList<UUID> getRequests(UUID uuid) {
		ArrayList<UUID> list = new ArrayList<>();
		
		if(isRegistered(uuid)) {
			ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM Friends WHERE UUID = '" + uuid.toString() + "'");
			
			if(rs != null) {
				try {
					if(rs.next()) {
						String s = rs.getString("FriendRequests");
						
						if(s != null) {
							if(!(s.equalsIgnoreCase("[]"))){
								for (String string : s.replace("[", "").replace("]", "").split(", ")) {
									list.add(UUID.fromString(string.trim()));
								}
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				return list;
			}
		}
		
		return list;
	}
	
	public static void addRequest(UUID uuid, UUID requested) {
		ArrayList<UUID> friends = getFriends(requested);
		ArrayList<UUID> requests = getRequests(requested);
			
		if(!(requests.contains(uuid)))
			requests.add(uuid);
			
		BungeeFriends.mysql.update("DELETE FROM Friends WHERE UUID = '" + requested.toString() + "'");
		BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, FriendRequests, OnlineStatus) VALUES ('" + requested.toString() + "', '" + friends.toString() + "', '" + requests.toString() + "', ?)", true);
	}
	
	public static void removeRequest(UUID uuid, UUID requested) {
		ArrayList<UUID> friends = getFriends(requested);
		ArrayList<UUID> requests = getRequests(requested);
		
		if(requests.contains(uuid))
			requests.remove(uuid);
			
		BungeeFriends.mysql.update("DELETE FROM Friends WHERE UUID = '" + requested.toString() + "'");
		BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, FriendRequests, OnlineStatus) VALUES ('" + requested.toString() + "', '" + friends.toString() + "', '" + requests.toString() + "', ?)", true);
	}

	public static void addFriend(UUID user, UUID friend) {
		if(!(isFriend(user, friend))) {
			ArrayList<UUID> requests = getRequests(user);
			ArrayList<UUID> friends = getFriends(user);
			
			if(!(friends.contains(friend)))
				friends.add(friend);
			
			BungeeFriends.mysql.update("DELETE FROM Friends WHERE UUID = '" + user.toString() + "'");
			BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, FriendRequests, OnlineStatus) VALUES ('" + user.toString() + "', '" + friends.toString() + "', '" + requests.toString() + "', ?)", true);
		}
	}
	
	public static void removeFriend(UUID user, UUID friend) {
		if(isFriend(user, friend)) {
			ArrayList<UUID> requests = getRequests(user);
			ArrayList<UUID> friends = getFriends(user);
			
			if(friends.contains(friend))
				friends.remove(friend);
			
			BungeeFriends.mysql.update("DELETE FROM Friends WHERE UUID = '" + user.toString() + "'");
			BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, FriendRequests, OnlineStatus) VALUES ('" + user.toString() + "', '" + friends.toString() + "', '" + requests.toString() + "', ?)", true);
		}
	}
	
	public static boolean isFriend(UUID user, UUID friend) {
		return (getFriends(user).contains(friend));
	}
	
	public static ArrayList<UUID> getFriends(UUID user) {
		ArrayList<UUID> list = new ArrayList<>();
		
		if(isRegistered(user)) {
			ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM Friends WHERE UUID = '" + user.toString() + "'");
			
			if(rs != null) {
				try {
					if(rs.next()) {
						String s = rs.getString("FriendUUIDs");
						
						if(s != null) {
							if(!(s.equalsIgnoreCase("[]"))){
								for (String string : s.replace("[", "").replace("]", "").split(", ")) {
									list.add(UUID.fromString(string.trim()));
								}
							}
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				return list;
			}
		}
		
		return list;
	}

	public static void registerIfNeeded(UUID user) {
		if(!(isRegistered(user)))
			BungeeFriends.mysql.updateWithBoolean("INSERT INTO Friends (UUID, FriendUUIDs, OnlineStatus) VALUES ('" + user.toString() + "', '" + new ArrayList<UUID>().toString() + "', ?)", true);
	}
	
	public static void setOnline(UUID user, boolean online) {
		registerIfNeeded(user);
		
		BungeeFriends.mysql.updateWithBoolean("UPDATE Friends SET OnlineStatus = ? WHERE UUID = '" + user + "'", online);
	}

	public static boolean isRegistered(UUID user) {
		ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM Friends WHERE UUID = '" + user.toString() + "'");
		
		try {
			if(rs != null) {
				if(rs.next()) {
					if(rs.getString("FriendUUIDs") != null) {
						return true;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

	public static boolean isOnline(UUID user) {
		ResultSet rs = BungeeFriends.mysql.getResult("SELECT * FROM Friends WHERE UUID = '" + user.toString() + "'");
		
		if(rs != null) {
			try {
				if(rs.next()) {
					return rs.getBoolean("OnlineStatus");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
			
		return false;
	}

}
