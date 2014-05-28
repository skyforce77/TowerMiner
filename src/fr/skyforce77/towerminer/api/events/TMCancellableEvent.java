package fr.skyforce77.towerminer.api.events;

public interface TMCancellableEvent{
	
	public boolean isCancelled();
	
	public void setCancelled(boolean cancelled);
}
