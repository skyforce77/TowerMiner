package fr.skyforce77.towerminer.api.events.chat;

import fr.skyforce77.towerminer.api.events.TMEvent;

public class ChatPluginActionEvent extends TMEvent{
	
	private String channel;
	private String action;

	public ChatPluginActionEvent(String channel, String action) {
		this.channel = channel;
		this.action = action;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public String getAction() {
		return action;
	}
	
}
