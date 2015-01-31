package fr.skyforce77.towerminer.api.cookie;

import fr.skyforce77.towerminer.api.events.TMEvent;

public class CookieEvent extends TMEvent{

	private String name;
	
	public CookieEvent(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}