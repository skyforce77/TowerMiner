package fr.skyforce77.towerminer.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.multiplayer.packets.Packet10EntityValueUpdate;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class Mob extends Entity{

	private static final long serialVersionUID = -995513747690476657L;

	Point last;
	boolean isgoing = false;
	int life;
	boolean died = false;
	int speed = 1;
	int fireticks = -1;

	public Mob(EntityTypes type) {
		super(type);
		location = new Point(Maps.getActualMap().getXDepart()*MapWritter.getBlockWidth()+(MapWritter.getBlockWidth()/2),Maps.getActualMap().getYDepart()*MapWritter.getBlockHeight()+(MapWritter.getBlockHeight()/2));
		last = new Point(0,0);
		life = type.getMaxLife();
		speed = type.getSpeed();
	}

	public int getLife() {
		return life;
	}

	public boolean isFired() {
		return fireticks != -1;
	}

	public void setFired(int ticks) {
		if(fireticks < ticks) {
			fireticks = ticks;
		}
		Connect.c.sendTCP(new Packet10EntityValueUpdate(getUUID(), "fired", "true"));
	}

	@Override
	public void onTick() {
		if(fireticks > -1) {
			fireticks--;
			
			if(fireticks == -1) {
				Connect.c.sendTCP(new Packet10EntityValueUpdate(getUUID(),"fired", "false"));
			}

			int i = new Random().nextInt(100);
			if(i < 7) {
				hurt(1);
			}
		}
		if(!isgoing) {
			isgoing = true;
			Point p = getBlockToGo();
			moveTo(p);
			((SinglePlayer)TowerMiner.menu).onEntityMove(this, getLocation(), p);
		}
	}
	
	public void moveTo(final Point block) {
		new Thread(){
			@Override
			public void run() {
				Point to = block;
				last = getBlockLocation();
				while(getLocation().getX() != to.x) {
					if(getLocation().getX() < to.x) {
						move(1,0);
						setRotation(Math.toRadians(90));
					} else {
						move(-1,0);
						setRotation(Math.toRadians(270));
					}
					try {
						if(TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer)TowerMiner.menu).speed.isSelected()) {
							Thread.sleep(10/speed/2);
						} else {
							Thread.sleep(10/speed);
						}
						while(TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer)TowerMiner.menu).paused) {Thread.sleep(10l);}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				while(getLocation().getY() != to.y) {
					if(getLocation().getY() < to.y) {
						move(0,1);
						setRotation(Math.toRadians(180));
					} else {
						move(0,-1);
						setRotation(Math.toRadians(0));
					}
					try {
						if(TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer)TowerMiner.menu).speed.isSelected()) {
							Thread.sleep(10/speed/2);
						} else {
							Thread.sleep(10/speed);
						}
						while(TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer)TowerMiner.menu).paused) {Thread.sleep(10l);}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				isgoing = false;
			}
		}.start();
	}

	public void move(int x, int y) {
		this.setLocation(new Point((int)getLocation().getX()+x, (int)getLocation().getY()+y));
	}

	private Point getBlockToGo() {
		if(!(TowerMiner.menu instanceof SinglePlayer)) {
			return getLocation();
		}
		SinglePlayer sp = (SinglePlayer)TowerMiner.menu;
		ArrayList<Point> points = new ArrayList<>();
		for(Point p : MapWritter.getAroundPoints(getBlockLocation().x, getBlockLocation().y)) {
			if(!(p.x == last.x && p.y == last.y) && Maps.getActualMap().hasPoint(p) && Maps.getActualMap().getBlockId((int)p.getX(), (int)p.getY()) == Maps.getActualMap().getBlockPathId() &&
					Maps.getActualMap().getOverlayId((int)p.getX(), (int)p.getY()) == Maps.getActualMap().getOverlayPathId()) {
				points.add(new Point(p.x*MapWritter.getBlockWidth()+(MapWritter.getBlockWidth()/2),p.y*MapWritter.getBlockHeight()+(MapWritter.getBlockHeight()/2)));
			}
		}
		if(points.size() > 0) {
			if(points.size() == 1) {
				return points.get(0);
			} else {
				return points.get(new Random().nextInt(points.size()));
			}
		}
		if(!died) {
			if(!sp.multiplayer) {
				sp.vie = sp.vie - life;
			} else {
				MultiPlayer mp = (MultiPlayer)sp;
				if(mp.server) {
					if(Maps.getActualMap().getDeathPoints()[0].equals(getBlockLocation())) {
						sp.vie = sp.vie - life;
					}
					if(Maps.getActualMap().getDeathPoints()[1].equals(getBlockLocation())) {
						mp.setClientLife(mp.clientlife - life);
					}
				}
			}
		}
		if(sp.entities.contains(this)) {
			sp.removed.add(this);
		}
		died = true;
		return location;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public void hurt(int damage) {
		if(!(TowerMiner.menu instanceof SinglePlayer)) {
			return;
		}
		SinglePlayer sp = (SinglePlayer)TowerMiner.menu;
		if(damage >= life) {
			if(!died) {
				sp.removed.add(this);
				sp.addGold(getType().getMaxLife());
			}
			died = true;
		} else {
			life = life-damage;
			if(sp.multiplayer) {
				MultiPlayer mp = (MultiPlayer)sp;
				if(mp.server) {
					Connect.c.sendTCP(new Packet10EntityValueUpdate(this.getUUID(), "life", life+""));
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D g2d, SinglePlayer sp) {
		double x = getLocation().getX();
		double y = getLocation().getY();
		double ro = getRotation();
		g2d.rotate(ro, x,y+sp.CanvasY);
		try {
			g2d.drawImage(getType().getTexture(0),(int)x-((MapWritter.getBlockWidth()-10)/2)+sp.CanvasX,(int)y-((MapWritter.getBlockHeight()-10)/2)+sp.CanvasY,MapWritter.getBlockWidth()-10,MapWritter.getBlockHeight()-10,null);
		} catch (Exception e) {}
		g2d.rotate(-ro, x,y+sp.CanvasY);
		if(isFired()) {
			g2d.drawImage(RessourcesManager.getTexture("fire"),(int)x-((MapWritter.getBlockWidth()-10)/2)+sp.CanvasX,(int)y-((MapWritter.getBlockHeight()-10)/2)+sp.CanvasY,MapWritter.getBlockWidth()-10,MapWritter.getBlockHeight()-10,null);
		}
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 12));
		RenderHelper.drawLife(g2d, this);
	}

}
