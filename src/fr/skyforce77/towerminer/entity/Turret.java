package fr.skyforce77.towerminer.entity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.events.entity.TurretPlacedEvent;
import fr.skyforce77.towerminer.api.events.entity.TurretUpgradeEvent;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.protocol.packets.Packet10EntityValueUpdate;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Turret extends Entity {

    private static final long serialVersionUID = 5758076475226543055L;

    int tir = 0;
    int data = 1;
    int distance = 90;
    int price = 20;
    int cost = 30;
    int power = 1;
    String owner = "menu.mp.blue";
    int rgb = -1;

    public Turret(EntityTypes type, Point location, String owner) {
        super(type);
        this.owner = owner;
        this.location = new Point(location.x * MapWritter.getBlockWidth() + (MapWritter.getBlockWidth() / 2), location.y * MapWritter.getBlockHeight() + (MapWritter.getBlockHeight() / 2));
        cost = type.getPrice();
        if (TowerMiner.menu instanceof MultiPlayer) {
        	rgb = owner.equals("menu.mp.red") ? Color.RED.getRGB() : Color.BLUE.getRGB();
        }
        PluginManager.callEvent(new TurretPlacedEvent(this));
    }
    
    public void setPower(int power) {
		this.power = power;
	}
    
    public int getPower() {
		return power;
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

    public Color getColor() {
        return new Color(rgb);
    }

    public void setColor(Color color) {
        rgb = color.getRGB();
    }

    public boolean isOwner(String owner) {
        return this.owner.equals(owner);
    }

    public double getDistance() {
        return distance;
    }

    public void addData() {
        if (TowerMiner.menu instanceof SinglePlayer) {
            SinglePlayer sp = (SinglePlayer) TowerMiner.menu;
            if (sp.getPlayer().equals(owner)) {
                Achievements.unlockAchievement(1);
                if (data + 1 == 10) {
                    Achievements.unlockAchievement(2);
                } else if (data + 1 == 18) {
                    Achievements.unlockAchievement(3);
                }
            }
        }
        data++;
        distance += 10;
        cost += price;
        price = price + (price / 3);
        new Packet10EntityValueUpdate(getUUID(), "turretdata", data).sendAllTCP();
        PluginManager.callEvent(new TurretUpgradeEvent(this, data-1, data));
    }

    @Override
    public void onTick() {
        if (!(TowerMiner.menu instanceof SinglePlayer) || (TowerMiner.menu instanceof MultiPlayer && !((MultiPlayer)TowerMiner.menu).server)) {
            return;
        }
        SinglePlayer sp = (SinglePlayer) TowerMiner.menu;

        double distance = 99999;
        Mob e = null;
        for (Entity en : sp.mobs) {
            double i = en.getLocation().distance(location.x, location.y);
            if (i < distance && i < distance && canSee((Mob)en)) {
                distance = i;
                e = (Mob) en;
            }
        }

        if (tir >= 40 - (data * 2)) {
            if (e != null) {
                new EntityProjectile(getProjectile(), this, e);
                //sp.onEntityTeleport(this, getLocation());
                tir = 0;
            }
        } else {
            tir++;
        }

        setRotationAim(e);
    }
    
    public void onClientTick() {
        if (!(TowerMiner.menu instanceof SinglePlayer)) {
            return;
        }
        SinglePlayer sp = (SinglePlayer) TowerMiner.menu;

        double distance = 99999;
        Mob e = null;
        for (Entity en : sp.draw) {
            double i = en.getLocation().distance(location.x, location.y);
            if (en instanceof Mob && i < distance && canSee((Mob)en)) {
                distance = i;
                e = (Mob) en;
            }
        }
        setRotationAim(e);
    }

    public void onDamage(Mob e) {
    }
    
    public boolean canSee(Mob m) {
    	double i = m.getLocation().distance(location.x, location.y);
    	if(!m.hasEffect(EntityEffectType.INVISIBLE) && i < this.distance) {
    		return true;
    	}
    	return false;
    }

    @Override
    public void draw(Graphics2D g2d, SinglePlayer sp) {
        double x = getLocation().getX();
        double y = getLocation().getY();
        double ro = getRotation();
        g2d.rotate(ro - Math.toRadians(getType().rotation), x, y + sp.CanvasY);
        try {
        	int size = 30+(int)(0.5*data);
           g2d.drawImage(RenderHelper.getColoredImage(getType().getTexture(0), new Color(rgb), 0.1F), (int) x - size/2 + sp.CanvasX, (int) y - size/2 + sp.CanvasY, size, size, null);
        } catch (Exception e) {
        }
        g2d.rotate(-ro + Math.toRadians(getType().rotation), x, y + sp.CanvasY);
    }

    @Override
    public void drawInformations(Graphics2D g2d, SinglePlayer sp) {
        double x = sp.Xcursor;
        double y = sp.Ycursor;
        String[] text = new String[3];
        String speed = data * 2 >= 40 ? "100%" : data * 2 + "%";
        if (sp instanceof MultiPlayer && !((MultiPlayer)sp).player.equals(getOwner())) {
            text = new String[]{LanguageManager.getText("turret.level") + ": " + data, LanguageManager.getText("turret.range") + ": " + (int)((float) distance / 45),
                    LanguageManager.getText("turret.speed") + ": " + speed, LanguageManager.getText("turret.owner") + ": " + LanguageManager.getText(owner),
                    LanguageManager.getText("turret.power") + ": " + power};
        } else {
            text = new String[]{LanguageManager.getText("turret.level") + ": " + data, LanguageManager.getText("turret.range") + ": " + (int)((float) distance / 45),
                    LanguageManager.getText("turret.speed") + ": " + speed, LanguageManager.getText("turret.cost") + ": " + cost,
                    LanguageManager.getText("turret.power") + ": " + power};
        }

        g2d.setFont(TowerMiner.getFont(12));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int hgt = metrics.getHeight();
        Dimension size = new Dimension(0, hgt + 2);

        for (String t : text) {
            int adv = metrics.stringWidth(t);
            if (adv > size.getWidth())
                size = new Dimension(adv + 2, hgt + 2);
        }

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect((int) x, (int) y - 16 * text.length, (int) (4 + size.getWidth()), 16 * text.length);
        g2d.setColor(Color.WHITE);
        int u = 0;
        for (String t : text) {
        	g2d.drawString(t, (int) x + 3, (int) y - 3 - 16*u);
        	u++;
        }
    }

    public EntityTypes getProjectile() {
        return EntityTypes.ARROW;
    }
    
    public static boolean canPlace(int x, int y) {
        if (Maps.getActualMap().getBlock(x, y) == null) {
            return true;
        }
        if(!(Maps.getActualMap().getBlock(x, y).equals(Maps.getActualMap().getBlockPath()) && Maps.getActualMap().getOverlayBlock(x, y).equals(Maps.getActualMap().getOverlayPath()))) {
        	if(!(Maps.getActualMap().getBlock(x, y).isLiquid() || Maps.getActualMap().getOverlayBlock(x, y).isLiquid()) &&
        		(Maps.getActualMap().getBlock(x, y).canPlaceTurretOn() && Maps.getActualMap().getOverlayBlock(x, y).canPlaceTurretOn())) {
        		return true;
        	}
        }
        return false;
    }

}
