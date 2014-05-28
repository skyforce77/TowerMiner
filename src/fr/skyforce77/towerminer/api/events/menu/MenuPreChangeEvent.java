package fr.skyforce77.towerminer.api.events.menu;

import fr.skyforce77.towerminer.api.events.TMCancellableEvent;
import fr.skyforce77.towerminer.menus.Menu;

public class MenuPreChangeEvent extends MenuEvent implements TMCancellableEvent{

	private boolean cancelled = false;
	private Menu next;
	
	public MenuPreChangeEvent(Menu menu, Menu next) {
		super(menu);
		this.next = next;
	}
	
	public Menu getNextMenu() {
		return next;
	}
	
	public void setNextMenu(Menu next) {
		this.next = next;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
}
