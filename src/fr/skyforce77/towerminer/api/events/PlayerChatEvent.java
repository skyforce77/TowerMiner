package fr.skyforce77.towerminer.api.events;

import fr.skyforce77.towerminer.protocol.chat.ChatMessage;

public class PlayerChatEvent extends TMCancellableEvent{

	private ChatMessage message;
	private String typed;

	public PlayerChatEvent(ChatMessage message, String typed) {
		super(false);
		this.message = message;
		this.typed = typed;
	}
	
	public ChatMessage getMessage() {
		return message;
	}
	
	public String getTypedMessage() {
		return typed;
	}
	
	public void setMessage(ChatMessage message) {
		this.message = message;
	}
	
}
