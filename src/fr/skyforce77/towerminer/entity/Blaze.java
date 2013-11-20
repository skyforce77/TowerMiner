package fr.skyforce77.towerminer.entity;

import java.awt.Point;

public class Blaze extends Turret{

	private static final long serialVersionUID = 185462L;

	public Blaze(EntityTypes type, Point location, String owner) {
		super(type, location, owner);
	}
	
	@Override
	public void onDamage(Mob e) {
		e.setFired(50*getData());
	}

}
