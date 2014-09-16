package fr.skyforce77.towerminer.entity;

import java.awt.Point;

public class WitherSkeleton extends Turret {

    private static final long serialVersionUID = 185462L;

    public WitherSkeleton(EntityTypes type, Point location, String owner) {
        super(type, location, owner);
    }

    @Override
    public boolean canSee(Mob m) {
    	double i = m.getLocation().distance(getLocation().x, getLocation().y);
    	if(i < getDistance()) {
    		return true;
    	}
    	return false;
    }

}
