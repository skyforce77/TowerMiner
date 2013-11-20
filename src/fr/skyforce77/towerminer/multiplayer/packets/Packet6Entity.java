package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.multiplayer.BigSending;

public class Packet6Entity extends Packet{
	
	public int id = 0;
	
	@Override
	public void onClientReceived(Connection c) {
		MultiPlayer mp = ((MultiPlayer)TowerMiner.menu);
		Entity en = (Entity)deserialize(BigSending.receiving.get(id).data);
		mp.draw.add(en);
	}

}
