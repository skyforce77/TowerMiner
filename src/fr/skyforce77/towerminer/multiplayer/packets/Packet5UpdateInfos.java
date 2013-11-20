package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.MultiPlayer;

public class Packet5UpdateInfos extends Packet{
	
	public int life;
	public int gold;
	
	@Override
	public void onClientReceived(Connection c) {
		MultiPlayer mp = ((MultiPlayer)TowerMiner.menu);
		mp.or = gold;
		mp.vie = life;
	}

}
