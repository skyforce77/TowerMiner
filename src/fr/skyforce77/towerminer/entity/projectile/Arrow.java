package fr.skyforce77.towerminer.entity.projectile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.entity.turret.Turret;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleType;

public class Arrow extends EntityProjectile {

    private static final long serialVersionUID = -4837863653471130636L;

    public Arrow(EntityTypes type, Turret shooter, Mob aimed) {
        super(type, shooter, aimed);
    }

    @Override
    public void draw(Graphics2D g2d, SinglePlayer sp) {
    	super.draw(g2d, sp);
    	
        Turret shooter = (Turret) sp.byUUID(getShooter());
        if(shooter.getLevel() >= 10 && new Random().nextInt(100) > 80) {
        	Particle p = new Particle(ParticleType.CRITICAL, getLocation().x, getLocation().y, null, Color.ORANGE);
        	p.setLiveTime(30);
        	TowerMiner.game.particles.add(p);
        }

    }

}
