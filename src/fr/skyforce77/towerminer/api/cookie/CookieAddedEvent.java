package fr.skyforce77.towerminer.api.cookie;

import fr.skyforce77.towerminer.api.events.TMCancellableEvent;

public class CookieAddedEvent extends CookieEvent implements TMCancellableEvent{

	private String cookie;
	private boolean cancelled = false;
	
	public CookieAddedEvent(String server, String name, String cookie) {
		super(name, server);
		this.cookie = cookie;
	}
	
	public String getCookie() {
		return cookie;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}