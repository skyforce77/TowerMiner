package fr.skyforce77.towerminer.entity;

import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.menus.SinglePlayer;

public class Entity implements Serializable{

	private static final long serialVersionUID = -4582237877637415632L;
	public static ArrayList<Integer> uuidused = new ArrayList<>();
	
	int type;
	int uuid = -1;
	double rotation = 0.0d;
	Point location;

	public Entity(EntityTypes type) {
		this.type = type.id;
		
		while(uuid == -1) {
			int id = new Random().nextInt(100000);
			if(!uuidused.contains(id)) {
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
		Point aimed = getDirection();
		this.rotation = Math.toDegrees(Math.atan2(en.getLocation().x - location.x,en.getLocation().y - location.y)-
				Math.atan2(aimed.x- location.x,aimed.y- location.y));
	}

	public Point getLocation() {
		return location;
	}
	
	public Point getBlockLocation() {
		return new Point(location.x/MapWritter.getBlockWidth()+(TowerMiner.game.CanvasX/MapWritter.getBlockWidth()),location.y/MapWritter.getBlockHeight()+(TowerMiner.game.CanvasY/MapWritter.getBlockHeight()));
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
	
	public void onTick() {}
	
	public void draw(Graphics2D g2d, SinglePlayer sp) {}
	
	public void drawInformations(Graphics2D g2d, SinglePlayer sp) {}

}
