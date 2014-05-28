package fr.skyforce77.towerminer.entity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
            if (i < distance && i < this.distance && canSee((Mob)en)) {
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
            if (en instanceof Mob && i < distance && i < this.distance && canSee((Mob)en)) {
                distance = i;
                e = (Mob) en;
            }
        }
        setRotationAim(e);
    }

    public void onDamage(Mob e) {
    }
    
    public boolean canSee(Mob m) {
    	if(!m.hasEffect(EntityEffectType.INVISIBLE)) {
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
           g2d.drawImage(RenderHelper.getColoredImage(getType().getTexture(0), new Color(rgb), 0.1F), (int) x - 15 + sp.CanvasX, (int) y - 15 + sp.CanvasY, 30, 30, null);
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
        if (sp instanceof MultiPlayer) {
            text = new String[]{LanguageManager.getText("turret.level") + ": " + data, LanguageManager.getText("turret.range") + ": " + ((float) distance / 45),
                    LanguageManager.getText("turret.speed") + ": " + speed, LanguageManager.getText("turret.owner") + ": " + LanguageManager.getText(owner)};
        } else {
            text = new String[]{LanguageManager.getText("turret.level") + ": " + data, LanguageManager.getText("turret.range") + ": " + ((float) distance / 45),
                    LanguageManager.getText("turret.speed") + ": " + speed, LanguageManager.getText("turret.cost") + ": " + cost};
        }

        g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 12));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int hgt = metrics.getHeight();
        Dimension size = new Dimension(0, hgt + 2);

        for (String t : text) {
            int adv = metrics.stringWidth(t);
            if (adv > size.getWidth())
                size = new Dimension(adv + 2, hgt + 2);
        }

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect((int) x, (int) y - 64, (int) (4 + size.getWidth()), 16 * 4);
        g2d.setColor(Color.WHITE);
        g2d.drawString(text[0], (int) x + 3, (int) y - 3);
        g2d.drawString(text[1], (int) x + 3, (int) y - 3 - 16);
        g2d.drawString(text[2], (int) x + 3, (int) y - 3 - 32);
        g2d.drawString(text[3], (int) x + 3, (int) y - 3 - 48);
    }

    public EntityTypes getProjectile() {
        return EntityTypes.ARROW;
    }

}
