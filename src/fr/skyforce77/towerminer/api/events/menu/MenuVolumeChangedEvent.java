package fr.skyforce77.towerminer.api.events.menu;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.sounds.Music;

public class MenuVolumeChangedEvent extends MenuEvent {

	private float volume;
	
	public MenuVolumeChangedEvent(Menu menu, float volume) {
		
		super(menu);
		
		TowerMiner.printInfo("Modification du volume " + volume);
		this.volume = volume;
		Music.volumeChange(volume);
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		this.volume = volume;
	}
	
}
