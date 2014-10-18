package fr.skyforce77.towerminer.entity.turret;

import java.awt.Point;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.effects.EntityEffect;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;

public class SnowGolem extends Turret {

    private static final long serialVersionUID = 185462L;

    public SnowGolem(EntityTypes type, Point location, String owner) {
        super(type, location, owner);
        Achievements.unlockAchievement(4);
    }

    @Override
    public void onDamage(Mob e) {
        e.addEffect(new EntityEffect(EntityEffectType.FREEZE, 10 * getLevel()));
    }

    public EntityTypes getProjectile() {
    	return EntityTypes.SNOWBALL;
    }
    
    @Override
    public void onTick() {
        if (!(TowerMiner.menu instanceof SinglePlayer) || (TowerMiner.menu instanceof MultiPlayer && !((MultiPlayer)TowerMiner.menu).server)) {
            return;
        }
        SinglePlayer sp = (SinglePlayer) TowerMiner.menu;

        int life = 99999;
        Mob e = null;
        for (Entity en : sp.mobs) {
            int i = ((Mob)en).getLife();
            if (i < life && i < life && canSee((Mob)en)) {
                life = i;
                e = (Mob) en;
            }
        }

        if (tir >= 40 - (getLevel() * 2)) {
            if (e != null) {
                createProjectile(e).spawn();
                tir = 0;
            }
        } else {
            tir++;
        }

        setRotationAim(e);
    }

}
