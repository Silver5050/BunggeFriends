package net.dev.bungeefriends.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class MySQL {

	private String host;
	private int port;
	private String database;
	private String user;
	private String password;
	private Connection con;
	
	public MySQL(String host, int port, String database, String user, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
		
		connect();
		update("CREATE TABLE IF NOT EXISTS Friends (UUID varchar(64), FriendUUIDs TEXT(32000), FriendRequests TEXT(3200), OnlineStatus boolean)");
		update("CREATE TABLE IF NOT EXISTS FriendSettings (UUID varchar(64), Requests boolean, Notify boolean, FriendChat boolean, ServerJumping boolean, PartyInvites boolean)");
	}

	public void connect() {
		try {
			con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
			
			System.out.println("[MySQL] Connected to database");
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			System.out.println("[MySQL] Could not connect");
		}
	}
	
	public void disconnect() {
		try {
			con.close();
			
			System.out.println("[MySQL] Disconnected from database");
		} catch (SQLException ex) {
			ex.printStackTrace();
			
			System.out.println("[MySQL] Could not disconnect");
		}
	}

	public void update(String qry) {
		if(isConnected()) {
			new FutureTask<>(new Runnable() {
				
				PreparedStatement ps;
				
				@Override
				public void run() {
					try {
						ps = con.prepareStatement(qry);

						ps.executeUpdate();
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}, 1).run();
		} else {
			connect();
		}
	}
	
	public void updateWithBoolean(String qry, boolean value) {
		if(isConnected()) {
			new FutureTask<>(new Runnable() {
				
				PreparedStatement ps;
				
				@Override
				public void run() {
					try {
						ps = con.prepareStatement(qry);
						ps.setBoolean(1, value);
						
						ps.executeUpdate();
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}, 1).run();
		} else {
			connect();
		}
	}
	
	public ResultSet getResult(String qry) {
		if(isConnected()) {
			try {
				final FutureTask<ResultSet> task = new FutureTask<ResultSet>(new Callable<ResultSet>() {
					
					PreparedStatement ps;
					
					@Override
					public ResultSet call() throws Exception {
						ps = con.prepareStatement(qry);
						
						return ps.executeQuery();
					}
				});
				
				task.run();
			
				return task.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			connect();
		}
		
		return null;
	}

	public boolean isConnected() {
		return (con != null ? true : false);
	}
	
}