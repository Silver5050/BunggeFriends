package net.dev.bungeefriends.listeners;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChannelListener implements Listener {

	@EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            
            try {
                String channel = in.readUTF();
                
                if(channel.equals("BungeeCommands")){
                	String playerName = in.readUTF();
                	String command = in.readUTF();
                	
                	BungeeCord.getInstance().getPluginManager().dispatchCommand(BungeeCord.getInstance().getPlayer(playerName), command.replace("/", ""));
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
      
        }
    }
	
}
