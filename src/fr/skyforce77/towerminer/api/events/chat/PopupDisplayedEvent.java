package fr.skyforce77.towerminer.api.events.chat;

import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.api.events.TMCancellableEvent;
import fr.skyforce77.towerminer.api.events.TMEvent;

public class PopupDisplayedEvent extends TMEvent implements TMCancellableEvent{

	private boolean cancelled = false;
	private Popup popup;

	public PopupDisplayedEvent(Popup popup) {
		this.popup = popup;
	}
	
	public Popup getPopup() {
		return popup;
	}
	
	public void setPopup(Popup popup) {
		this.popup = popup;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
}
