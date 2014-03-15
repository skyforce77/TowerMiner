package fr.skyforce77.towerminer.api.events;

import fr.skyforce77.towerminer.entity.Turret;

public class TurretPlacedEvent extends TMEvent{

	private Turret turret;

	public TurretPlacedEvent(Turret turret) {
		super();
		this.turret = turret;
	}
	
	public Turret getTurret() {
		return turret;
	}
	
}
