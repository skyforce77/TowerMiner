package fr.skyforce77.towerminer.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Vector2d;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.protocol.save.TMStorage;

public class Entity implements Serializable {

    private static final long serialVersionUID = -4582237877637415632L;
    public static ArrayList<Integer> uuidused = new ArrayList<Integer>();

    public TMStorage data;

    public Entity(EntityTypes type) {
    	data = new TMStorage();
        data.addInteger("type", type.getId());

        setUUID(-1);
        while (getUUID() == -1) {
            int id = new Random().nextInt(Integer.MAX_VALUE);
            if (!uuidused.contains(id)) {
                data.addInteger("uuid", id);
                uuidused.add(id);
            }
        }
    }

    public int getUUID() {
        return data.getInteger("uuid");
    }
    
    public void setUUID(int id) {
    	data.addInteger("uuid", id);
    }
    
    public TMStorage getData() {
        return data;
    }

    public EntityTypes getType() {
        return EntityTypes.getType(data.getInteger("type"));
    }

    public double getRotation() {
        return data.getDouble("rotation");
    }

    public void setRotation(double rotation) {
    	data.addDouble("rotation", rotation);
    }

    public void setRotationAim(Entity en) {
        Vector2d vec = new Vector2d(en.getLocation().getX() - getLocation().getX(), en.getLocation().getY() - getLocation().getY());
        double r1 = vec.angle(new Vector2d(0, -1));
        double r2 = vec.angle(new Vector2d(-1, 0));
        if (r2 <= Math.PI / 2) {
            setRotation(2 * Math.PI - r1);
        } else {
            setRotation(r1);
        }
    }

    public Point getLocation() {
        return (Point)data.getObject("location");
    }

    public Vector2d getVecLocation() {
        return new Vector2d(getLocation().getX(), getLocation().getY());
    }

    public Point getBlockLocation() {
        return new Point(getLocation().x / MapWritter.getBlockWidth() + (TowerMiner.game.CanvasX / MapWritter.getBlockWidth()), getLocation().y / MapWritter.getBlockHeight() + (TowerMiner.game.CanvasY / MapWritter.getBlockHeight()));
    }

    public void setLocation(Point location) {
    	data.addObject("location", location);
    }

    public Point getDirection() {
        Point vector = new Point();

        double rotX = this.getRotation();

        double h = Math.cos(Math.toRadians(0.0));

        vector.setLocation(-h * Math.sin(Math.toRadians(rotX)), h * Math.cos(Math.toRadians(rotX)));

        return vector;
    }

	public void onTick() {
    }

    public void draw(Graphics2D g2d, SinglePlayer sp) {
    }

    public void drawInformations(Graphics2D g2d, SinglePlayer sp) {
    }

}
