package fr.skyforce77.towerminer.entity;

import java.awt.Point;

public class WitherSkeleton extends Turret {

    private static final long serialVersionUID = 185462L;

    public WitherSkeleton(EntityTypes type, Point location, String owner) {
        super(type, location, owner);
    }

    @Override
    public boolean canSee(Mob m) {
    	double i = m.getLocation().distance(location.x, location.y);
    	if(i < this.distance) {
    		return true;
    	}
    	return false;
    }

}
