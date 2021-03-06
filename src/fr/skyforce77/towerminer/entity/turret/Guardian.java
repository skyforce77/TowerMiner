package fr.skyforce77.towerminer.entity.turret;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Guardian extends Turret {

	private static final long serialVersionUID = 185462L;

	public Guardian(EntityTypes type, Point location, String owner) {
		super(type, location, owner);
		setAimedEntity(-1);
		data.addDouble("distance", 30.0);
	}

	@Override
	public void addLevel() {
		super.addLevel();
		setPower(getPower()+1);
		data.addDouble("distance", 30.0);
	}
	
	public int getAimedEntity() {
		return data.getInteger("aimedentity");
	}
	
	public void setAimedEntity(int entity) {
		data.addInteger("aimedentity", entity);
	}

	@Override
	public void onTick() {
		if (!(TowerMiner.menu instanceof SinglePlayer) || (TowerMiner.menu instanceof MultiPlayer && !((MultiPlayer)TowerMiner.menu).server)) {
			return;
		}
		SinglePlayer sp = (SinglePlayer) TowerMiner.menu;

		int life = 99999;
		Mob e = null;
		for (Entity en : sp.mobs) {
			int i = ((Mob)en).getLife();
			if (i < life && i < life && canSee((Mob)en)) {
				life = i;
				e = (Mob) en;
			}
		}

		if(e != null) {
			setAimedEntity(e.getUUID());
		} else {
			setAimedEntity(-1);
		}

		if (tir >= 80 - (getLevel()* 2)) {
			if (e != null) {
				e.hurt(getPower());
				onDamage(e);
				tir = 0;
			}
		} else {
			tir++;
		}

		setRotationAim(e);
	}

	public boolean canSee(Mob m) {
		for (Entity en : ((SinglePlayer)TowerMiner.menu).entity) {
			if(en instanceof Turret && !(en instanceof Guardian)) {
				Turret t = (Turret)en;
				if(t.canSee(m)) {
					return true;
				}
			}
		}
		for (Entity en : ((SinglePlayer)TowerMiner.menu).draw) {
			if(en instanceof Turret && !(en instanceof Guardian)) {
				Turret t = (Turret)en;
				if(t.canSee(m)) {
					return true;
				}
			}
		}
		return false;
	};

	@Override
	public void draw(Graphics2D g2d, SinglePlayer sp) {
		int size = 50+(int)(0.5*getLevel());
		double x = getLocation().getX();
		double y = getLocation().getY();
		double ro = getRotation();
		g2d.rotate(ro - Math.toRadians(getType().getRotation()), x, y + sp.CanvasY);
		try {
			g2d.drawImage(RenderHelper.getColoredImage(getType().getTexture(0), getColor(), 0.1F), (int) x - size/2 + sp.CanvasX, (int) y - size/2 + sp.CanvasY, size, size, null);
		} catch (Exception e) {
		}
		g2d.rotate(-ro + Math.toRadians(getType().getRotation()), x, y + sp.CanvasY);

		g2d = (Graphics2D)g2d.create();
		g2d.rotate(ro - Math.toRadians(180), x, y + sp.CanvasY);
		if(sp instanceof MultiPlayer) {
			MultiPlayer mp = (MultiPlayer)sp;
			if(!mp.server && (getAimedEntity() == -1 || sp.byUUID(getAimedEntity()) == null)) {
				double distance = 99999;
				Mob e = null;
				for (Entity en : sp.draw) {
					if(en instanceof Mob) {
						double i = en.getLocation().distance(getLocation().x, getLocation().y);
						if (i < distance && i < distance && canSee((Mob)en)) {
							distance = i;
							e = (Mob) en;
						}
					}
				}

				if(e != null) {
					setAimedEntity(e.getUUID());
				}
			}
		}
		if(getAimedEntity() != -1) {
			Entity a = sp.byUUID(getAimedEntity());
			if(a != null) {
				double i = a.getLocation().distance(getLocation().x, getLocation().y);
				int images = (int)i/32;
				while(images != 0) {
					int f = (80 - (getLevel() * 2));
					float g = 0F;
					if(tir > 0) {
						g = (float)tir/(float)f;
					}
					g2d.drawImage(RenderHelper.getColoredImage(RessourcesManager.getTexture("guardian_beam"), new Color(250, 250-(int)(240*g), 0), 1F), (int) x - 8 + sp.CanvasX, (int) y +32*images + sp.CanvasY, 16, 32, null);
					images--;
				}
			}
		}
	}
	
	@Override
    public void drawInformations(Graphics2D g2d, SinglePlayer sp) {
        double x = sp.Xcursor;
        double y = sp.Ycursor;
        String[] text = new String[3];
        if (sp instanceof MultiPlayer && !((MultiPlayer)sp).player.equals(getOwner())) {
            text = new String[]{LanguageManager.getText("turret.level") + ": " + getLevel(), LanguageManager.getText("turret.range") + ": " + (int)((float) getDistance() / 45),
                    LanguageManager.getText("turret.owner") + ": " + LanguageManager.getText(getOwner()),
                    LanguageManager.getText("turret.power") + ": " + getPower()};
        } else {
            text = new String[]{LanguageManager.getText("turret.level") + ": " + getLevel(), LanguageManager.getText("turret.range") + ": " + (int)((float) getDistance() / 45),
                    LanguageManager.getText("turret.cost") + ": " + getCost(),
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
        	g2d.drawString(t, (int) x + 3, (int) y - 3 - 16*u);
        	u++;
        }
    }

	public static boolean canPlace(int x, int y) {
		if (Maps.getActualMap().getBlock(x, y) == null) {
			return true;
		}
		if(!(Maps.getActualMap().getBlock(x, y).equals(Maps.getActualMap().getBlockPath()) && Maps.getActualMap().getOverlayBlock(x, y).equals(Maps.getActualMap().getOverlayPath()))) {
			if((Maps.getActualMap().getBlock(x, y).isLiquid() || Maps.getActualMap().getOverlayBlock(x, y).isLiquid()) &&
					!(Maps.getActualMap().getBlock(x, y).canPlaceTurretOn() && Maps.getActualMap().getOverlayBlock(x, y).canPlaceTurretOn())) {
				return true;
			}
		}
		return false;
	}

}
