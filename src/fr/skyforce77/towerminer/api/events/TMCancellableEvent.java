package fr.skyforce77.towerminer.api.events;

public class TMCancellableEvent extends TMEvent{

	private boolean cancelled;
	
	public TMCancellableEvent(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
