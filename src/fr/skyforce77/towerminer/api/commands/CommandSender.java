package fr.skyforce77.towerminer.api.commands;

import fr.skyforce77.towerminer.api.Utils;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;

public class CommandSender {

	private int type = 0;
	
	public CommandSender() {}
	
	public int getType() {
		return type;
	}
	
	public void sendMessage(ChatMessage message) {
		if(type == 0) {
			Utils.write(message);
		}
	}
	
	public void sendMessage(String message) {
		sendMessage(new ChatMessage(message));
	}
}
