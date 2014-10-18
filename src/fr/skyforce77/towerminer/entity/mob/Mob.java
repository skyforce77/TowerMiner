package fr.skyforce77.towerminer.entity.mob;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.events.entity.MobDespawnEvent;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.effects.EntityEffect;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.protocol.packets.Packet10DataValueUpdate;
import fr.skyforce77.towerminer.render.RenderHelper;

public class Mob extends Entity {

    private static final long serialVersionUID = -995513747690476657L;

    Point last = new Point(0, 0);
    boolean isgoing = false;

    public Mob(EntityTypes type) {
        super(type);
        setLocation(new Point(Maps.getActualMap().getXDepart() * MapWritter.getBlockWidth() + (MapWritter.getBlockWidth() / 2), Maps.getActualMap().getYDepart() * MapWritter.getBlockHeight() + (MapWritter.getBlockHeight() / 2)));
        setLife(type.getMaxLife());
        setSpeed(type.getSpeed());
        setEffects(new CopyOnWriteArrayList<EntityEffect>());
        setDead(false);

        if (type.equals(EntityTypes.MUSHCOW)) {
            Achievements.unlockAchievement(8);
        }
    }

    public boolean isDead() {
        return data.getBoolean("isdead");
    }
    
    public void setDead(boolean dead) {
        data.addBoolean("isdead", dead);
    }
   
    public int getSpeed() {
        return data.getInteger("speed");
    }

    public void setSpeed(int speed) {
        data.addInteger("speed", speed);
    }
    
    public int getLife() {
        return data.getInteger("life");
    }

    public void setLife(int life) {
        data.addInteger("life", life);
    }
    
    @SuppressWarnings("unchecked")
	public CopyOnWriteArrayList<EntityEffect> getEffects() {
    	return (CopyOnWriteArrayList<EntityEffect>)data.getObject("effects");
    }
    
    public void setEffects(CopyOnWriteArrayList<EntityEffect> effects) {
    	data.addObject("effects", effects);
    }

    @Override
    public void onTick() {
        final Mob m = this;
        new Thread("MobTick-" + m.getUUID()) {
            @Override
            public void run() {
                for (EntityEffect effect : getEffects()) {
                    effect.onTick(m);
                }

                if (!isgoing) {
                    isgoing = true;
                    Point p = getBlockToGo();
                    moveTo(p);
                    ((SinglePlayer) TowerMiner.menu).onEntityMove(m, getLocation(), p);
                }
            }
        }.start();
    }

    public void moveTo(final Point block) {
        EntityMovingManager.moveTo(this, block);
    }

    public void move(int x, int y) {
        this.setLocation(new Point((int) getLocation().getX() + x, (int) getLocation().getY() + y));
    }

    private Point getBlockToGo() {
        if (!(TowerMiner.menu instanceof SinglePlayer)) {
            return getLocation();
        }
        SinglePlayer sp = (SinglePlayer) TowerMiner.menu;
        ArrayList<Point> points = new ArrayList<Point>();
        for (Point p : MapWritter.getAroundPoints(getBlockLocation().x, getBlockLocation().y)) {
            if (!(p.x == last.x && p.y == last.y) && Maps.getActualMap().hasPoint(p) && Maps.getActualMap().getBlockId((int) p.getX(), (int) p.getY()) == Maps.getActualMap().getBlockPathId() &&
                    Maps.getActualMap().getOverlayId((int) p.getX(), (int) p.getY()) == Maps.getActualMap().getOverlayPathId()) {
                points.add(new Point(p.x * MapWritter.getBlockWidth() + (MapWritter.getBlockWidth() / 2), p.y * MapWritter.getBlockHeight() + (MapWritter.getBlockHeight() / 2)));
            }
        }
        if (points.size() > 0) {
            if (points.size() == 1) {
                return points.get(0);
            } else {
                return points.get(new Random().nextInt(points.size()));
            }
        }
        if (!isDead()) {
            if (!sp.multiplayer) {
                sp.vie = sp.vie - getLife();
            } else {
                MultiPlayer mp = (MultiPlayer) sp;
                if (mp.server) {
                    if (Maps.getActualMap().getDeathPoints()[0].equals(getBlockLocation())) {
                        sp.vie = sp.vie - getLife();
                    }
                    if (Maps.getActualMap().getDeathPoints()[1].equals(getBlockLocation())) {
                        mp.setClientLife(mp.clientlife - getLife());
                    }
                }
            }
        }
        if (sp.mobs.contains(this)) {
            PluginManager.callAsyncEvent(new MobDespawnEvent(this));
            sp.removed.add(this);
        }
        setDead(true);
        return getBlockLocation();
    }

    public void hurt(int damage) {
        if (!(TowerMiner.menu instanceof SinglePlayer)) {
            return;
        }
        SinglePlayer sp = (SinglePlayer) TowerMiner.menu;
        if (damage >= getLife()) {
            if (!isDead()) {
                PluginManager.callAsyncEvent(new MobDespawnEvent(this));
                sp.removed.add(this);
                sp.addGold(getType().getMaxLife());
            }
            setDead(true);;
        } else {
            setLife(getLife() - damage);
            addEffect(new EntityEffect(EntityEffectType.HURT, 10));
            if (sp.multiplayer) {
                MultiPlayer mp = (MultiPlayer) sp;
                if (mp.server) {
                    new Packet10DataValueUpdate(this.getUUID(), "life", getLife()).sendAllTCP();
                }
            }
        }
    }

    public boolean hasEffect(EntityEffectType type) {
        for (EntityEffect effect : getEffects()) {
            if (effect.getType().getID() == type.getID()) {
                return true;
            }
        }
        return false;
    }

    public EntityEffect getEffect(EntityEffectType type) {
        for (EntityEffect effect : getEffects()) {
            if (effect.getType().getID() == type.getID()) {
                return effect;
            }
        }
        return null;
    }

    public void removeEffect(EntityEffectType type) {
        if (hasEffect(type)) {
            CopyOnWriteArrayList<EntityEffect> ef = getEffects();
            ef.remove(getEffect(type));
            setEffects(ef);
            new Packet10DataValueUpdate(getUUID(), "effects", ef).sendAllTCP();
        }
    }

    public void addEffect(EntityEffect effect) {
        boolean can = true;
        if (hasEffect(effect.getType())) {
            if (getEffect(effect.getType()).getTicks() < effect.getTicks()) {
                removeEffect(effect.getType());
            } else {
                can = false;
            }
        }

        if (can) {
        	CopyOnWriteArrayList<EntityEffect> ef = getEffects();
            ef.add(effect);
            setEffects(ef);
            new Packet10DataValueUpdate(getUUID(), "effects", ef).sendAllTCP();
        }
    }

    @Override
    public void draw(Graphics2D g2d, SinglePlayer sp) {
        double x = getLocation().getX();
        double y = getLocation().getY();
        double ro = getRotation();
        g2d.rotate(ro, x, y + sp.CanvasY);
        Image i = getType().getTexture(0);
        if (hasEffect(EntityEffectType.POISONNED)) {
            i = RenderHelper.getColoredImage(i, new Color(40, 200, 40), 0.1F);
        }
        if (hasEffect(EntityEffectType.FREEZE)) {
            i = RenderHelper.getColoredImage(i, Color.CYAN, 0.1F);
        }
        if (hasEffect(EntityEffectType.HURT)) {
            i = RenderHelper.getColoredImage(i, Color.RED, 0.5F);
        }
        if (hasEffect(EntityEffectType.INVISIBLE)) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        try {
            g2d.drawImage(i, (int) x - ((MapWritter.getBlockWidth() - 10) / 2) + sp.CanvasX, (int) y - ((MapWritter.getBlockHeight() - 10) / 2) + sp.CanvasY, MapWritter.getBlockWidth() - 10, MapWritter.getBlockHeight() - 10, null);
        } catch (Exception e) {
        }
        g2d.rotate(-ro, x, y + sp.CanvasY);
        if (hasEffect(EntityEffectType.FIRED)) {
            MapWritter.drawBlock(g2d, 51, 0, (int) x - ((MapWritter.getBlockWidth() - 10) / 2), (int) y - ((MapWritter.getBlockHeight() - 10) / 2), 0, TowerMiner.game.CanvasY, MapWritter.getBlockWidth() - 10);
        }
        g2d.setColor(Color.WHITE);
        g2d.setFont(TowerMiner.getFont(12));
        RenderHelper.drawLife(g2d, this);
    }

}
