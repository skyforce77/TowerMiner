package fr.skyforce77.towerminer.api.events.chat;

import fr.skyforce77.towerminer.api.events.TMEvent;

public class ChatPluginActionEvent extends TMEvent{
	
	private String channel;
	private String[] actions;

	public ChatPluginActionEvent(String channel, String[] actions) {
		this.channel = channel;
		this.actions = actions;
	}
	
	public String getChannel() {
		return channel;
	}
	
	public String[] getActions() {
		return actions;
	}
	
}
