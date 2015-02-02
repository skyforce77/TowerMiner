package fr.skyforce77.towerminer.api.cookie;

import fr.skyforce77.towerminer.api.events.TMEvent;

public class CookieEvent extends TMEvent{

	private String name;
	private String server;
	
	public CookieEvent(String name, String server) {
		this.name = name;
		this.server = server;
	}
	
	public String getName() {
		return name;
	}
	
	public String getServer() {
		return server;
	}
}