package fr.skyforce77.towerminer.api.events.entity;

import fr.skyforce77.towerminer.entity.turret.Turret;

public class TurretPlacedEvent extends EntityEvent{

	public TurretPlacedEvent(Turret turret) {
		super(turret);
	}
	
	public Turret getTurret() {
		return (Turret)getEntity();
	}
	
}
