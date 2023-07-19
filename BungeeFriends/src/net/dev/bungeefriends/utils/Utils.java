package net.dev.bungeefriends.utils;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class Utils {

	public static void sendConsole(String msg) {
		BungeeCord.getInstance().getConsole().sendMessage(getAsBaseComponent(msg));
	}
	
	public static TextComponent getAsBaseComponent(String s) {
		return (new TextComponent(ChatColor.translateAlternateColorCodes('&', s)));
	}

}
