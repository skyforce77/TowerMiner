package fr.skyforce77.towerminer.api.commands;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.api.Utils;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.packets.Packet11ChatMessage;

public class CommandSender {

	private int type = 0;
	private Connection c;
	
	public CommandSender() {}
	
	public CommandSender(Connection c) {
		this.type = 1;
		this.c = c;
	}
	
	public CommandSender(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
	
	public void sendMessage(ChatMessage message) {
		if(type == 0) {
			Utils.write(message);
		} else if(type == 1 && c != null) {
			Packet11ChatMessage pm = new Packet11ChatMessage(message);
			pm.response = true;
			pm.sendConnectionTCP(c);
		}
	}
	
	public void sendMessage(String message) {
		sendMessage(new ChatMessage(message));
	}
}
