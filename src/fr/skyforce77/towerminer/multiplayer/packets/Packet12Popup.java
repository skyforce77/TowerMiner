package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Packet12Popup extends Packet{

	public String message;
	public String option;
	public String image;
	
	public Packet12Popup() {}
	
	public Packet12Popup(String message) {
		this.message = message;
	}
	
	public Packet12Popup(String message, String option) {
		this.message = message;
		this.option = option;
	}
	
	public Packet12Popup(String message, String option, String image) {
		this.message = message;
		this.option = option;
		this.image = image;
	}

	@Override
	public void onReceived(Connection c) {
		if(option != null) {
			if(image != null) {
				TowerMiner.game.displayPopup(new Popup(LanguageManager.getText(message, LanguageManager.getText(option)), 3000, image));
			} else {
				TowerMiner.game.displayPopup(new Popup(LanguageManager.getText(message, LanguageManager.getText(option)),3000));
			}
		} else {
			if(image != null) {
				TowerMiner.game.displayPopup(new Popup(LanguageManager.getText(message), 3000, image));
			} else {
				TowerMiner.game.displayPopup(new Popup(LanguageManager.getText(message),3000));
			}
		}
	}

}
