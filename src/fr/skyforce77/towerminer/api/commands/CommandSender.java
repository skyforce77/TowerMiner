package fr.skyforce77.towerminer.api.commands;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.api.Utils;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.packets.Packet11ChatMessage;

public class CommandSender {

	private SenderType type = SenderType.CHAT;
	private Connection c;
	
	public CommandSender(Connection c) {
		this.type = SenderType.OTHER_PLAYER;
		this.c = c;
	}
	
	public CommandSender(SenderType type) {
		this.type = type;
	}
	
	public SenderType getType() {
		return type;
	}
	
	public void sendMessage(ChatMessage message) {
		if(type.equals(SenderType.CHAT)) {
			Utils.write(message);
		} else if(type.equals(SenderType.OTHER_PLAYER) && c != null) {
			Packet11ChatMessage pm = new Packet11ChatMessage(message);
			pm.response = true;
			pm.sendConnectionTCP(c);
		} else if(type.equals(SenderType.CONSOLE)) {
			TowerMiner.printInfo(message.toString());
		}
	}
	
	public void sendMessage(String message) {
		sendMessage(new ChatMessage(message));
	}
	
}
