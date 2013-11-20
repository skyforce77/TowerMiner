package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.MPDisconnected;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Packet1Disconnecting extends Packet{

	public String reason;
	
	public Packet1Disconnecting() {}
	
	public Packet1Disconnecting(String reason) {
		this.reason = reason;
	}
	
	@Override
	public void onClientReceived(Connection c) {
		MPDisconnected m = new MPDisconnected();
		m.reason = LanguageManager.getText(reason);
		TowerMiner.returnMenu(m);
	}
	
	@Override
	public void onServerReceived(Connection c) {
		MPDisconnected m = new MPDisconnected();
		m.reason = LanguageManager.getText(reason);
		m.text = LanguageManager.getText("menu.mp.connection.client");
		TowerMiner.returnMenu(m);
	}

}
