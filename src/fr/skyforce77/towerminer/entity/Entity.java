package fr.skyforce77.towerminer.entity;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.menus.SinglePlayer;

import javax.vecmath.Vector2d;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Entity implements Serializable {

    private static final long serialVersionUID = -4582237877637415632L;
    public static ArrayList<Integer> uuidused = new ArrayList<Integer>();

    int type;
    public int uuid = -1;
    double rotation = 0.0d;
    Point location;

    public Entity(EntityTypes type) {
        this.type = type.id;

        while (uuid == -1) {
            int id = new Random().nextInt(Integer.MAX_VALUE);
            if (!uuidused.contains(id)) {
                uuid = id;
            }
        }
    }

    public int getUUID() {
        return uuid;
    }

    public EntityTypes getType() {
        return EntityTypes.getType(type);
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
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
        return location;
    }

    public Vector2d getVecLocation() {
        return new Vector2d(location.getX(), location.getY());
    }

    public Point getBlockLocation() {
        return new Point(location.x / MapWritter.getBlockWidth() + (TowerMiner.game.CanvasX / MapWritter.getBlockWidth()), location.y / MapWritter.getBlockHeight() + (TowerMiner.game.CanvasY / MapWritter.getBlockHeight()));
    }

    public void setLocation(Point location) {
        this.location = location;
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
