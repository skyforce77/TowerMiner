package fr.skyforce77.towerminer.entity.turret;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.ParticleEffect;
import fr.skyforce77.towerminer.particles.ParticleType;

public class Creeper extends Turret {

    private static final long serialVersionUID = 185462L;

    public Creeper(EntityTypes type, Point location, String owner) {
        super(type, location, owner);
    }

    @Override
    public void onTick() {
        if (!(TowerMiner.menu instanceof SinglePlayer) || (TowerMiner.menu instanceof MultiPlayer && !((MultiPlayer)TowerMiner.menu).server)) {
            return;
        }
        SinglePlayer sp = (SinglePlayer) TowerMiner.menu;

        ArrayList<Mob> mobs = new ArrayList<Mob>();
        
        double distance = 99999;
        Mob e = null;
        for (Entity en : sp.mobs) {
            double i = en.getLocation().distance(getLocation().x, getLocation().y);
            if (i < distance && i < getDistance()) {
                distance = i;
                e = (Mob) en;
            }
            if (i < getDistance()) {
                mobs.add((Mob) en);
            }
        }

        if (tir >= 60 - (getLevel() * 2)) {
            if (e != null) {
                for(Mob m : mobs) {
                	onDamage(m);
                	m.hurt(1);
                }
                ParticleEffect.createParticleAlea(ParticleType.SMOKE, sp, (int) getLocation().getX(), (int) getLocation().getY(), (int)getDistance(), Color.BLACK);
                ParticleEffect.createParticleAlea(ParticleType.EXPLOSION, sp, (int) getLocation().getX(), (int) getLocation().getY(), (int)getDistance(), Color.LIGHT_GRAY);
                tir = 0;
            }
        } else {
            tir++;
        }

        setRotationAim(e);
    }

}
