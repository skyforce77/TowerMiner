package fr.skyforce77.towerminer.entity.effects;

import java.util.HashMap;
import fr.skyforce77.towerminer.entity.Mob;

public class EntityEffectType {

	public static EntityEffectType FIRED;
	public static EntityEffectType POISONNED;
	public static EntityEffectType SLOW;
	
	private static HashMap<Integer, EntityEffectType> types = new HashMap<>();
	
	public static void createEntityEffectTypes(){
		FIRED = new FireEffect();
		POISONNED = new PoisonEffect();
		SLOW = new EntityEffectType(2);
	}
	
	public static EntityEffectType byId(int id) {
		return types.get(id);
	}
	
	int type = 0;
	
	protected EntityEffectType(int type) {
		this.type = type;
		types.put(type, this);
	}
	
	public int getID() {
		return type;
	}
	
	public void onTick(Mob m, EntityEffect e) {
		if(e.getTicks() > -1) {
			e.setTicks(e.getTicks()-1);
			
			if(e.getTicks() <= -1) {
				m.removeEffect(this);
			}
		}
	}

}
