package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Packet11ChatMessage extends Packet{

	public String message;
	public String option;
	
	public Packet11ChatMessage() {}
	
	public Packet11ChatMessage(String message, String player) {
		this.message = message;
		this.option = player;
	}
	
	@Override
	public void onReceived(Connection c) {
		MultiPlayer mp = ((MultiPlayer)TowerMiner.menu);
		mp.chat.onMessageReceived(LanguageManager.getText(option)+": "+message);
	}

}
