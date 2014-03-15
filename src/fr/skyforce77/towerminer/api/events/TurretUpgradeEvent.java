package fr.skyforce77.towerminer.api.events;

import fr.skyforce77.towerminer.entity.Turret;

public class TurretUpgradeEvent extends TMEvent{
	
	private Turret turret;
	private int last;
	private int now;

	public TurretUpgradeEvent(Turret turret, int last, int now) {
		this.turret = turret;
	}
	
	public Turret getTurret() {
		return turret;
	}
	
	public int getLastLevel() {
		return last;
	}
	
	public int getActualLevel() {
		return now;
	}
	
}
