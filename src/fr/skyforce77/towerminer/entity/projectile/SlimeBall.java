package fr.skyforce77.towerminer.entity.projectile;

import java.awt.Graphics2D;
import java.awt.Image;

import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.entity.turret.Turret;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.menus.SinglePlayer;

public class SlimeBall extends EntityProjectile {

    private static final long serialVersionUID = -4837863653471130636L;
    
    public SlimeBall(EntityTypes type, Turret shooter, Mob aimed) {
        super(type, shooter, aimed);
        setLevel(shooter.getLevel());
    }
    
    public SlimeBall(EntityTypes type, Turret shooter, Mob aimed, int level) {
        super(type, shooter, aimed);
        setLevel(level);
    }
    
    public void setLevel(int level) {
    	data.addInteger("level", level);
    }
    
    public int getLevel() {
    	return data.getInteger("level");
    }
    
    @Override
    public void onHurt(SinglePlayer sp, Mob e) {
    	if(getLevel() > 1) {
    		int count = 0;
            for (Entity en : sp.mobs) {
            	if(count < 2 && !en.equals(e)) {
            		SlimeBall s = new SlimeBall(getType(), (Turret)sp.byUUID(getShooter()), (Mob)en, getLevel()-1);
            		s.setLocation(getLocation());
            		s.spawn();
            	}
                count++;
            }
    	}
    }
    
    @Override
    public void draw(Graphics2D g2d, SinglePlayer sp) {
        double x = getLocation().getX();
        double y = getLocation().getY();
        double ro = getRotation();
        if (getType().getLevel() == 1)
            g2d.rotate(ro - Math.toRadians(getType().getRotation()), x, y + sp.CanvasY);
        Image i = getType().getTexture(0);
        try {
            int s = 30-getLevel();
            g2d.drawImage(i, (int) x - ((MapWritter.getBlockWidth() - s) / 2) + sp.CanvasX, (int) y + 14 - ((MapWritter.getBlockHeight() - s) / 2) + sp.CanvasY, MapWritter.getBlockWidth() - s, MapWritter.getBlockHeight() - s, null);
        } catch (Exception e) {
        }
        if (getType().getLevel() == 1)
            g2d.rotate(-ro + Math.toRadians(getType().getRotation()), x, y + sp.CanvasY);
    }

}
