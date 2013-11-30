package fr.skyforce77.towerminer.entity.effects;

import java.util.Random;

import fr.skyforce77.towerminer.entity.Mob;

public class PoisonEffect extends EntityEffectType{

	public PoisonEffect() {
		super(1);
	}
	
	@Override
	public void onTick(Mob m, EntityEffect e) {
		if(e.getTicks() > -1) {
			e.setTicks(e.getTicks()-1);
			
			if(e.getTicks() <= -1) {
				m.removeEffect(this);
			}

			int i = new Random().nextInt(100);
			if(i < 9) {
				m.hurt(new Random().nextInt(3)+1);
			}
		}
	}

}
