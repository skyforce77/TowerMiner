package fr.skyforce77.towerminer.api.events.entity;

import fr.skyforce77.towerminer.entity.Mob;

public class MobDespawnEvent extends EntityEvent{
	
	public MobDespawnEvent(Mob mob) {
		super(mob);
	}
	
	public Mob getMob() {
		return (Mob)getEntity();
	}
}
