package fr.skyforce77.towerminer.multiplayer.packets;

import java.awt.Point;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.Mob;
import fr.skyforce77.towerminer.menus.MultiPlayer;

public class Packet7EntityMove extends Packet{
	
	public int entity;
	public int x;
	public int y;
	public double rotation;
	public int xto;
	public int yto;
	
	@Override
	public void onClientReceived(Connection c) {
		MultiPlayer mp = ((MultiPlayer)TowerMiner.menu);
		for(Entity en : mp.draw) {
			if(en.getUUID() == entity) {
				if(en instanceof Mob) {
					en.setLocation(new Point(x,y));
					en.setRotation(rotation);
					((Mob)en).moveTo(new Point(xto,yto));
				}
			}
		}
	}

}
