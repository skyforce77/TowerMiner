package fr.skyforce77.towerminer.entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.effects.EntityEffect;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.protocol.packets.Packet10EntityValueUpdate;
import fr.skyforce77.towerminer.render.RenderHelper;

public class Mob extends Entity{

	private static final long serialVersionUID = -995513747690476657L;

	Point last;
	boolean isgoing = false;
	int life;
	boolean died = false;
	int speed = 1;
	CopyOnWriteArrayList<EntityEffect> effects = new CopyOnWriteArrayList<EntityEffect>();

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

	@Override
	public void onTick() {
		final Mob m = this;
		new Thread() {
			@Override
			public void run() {
				for(EntityEffect effect : effects) {
					effect.onTick(m);
				}
				
				if(!isgoing) {
					isgoing = true;
					Point p = getBlockToGo();
					moveTo(p);
					((SinglePlayer)TowerMiner.menu).onEntityMove(m, getLocation(), p);
				}
			}
		}.start();
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
						int s = 10/speed;
						if(TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer)TowerMiner.menu).speed.isSelected()) {
							s = s/2;
						}
						if(hasEffect(EntityEffectType.SLOW)) {
							s = s*2;
						}
						sleep(s);
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
		ArrayList<Point> points = new ArrayList<Point>();
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
		if(sp.mobs.contains(this)) {
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
					new Packet10EntityValueUpdate(this.getUUID(), "life", life).sendAllTCP();
				}
			}
		}
	}
	
	public boolean hasEffect(EntityEffectType type) {
		for(EntityEffect effect : effects) {
			if(effect.getType().getID() == type.getID()) {
				return true;
			}
		}
		return false;
	}
	
	public EntityEffect getEffect(EntityEffectType type) {
		for(EntityEffect effect : effects) {
			if(effect.getType().getID() == type.getID()) {
				return effect;
			}
		}
		return null;
	}
	
	public void removeEffect(EntityEffectType type) {
		if(hasEffect(type)) {
			effects.remove(getEffect(type));
			new Packet10EntityValueUpdate(getUUID(), "rmveffect", type.getID()).sendAllTCP();
		}
	}
	
	public void addEffect(EntityEffect effect) {
		boolean can = true;
		if(hasEffect(effect.getType())) {
			if(getEffect(effect.getType()).getTicks() < effect.getTicks()){
				removeEffect(effect.getType());
			} else {
				can = false;
			}
		}
		
		if(can) {
			effects.add(effect);
			new Packet10EntityValueUpdate(getUUID(), "addeffect", effect).sendAllTCP();
		}
	}

	@Override
	public void draw(Graphics2D g2d, SinglePlayer sp) {
		double x = getLocation().getX();
		double y = getLocation().getY();
		double ro = getRotation();
		g2d.rotate(ro, x,y+sp.CanvasY);
		Image i = getType().getTexture(0);
		if(hasEffect(EntityEffectType.POISONNED)) {
			i = RenderHelper.getColoredImage(i, new Color(40, 200, 40), 0.1F);
		}
		try {
			g2d.drawImage(i,(int)x-((MapWritter.getBlockWidth()-10)/2)+sp.CanvasX,(int)y-((MapWritter.getBlockHeight()-10)/2)+sp.CanvasY,MapWritter.getBlockWidth()-10,MapWritter.getBlockHeight()-10,null);
		} catch (Exception e) {}
		g2d.rotate(-ro, x,y+sp.CanvasY);
		if(hasEffect(EntityEffectType.FIRED)) {
			MapWritter.drawBlock(g2d, 51, 0,(int)x-((MapWritter.getBlockWidth()-10)/2),(int)y-((MapWritter.getBlockHeight()-10)/2), 0, TowerMiner.game.CanvasY, MapWritter.getBlockWidth()-10);
		}
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 12));
		RenderHelper.drawLife(g2d, this);
	}

}
