package fr.skyforce77.towerminer.entity;

import java.awt.Point;

import fr.skyforce77.towerminer.entity.effects.EntityEffectType;

public class Enderman extends Turret {

    private static final long serialVersionUID = 185462L;

    public Enderman(EntityTypes type, Point location, String owner) {
        super(type, location, owner);
    }

    @Override
    public void onDamage(Mob e) {
        e.removeEffect(EntityEffectType.INVISIBLE);
    }

    @Override
    public EntityTypes getProjectile() {
        return EntityTypes.ENDERPEARL;
    }
    
    public boolean canSee(Mob m) {
    	if(!m.hasEffect(EntityEffectType.INVISIBLE)) {
    		return false;
    	}
    	
    	double i = m.getLocation().distance(location.x, location.y);
    	if(i < this.distance) {
    		return true;
    	}
    	return false;
    };

}
