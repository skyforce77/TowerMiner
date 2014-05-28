package fr.skyforce77.towerminer.api.events.menu;

import fr.skyforce77.towerminer.api.events.TMEvent;
import fr.skyforce77.towerminer.menus.Menu;

public class MenuEvent extends TMEvent{

	private Menu menu;

	public MenuEvent(Menu menu) {
		this.menu = menu;
	}
	
	public Menu getMenu() {
		return menu;
	}
	
	public void setMenu(Menu menu) {
		this.menu = menu;
	}
	
}
