package fr.skyforce77.towerminer.api.cookie;

import fr.skyforce77.towerminer.api.events.TMCancellableEvent;
import fr.skyforce77.towerminer.protocol.save.TMStorage;

public class CookieRemovedEvent extends CookieEvent implements TMCancellableEvent{

	private TMStorage cookie;
	private boolean cancelled = false;
	
	public CookieRemovedEvent(String name, TMStorage cookie) {
		super(name);
		this.cookie = cookie;
	}
	
	public TMStorage getCookie() {
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