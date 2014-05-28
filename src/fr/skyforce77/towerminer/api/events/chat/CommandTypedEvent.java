package fr.skyforce77.towerminer.api.events.chat;

import fr.skyforce77.towerminer.api.events.TMCancellableEvent;
import fr.skyforce77.towerminer.api.events.TMEvent;

public class CommandTypedEvent extends TMEvent implements TMCancellableEvent{

	private boolean cancelled = false;
	private String label;
	private String[] args;

	public CommandTypedEvent(String label, String[] args) {
		this.label = label;
		this.args = args;
	}
	
	public String getLabel() {
		return label;
	}
	
	public String[] getArguments() {
		return args;
	}
	
	public void setArguments(String[] args) {
		this.args = args;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
}
