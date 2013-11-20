package fr.skyforce77.towerminer.multiplayer.packets;

import java.awt.Point;
import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.Turret;
import fr.skyforce77.towerminer.menus.MultiPlayer;

public class Packet9MouseClick extends Packet{
	
	public int modifier;
	public int selected;
	public int x;
	public int y;
	public int aimed;
	
	@Override
	public void onServerReceived(Connection c) {
		MultiPlayer mp = ((MultiPlayer)TowerMiner.menu);
		Turret aimed = null;
		for(Turret en : mp.turrets) {
			if(en.getUUID() == this.aimed) {
				aimed = en;
			}
		}
		if(modifier == 16) {
			mp.setClientGold(mp.clientgold - EntityTypes.turrets[selected].getPrice());
			EntityTypes type = EntityTypes.turrets[selected];
			try {
				Turret tu = (Turret)type.getEntityClass().getConstructor(EntityTypes.class, Point.class, String.class).newInstance(EntityTypes.turrets[selected], new Point(x,y-1), "client");
				mp.turrets.add(tu);
				mp.onEntityAdded(tu);
			} catch (Exception ex) {}
		} else if(modifier == 4) {
			if(aimed != null && aimed.getPrice() <= mp.clientgold) {
				mp.setClientGold(mp.clientgold - aimed.getPrice());
				aimed.addData();
			}
		} else if(modifier == 8) {
			if(aimed != null) {
				mp.setClientGold(mp.clientgold + (int)(aimed.getCost()*0.75));
				mp.removed.add(aimed);
			}
		}
	}

}
