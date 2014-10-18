package fr.skyforce77.towerminer.entity.effects;

import java.util.Random;

import fr.skyforce77.towerminer.entity.mob.Mob;

public class FireEffect extends EntityEffectType {

    public FireEffect() {
        super(0);
    }

    @Override
    public void onTick(Mob m, EntityEffect e) {
        if (e.getTicks() > -1) {
            e.setTicks(e.getTicks() - 1);

            if (e.getTicks() <= -1) {
                m.removeEffect(this);
            }

            int i = new Random().nextInt(100);
            if (i < 7) {
                m.hurt(2);
            }
        }
    }

}
