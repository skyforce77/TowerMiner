package fr.skyforce77.towerminer.entity.turret;

import java.awt.Point;

import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.effects.EntityEffect;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.entity.mob.Mob;

public class Spider extends Turret {

    private static final long serialVersionUID = 185462L;

    public Spider(EntityTypes type, Point location, String owner) {
        super(type, location, owner);
        Achievements.unlockAchievement(6);
    }

    @Override
    public void onDamage(Mob e) {
        e.addEffect(new EntityEffect(EntityEffectType.POISONNED, 40 * getLevel()));
    }

    public EntityTypes getProjectile() {
    	return EntityTypes.POISON_POTION;
    }

}
