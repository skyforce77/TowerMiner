package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.menus.MultiPlayer;

public class Packet8EntityRemove extends Packet{

	public int entity;

	@Override
	public void onClientReceived(Connection c) {
		final MultiPlayer mp = ((MultiPlayer)TowerMiner.menu);
		new Thread() {
			public void run() {
				Entity ent = null;
				while(ent == null) {
					try {
						sleep(10l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for(Entity en : mp.draw) {
						if(en.getUUID() == entity) {
							ent = en;
						}
					}
				}
				mp.draw.remove(ent);
			};
		}.start();
	}

}
