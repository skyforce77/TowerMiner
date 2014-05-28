package fr.skyforce77.towerminer.api.events.entity;

import fr.skyforce77.towerminer.entity.Mob;

public class MobSpawnEvent extends EntityEvent{
	
	public MobSpawnEvent(Mob mob) {
		super(mob);
	}
	
	public Mob getMob() {
		return (Mob)getEntity();
	}
}
