package fr.skyforce77.towerminer.api.events.menu;

import fr.skyforce77.towerminer.menus.Menu;

public class MenuReturnEvent extends MenuPreChangeEvent{
	
	public MenuReturnEvent(Menu menu, Menu next) {
		super(menu, next);
	}
	
}
