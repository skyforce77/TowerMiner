package fr.skyforce77.towerminer.entity.turret;

import java.awt.Point;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;

public class Slime extends Turret {

    private static final long serialVersionUID = 185462L;

    public Slime(EntityTypes type, Point location, String owner) {
        super(type, location, owner);
    }

    public EntityTypes getProjectile() {
    	return EntityTypes.SLIME_BALL;
    }
    
    @Override
    public void onTick() {
        if (!(TowerMiner.menu instanceof SinglePlayer) || (TowerMiner.menu instanceof MultiPlayer && !((MultiPlayer) TowerMiner.menu).server)) {
            return;
        }
        SinglePlayer sp = (SinglePlayer) TowerMiner.menu;

        double distance = 99999;
        Mob e = null;
        for (Entity en : sp.mobs) {
            double i = en.getLocation().distance(getLocation().x, getLocation().y);
            if (i < distance && i < distance && canSee((Mob) en)) {
                distance = i;
                e = (Mob) en;
            }
        }

        if (tir >= 40) {
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
