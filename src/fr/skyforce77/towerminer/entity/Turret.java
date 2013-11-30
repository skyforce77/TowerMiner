package fr.skyforce77.towerminer.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.multiplayer.packets.Packet10EntityValueUpdate;
import fr.skyforce77.towerminer.render.RenderHelper;

public class Turret extends Entity{

	private static final long serialVersionUID = 5758076475226543055L;
	
	int tir = 0;
	int data = 1;
	int distance = 90;
	int price = 20;
	int cost = 30;
	String owner = "server";

	public Turret(EntityTypes type, Point location, String owner) {
		super(type);
		this.owner = owner;
		this.location = new Point(location.x*MapWritter.getBlockWidth()+(MapWritter.getBlockWidth()/2),location.y*MapWritter.getBlockHeight()+(MapWritter.getBlockHeight()/2));
		cost = type.getPrice();
	}

	public int getData() {
		return data;
	}

	public int getPrice() {
		return price;
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public boolean isOwner(String owner) {
		return this.owner.equals(owner);
	}

	public double getDistance() {
		return distance;
	}

	public void addData() {
		if(TowerMiner.menu instanceof SinglePlayer) {
			SinglePlayer sp = (SinglePlayer)TowerMiner.menu;
			if(sp.getId().equals(owner)) {
				Achievements.unlockAchievement(1);
				if(data+1 == 10) {
					Achievements.unlockAchievement(2);
				} else if(data+1 == 15) {
					Achievements.unlockAchievement(3);
				}
			}
		}
		data++;
		distance+=10;
		cost+=price;
		price = price+(price/2);
		if(Connect.c != null) 
			Connect.c.sendTCP(new Packet10EntityValueUpdate(getUUID(), "turretdata", data));
	}

	@Override
	public void onTick() {
		if(!(TowerMiner.menu instanceof SinglePlayer)) {
			return;
		}
		SinglePlayer sp = (SinglePlayer)TowerMiner.menu;
		if(tir >= 40-(data*2)) {
			double distance = 99999;
			Mob e = null;
			for(Entity en : sp.entities) {
				double i = en.getLocation().distance(location.x, location.y);
				if(i < distance && i < this.distance) {
					distance = i;
					e = (Mob)en;
				}
			}
			if(e != null) {
				setRotationAim(e);
				sp.onEntityTeleport(this, getLocation());
				e.hurt(1);
				onDamage(e);
				tir = 0;
			}
		} else {
			tir++;
		}
	}
	
	public void onDamage(Mob e) {};
	
	@Override
	public void draw(Graphics2D g2d, SinglePlayer sp) {
		double x = getLocation().getX();
		double y = getLocation().getY();
		double ro = getRotation();
		g2d.rotate(ro, x,y+sp.CanvasY);
		try {
			if(sp instanceof MultiPlayer) {
				if(!owner.equals("server")) {
					g2d.drawImage(RenderHelper.getColoredImage(getType().getTexture(0), Color.RED, 0.1F),(int)x-15+sp.CanvasX,(int)y-15+sp.CanvasY,30,30,null);
				} else {
					g2d.drawImage(RenderHelper.getColoredImage(getType().getTexture(0), Color.BLUE, 0.1F),(int)x-15+sp.CanvasX,(int)y-15+sp.CanvasY,30,30,null);
				}
			} else {
				g2d.drawImage(getType().getTexture(0),(int)x-15+sp.CanvasX,(int)y-15+sp.CanvasY,30,30,null);
			}
		} catch (Exception e) {}
		g2d.rotate(-ro, x,y+sp.CanvasY);
	}
	
	@Override
	public void drawInformations(Graphics2D g2d, SinglePlayer sp) {
		/*double x = getLocation().getX();
		double y = getLocation().getY();
		g2d.drawString("test",(float)x,(float)y);*/
	}

}
