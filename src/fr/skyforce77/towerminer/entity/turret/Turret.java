package fr.skyforce77.towerminer.entity.turret;

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
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.entity.projectile.EntityProjectile;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Turret extends Entity {

    private static final long serialVersionUID = 5758076475226543055L;

    int tir = 0;

    public Turret(EntityTypes type, Point location, String owner) {
        super(type);
        data.addString("owner", owner);
        setLocation(new Point(location.x * MapWritter.getBlockWidth() + (MapWritter.getBlockWidth() / 2), location.y * MapWritter.getBlockHeight() + (MapWritter.getBlockHeight() / 2)));
        data.addInteger("cost", type.getPrice());
        data.addInteger("level", 1);
        data.addDouble("distance", 90.0);
        data.addInteger("price", 20);
        data.addInteger("cost", 30);
        setPower(1);
        if (TowerMiner.menu instanceof MultiPlayer) {
        	setColor(owner.equals("menu.mp.red") ? Color.RED : Color.BLUE);
        }
        PluginManager.callAsyncEvent(new TurretPlacedEvent(this));
    }

    public static boolean canPlace(int x, int y) {
        if (Maps.getActualMap().getBlock(x, y) == null) {
            return true;
        }
        if (!(Maps.getActualMap().getBlock(x, y).equals(Maps.getActualMap().getBlockPath()) && Maps.getActualMap().getOverlayBlock(x, y).equals(Maps.getActualMap().getOverlayPath()))) {
            if (!(Maps.getActualMap().getBlock(x, y).isLiquid() || Maps.getActualMap().getOverlayBlock(x, y).isLiquid()) &&
                    (Maps.getActualMap().getBlock(x, y).canPlaceTurretOn() && Maps.getActualMap().getOverlayBlock(x, y).canPlaceTurretOn())) {
                return true;
            }
        }
        return false;
    }
    
    public int getPower() {
		return data.getInteger("power");
	}

    public void setPower(int power) {
    	data.addInteger("power", power);
    }

    public int getLevel() {
        return data.getInteger("level");
    }

    public int getPrice() {
        return data.getInteger("price");
    }

    public int getCost() {
        return data.getInteger("cost");
    }

    public String getOwner() {
        return data.getString("owner");
    }

    public Color getColor() {
        return new Color(data.getInteger("color"));
    }

    public void setColor(Color color) {
        data.addInteger("color", color.getRGB());
    }

    public boolean isOwner(String owner) {
        return getOwner().equals(owner);
    }

    public double getDistance() {
        return data.getDouble("distance");
    }

    public void addLevel() {
        if (TowerMiner.menu instanceof SinglePlayer) {
            SinglePlayer sp = (SinglePlayer) TowerMiner.menu;
            if (sp.getPlayer().equals(getOwner())) {
                Achievements.unlockAchievement(1);
                if (getLevel() + 1 == 10) {
                    Achievements.unlockAchievement(2);
                } else if (getLevel() + 1 == 18) {
                    Achievements.unlockAchievement(3);
                }
            }
        }
        data.addInteger("level", data.getInteger("level")+1);
        data.addDouble("distance", data.getDouble("distance")+10.0);
        data.addInteger("cost", data.getInteger("cost")+data.getInteger("price"));
        data.addInteger("price", data.getInteger("price")+(data.getInteger("price")/3));
        if(TowerMiner.menu instanceof MultiPlayer) {
        	((MultiPlayer)TowerMiner.menu).sendData(this);
        }
        PluginManager.callAsyncEvent(new TurretUpgradeEvent(this, getLevel() - 1, getLevel()));
    }
    
    @Override
    public void onTick() {
        if (!(TowerMiner.menu instanceof SinglePlayer) || (TowerMiner.menu instanceof MultiPlayer && !((MultiPlayer) TowerMiner.menu).server)) {
            return;
        }
        SinglePlayer sp = (SinglePlayer) TowerMiner.menu;

        double distance = 99999;
        Mob e = null;
        for (Entity en : sp.mobs) {
            double i = en.getLocation().distance(getLocation().x, getLocation().y);
            if (i < distance && i < distance && canSee((Mob) en)) {
                distance = i;
                e = (Mob) en;
            }
        }

        if (tir >= 40 - (getLevel() * 2)) {
            if (e != null) {
                createProjectile(e).spawn();
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
            double i = en.getLocation().distance(getLocation().x, getLocation().y);
            if (en instanceof Mob && i < distance && canSee((Mob) en)) {
                distance = i;
                e = (Mob) en;
            }
        }
        setRotationAim(e);
    }

    public void onDamage(Mob e) {
    }

    public boolean canSee(Mob m) {
        double i = m.getLocation().distance(getLocation().x, getLocation().y);
        if (!m.hasEffect(EntityEffectType.INVISIBLE) && i < getDistance()) {
            return true;
        }
        return false;
    }

    @Override
    public void draw(Graphics2D g2d, SinglePlayer sp) {
        double x = getLocation().getX();
        double y = getLocation().getY();
        double ro = getRotation();
        g2d.rotate(ro - Math.toRadians(getType().getRotation()), x, y + sp.CanvasY);
        try {
            int size = 30 + (int) (0.5 * getLevel());
            g2d.drawImage(RenderHelper.getColoredImage(getType().getTexture(0), getColor(), 0.1F), (int) x - size / 2 + sp.CanvasX, (int) y - size / 2 + sp.CanvasY, size, size, null);
        } catch (Exception e) {
        }
        g2d.rotate(-ro + Math.toRadians(getType().getRotation()), x, y + sp.CanvasY);
    }

    @Override
    public void drawInformations(Graphics2D g2d, SinglePlayer sp) {
        double x = sp.Xcursor;
        double y = sp.Ycursor;
        String[] text = new String[3];
        String speed = getLevel() * 2 >= 40 ? "100%" : getLevel() * 2 + "%";
        if (sp instanceof MultiPlayer && !((MultiPlayer) sp).player.equals(getOwner())) {
            text = new String[]{LanguageManager.getText("turret.level") + ": " + getLevel(), LanguageManager.getText("turret.range") + ": " + (int) ((float) getDistance() / 45),
                    LanguageManager.getText("turret.speed") + ": " + speed, LanguageManager.getText("turret.owner") + ": " + LanguageManager.getText(getOwner()),
                    LanguageManager.getText("turret.power") + ": " + getPower()};
        } else {
            text = new String[]{LanguageManager.getText("turret.level") + ": " + getLevel(), LanguageManager.getText("turret.range") + ": " + (int) ((float) getDistance() / 45),
                    LanguageManager.getText("turret.speed") + ": " + speed, LanguageManager.getText("turret.cost") + ": " + getCost(),
                    LanguageManager.getText("turret.power") + ": " + getPower()};
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
            g2d.drawString(t, (int) x + 3, (int) y - 3 - 16 * u);
            u++;
        }
    }

    public EntityProjectile createProjectile(Mob m) {
    	Entity en = null;
    	try {
			en = getProjectile().getEntityClass().getConstructor(EntityTypes.class, Turret.class, Mob.class)
					.newInstance(getProjectile(), this, m);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return (EntityProjectile)en;
    }
    
    public EntityTypes getProjectile() {
    	return EntityTypes.ARROW;
    }

}
