package fr.skyforce77.towerminer.entity.projectile;

import java.awt.Color;

import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.entity.turret.Turret;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.ParticleEffect;
import fr.skyforce77.towerminer.particles.ParticleType;

public class PotionWeakness extends EntityProjectile {

    private static final long serialVersionUID = -4837863653471130636L;
    
    public PotionWeakness(EntityTypes type, Turret shooter, Mob aimed) {
        super(type, shooter, aimed);
    }
    
    @Override
    public void onHurt(SinglePlayer sp, Mob e) {
    	ParticleEffect.createParticleSplash(ParticleType.POTION_SPLASH, sp, (int) getLocation().getX(), (int) getLocation().getY(), Color.DARK_GRAY);
    }

}
