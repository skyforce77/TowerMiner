package fr.skyforce77.towerminer.entity;

import java.awt.Point;

import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.entity.effects.EntityEffect;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;

public class Witch extends Turret {

    private static final long serialVersionUID = 185462L;

    public Witch(EntityTypes type, Point location, String owner) {
        super(type, location, owner);
        Achievements.unlockAchievement(4);
    }

    @Override
    public void onDamage(Mob e) {
        e.addEffect(new EntityEffect(EntityEffectType.SLOW, 40 * getData()));
    }

    @Override
    public EntityTypes getProjectile() {
        return EntityTypes.WEAKNESS_POTION;
    }
    
    public boolean canSee(Mob m) {
    	double i = m.getLocation().distance(location.x, location.y);
    	if(i < this.distance) {
    		return true;
    	}
    	return false;
    };

}
