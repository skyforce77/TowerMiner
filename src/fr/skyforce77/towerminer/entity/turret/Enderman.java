package fr.skyforce77.towerminer.entity.turret;

import java.awt.Point;

import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.entity.mob.Mob;

public class Enderman extends Turret {

    private static final long serialVersionUID = 185462L;

    public Enderman(EntityTypes type, Point location, String owner) {
        super(type, location, owner);
    }

    @Override
    public void onDamage(Mob e) {
        e.removeEffect(EntityEffectType.INVISIBLE);
    }

    public EntityTypes getProjectile() {
    	return EntityTypes.ENDERPEARL;
    }
    
    public boolean canSee(Mob m) {
    	if(!m.hasEffect(EntityEffectType.INVISIBLE)) {
    		return false;
    	}
    	
    	double i = m.getLocation().distance(getLocation().x, getLocation().y);
    	if(i < getDistance()) {
    		return true;
    	}
    	return false;
    };

}
