package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.Mob;
import fr.skyforce77.towerminer.entity.Turret;
import fr.skyforce77.towerminer.menus.MultiPlayer;

public class Packet10EntityValueUpdate extends Packet{

	public int entity;
	public String value;
	public String data;
	
	public Packet10EntityValueUpdate() {}
	
	public Packet10EntityValueUpdate(int entity, String value, String data) {
		this.value = value;
		this.data = data;
		this.entity = entity;
	}
	
	@Override
	public void onClientReceived(Connection c) {
		MultiPlayer mp = ((MultiPlayer)TowerMiner.menu);
		for(Entity en : mp.draw) {
			if(en.getUUID() == entity) {
				if(value.equals("fired")) {
					int ticks = -1;
					if(data.equals("true")) {
						ticks = 1;
					}
					((Mob)en).setFired(ticks);
				} else if(value.equals("turretdata")) {
					int data = Integer.parseInt(this.data);
					Turret tu = (Turret)en;
					while(tu.getData() < data) {
						tu.addData();
					}
				} else if(value.equals("life")) {
					int data = Integer.parseInt(this.data);
					((Mob)en).setLife(data);
				}
			}
		}
	}

}
