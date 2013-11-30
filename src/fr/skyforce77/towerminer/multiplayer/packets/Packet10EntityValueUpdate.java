package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.Mob;
import fr.skyforce77.towerminer.entity.Turret;
import fr.skyforce77.towerminer.entity.effects.EntityEffect;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.menus.MultiPlayer;

public class Packet10EntityValueUpdate extends Packet{

	public int entity;
	public String value;
	public byte[] data;
	
	public Packet10EntityValueUpdate() {}
	
	public Packet10EntityValueUpdate(int entity, String value, int data) {
		this.value = value;
		this.data = this.serialize(data);
		this.entity = entity;
	}
	
	public Packet10EntityValueUpdate(int entity, EntityEffect data) {
		this.value = "addeffect";
		this.data = this.serialize(data);
		this.entity = entity;
	}
	
	@Override
	public void onClientReceived(Connection c) {
		MultiPlayer mp = ((MultiPlayer)TowerMiner.menu);
		for(Entity en : mp.draw) {
			if(en.getUUID() == entity) {
				if(value.equals("addeffect")) {
					((Mob)en).addEffect((EntityEffect)deserialize(data));
				} else if(value.equals("rmveffect")) {
					((Mob)en).removeEffect(EntityEffectType.byId((int)deserialize(data)));
				} else if(value.equals("turretdata")) {
					int data = (int)deserialize(this.data);
					Turret tu = (Turret)en;
					while(tu.getData() < data) {
						tu.addData();
					}
				} else if(value.equals("life")) {
					int data = (int)deserialize(this.data);
					((Mob)en).setLife(data);
				}
			}
		}
	}

}
