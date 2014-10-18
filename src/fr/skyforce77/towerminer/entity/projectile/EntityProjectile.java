package fr.skyforce77.towerminer.entity.projectile;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;

import javax.vecmath.Vector2d;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.entity.turret.Turret;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.protocol.packets.Packet10DataValueUpdate;

public class EntityProjectile extends Entity {

    private static final long serialVersionUID = -4837863653471130636L;

    public EntityProjectile(EntityTypes type, Turret shooter, Mob aimed) {
        super(type);
        data.addInteger("shooter", shooter.getUUID());
        data.addInteger("aimed", aimed.getUUID());
        data.addObject("last", new Vector2d(0, 0));
        setLocation(shooter.getLocation());

        Vector2d vec = new Vector2d(aimed.getLocation().getX() - getLocation().getX() + getLastPostition().x, aimed.getLocation().getY() - getLocation().getY() + getLastPostition().y);
        double r1 = vec.angle(new Vector2d(0, -1));
        double r2 = vec.angle(new Vector2d(-1, 0));
        if (r2 <= Math.PI / 2) {
            setRotation(2 * Math.PI - r1);
        } else {
            setRotation(r1);
        }
    }

    public int getShooter() {
    	return data.getInteger("shooter");
    }

    public int getAimed() {
        return data.getInteger("aimed");
    }

    public void setAimed(int entity) {
        data.addInteger("aimed", entity);
    }
    
    public Vector2d getLastPostition() {
    	return (Vector2d)data.getObject("last");
    }

    @Override
    public void onTick() {
        if (TowerMiner.menu instanceof SinglePlayer) {
            SinglePlayer sp = (SinglePlayer) TowerMiner.menu;
            Turret shooter = (Turret) sp.byUUID(getShooter());
            Entity aimed = sp.byUUID(getAimed());

            if (shooter != null && aimed != null) {
                Vector2d vec = new Vector2d(aimed.getLocation().getX() - getLocation().getX() + getLastPostition().x, aimed.getLocation().getY() - getLocation().getY() + getLastPostition().y);
                double r1 = vec.angle(new Vector2d(0, -1));
                double r2 = vec.angle(new Vector2d(-1, 0));
                if (r2 <= Math.PI / 2) {
                    setRotation(2 * Math.PI - r1);
                } else {
                    setRotation(r1);
                }

                vec.normalize();
                int nx = (int) (getLocation().getX() + vec.x * (6 + (0.2 * shooter.getLevel())));
                int ny = (int) (getLocation().getY() + vec.y * (6 + (0.2 * shooter.getLevel())));
                setLocation(new Point(nx, ny));
                data.addObject("last", vec);
            } else {
                Mob e = null;
                double distance = 48 * 5;
                for (Entity en : sp.mobs) {
                    double i = en.getLocation().distance(getLocation().x, getLocation().y);
                    if (i < distance && i < distance && shooter.canSee((Mob)en)) {
                        distance = i;
                        e = (Mob) en;
                    }
                }
                if (e != null && shooter != null) {
                    setAimed(e.getUUID());
                    if (sp instanceof MultiPlayer && ((MultiPlayer) sp).server) {
                        new Packet10DataValueUpdate(getUUID(), "aimed", getAimed()).sendAllTCP();
                    }
                } else {
                    sp.removed.add(this);
                    if(sp.draw.contains(this)) {
                    	sp.draw.remove(this);
                    }
                }
            }

            if (aimed.getLocation().distance(getLocation()) <= 32) {
                sp.removed.add(this);
                if(sp.draw.contains(this)) {
                	sp.draw.remove(this);
                }
                ((Mob) aimed).hurt(((Turret) shooter).getPower());
                ((Turret) shooter).onDamage((Mob) aimed);
                if((sp instanceof MultiPlayer && ((MultiPlayer)sp).server) || !(sp instanceof MultiPlayer))
                	onHurt(sp, (Mob)aimed);
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
            int s = 20;
            if (getType().equals(EntityTypes.FIREBALL)) {
                s = 25;
                MapWritter.drawBlock(g2d, 51, 0, (int) x - ((MapWritter.getBlockWidth() - s) / 2), (int) y - ((MapWritter.getBlockHeight() - s) / 2), 0, TowerMiner.game.CanvasY, MapWritter.getBlockWidth() - s);
            }
            g2d.drawImage(i, (int) x - ((MapWritter.getBlockWidth() - s) / 2) + sp.CanvasX, (int) y + 14 - ((MapWritter.getBlockHeight() - s) / 2) + sp.CanvasY, MapWritter.getBlockWidth() - s, MapWritter.getBlockHeight() - s, null);
        } catch (Exception e) {
        }
        if (getType().getLevel() == 1)
            g2d.rotate(-ro + Math.toRadians(getType().getRotation()), x, y + sp.CanvasY);
    }
    
    public void onHurt(SinglePlayer sp, Mob e) {}
    
    public void spawn() {
        ((SinglePlayer) TowerMiner.menu).entity.add(this);
        ((SinglePlayer) TowerMiner.menu).onEntityAdded(this);
    }

}
