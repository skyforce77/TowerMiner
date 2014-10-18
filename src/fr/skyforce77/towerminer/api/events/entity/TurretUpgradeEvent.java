package fr.skyforce77.towerminer.api.events.entity;

import fr.skyforce77.towerminer.entity.turret.Turret;

public class TurretUpgradeEvent extends EntityEvent{
	
	private int last;
	private int now;

	public TurretUpgradeEvent(Turret turret, int last, int now) {
		super(turret);
	}
	
	public Turret getTurret() {
		return (Turret)getEntity();
	}
	
	public int getLastLevel() {
		return last;
	}
	
	public int getActualLevel() {
		return now;
	}
	
}
