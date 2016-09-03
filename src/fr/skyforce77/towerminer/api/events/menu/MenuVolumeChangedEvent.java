package fr.skyforce77.towerminer.api.events.menu;

import fr.skyforce77.towerminer.menus.Menu;

public class MenuVolumeChangedEvent extends MenuEvent {

	private float volume;
	
	public MenuVolumeChangedEvent(Menu menu, float volume) {
		super(menu);
		this.volume = volume;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}
	
}
