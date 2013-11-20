package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MPServerWait;
import fr.skyforce77.towerminer.multiplayer.BigSending;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.multiplayer.MPInfos;
import fr.skyforce77.towerminer.multiplayer.ObjectReceiver.ReceivingThread;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Packet0Connecting extends Packet{

	public int version = TowerMiner.game.connectversion;

	@Override
	public void onServerReceived(final Connection c) {
		if(MPInfos.connected) {
			c.sendTCP(new Packet1Disconnecting("menu.mp.client.kick.match"));
		} else if(version != TowerMiner.game.connectversion) {
			c.sendTCP(new Packet1Disconnecting("menu.mp.client.kick.version"));
		} else {
			MPInfos.connected = true;
			((MPServerWait)TowerMiner.menu).text = LanguageManager.getText("menu.mp.server.connect");
			Connect.c = c;
			c.sendTCP(new Packet3Action("sendingmap"));
			BigSending.sendBigObject(Maps.getActualMap(), new ReceivingThread() {
				@Override
				public void run(int objectid) {
					c.sendTCP(new Packet3Action("finishedsendingmap", (byte)objectid));
				}
			});
		}
	}

}
