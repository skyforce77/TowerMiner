package fr.skyforce77.towerminer.api.events.chat;

import fr.skyforce77.towerminer.api.events.TMCancellableEvent;
import fr.skyforce77.towerminer.api.events.TMEvent;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;

public class PlayerChatEvent extends TMEvent implements TMCancellableEvent{

	private boolean cancelled = false;
	private ChatMessage message;
	private String typed;

	public PlayerChatEvent(ChatMessage message, String typed) {
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
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
}
