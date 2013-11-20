package fr.skyforce77.towerminer.multiplayer.packets;

import java.util.ArrayList;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.Mob;
import fr.skyforce77.towerminer.menus.MultiPlayer;

public class Packet4RoundFinished extends Packet{
	
	public int life;
	public int gold;
	
	@Override
	public void onClientReceived(Connection c) {
		MultiPlayer mp = ((MultiPlayer)TowerMiner.menu);
		mp.or = gold;
		mp.round++;
		mp.vie = life;
		mp.next.setEnabled(true);
		((MultiPlayer)TowerMiner.menu).renderarrow = true;
		ArrayList<Entity> toremove = new ArrayList<>();
		for(Entity en : ((MultiPlayer)TowerMiner.menu).draw) {
			if(en instanceof Mob) {
				toremove.add(en);
			}
		}
		for(Entity en : toremove) {
			((MultiPlayer)TowerMiner.menu).draw.remove(en);
		}
	}

}
