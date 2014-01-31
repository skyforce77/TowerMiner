package fr.skyforce77.towerminer.entity;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleEffect;
import fr.skyforce77.towerminer.particles.ParticleType;
import fr.skyforce77.towerminer.protocol.packets.Packet10EntityValueUpdate;
import fr.skyforce77.towerminer.protocol.packets.Packet13EntityTeleport;

import javax.vecmath.Vector2d;
import java.awt.*;

public class EntityProjectile extends Entity {

    private static final long serialVersionUID = -4837863653471130636L;

    private int shooter;
    private int aimed;
    private Vector2d last = new Vector2d(0, 0);

    public EntityProjectile(EntityTypes type, Turret shooter, Mob aimed) {
        super(type);
        this.shooter = shooter.getUUID();
        this.aimed = aimed.getUUID();
        setLocation(shooter.getLocation());

        Vector2d vec = new Vector2d(aimed.getLocation().getX() - getLocation().getX() + last.x, aimed.getLocation().getY() - getLocation().getY() + last.y);
        double r1 = vec.angle(new Vector2d(0, -1));
        double r2 = vec.angle(new Vector2d(-1, 0));
        if (r2 <= Math.PI / 2) {
            setRotation(2 * Math.PI - r1);
        } else {
            setRotation(r1);
        }

        ((SinglePlayer) TowerMiner.menu).entity.add(this);
        ((SinglePlayer) TowerMiner.menu).onEntityAdded(this);
    }

    public int getShooter() {
        return shooter;
    }

    public int getAimed() {
        return aimed;
    }

    public void setAimed(int entity) {
        aimed = entity;
    }

    @Override
    public void onTick() {
        if (TowerMiner.menu instanceof SinglePlayer) {
            SinglePlayer sp = (SinglePlayer) TowerMiner.menu;
            Turret shooter = (Turret) sp.byUUID(this.shooter);
            Entity aimed = sp.byUUID(this.aimed);

            if (shooter != null && aimed != null) {
                Vector2d vec = new Vector2d(aimed.getLocation().getX() - getLocation().getX() + last.x, aimed.getLocation().getY() - getLocation().getY() + last.y);
                double r1 = vec.angle(new Vector2d(0, -1));
                double r2 = vec.angle(new Vector2d(-1, 0));
                if (r2 <= Math.PI / 2) {
                    setRotation(2 * Math.PI - r1);
                } else {
                    setRotation(r1);
                }

                vec.normalize();
                int nx = (int) (getLocation().getX() + vec.x * (6 + (0.2 * shooter.getData())));
                int ny = (int) (getLocation().getY() + vec.y * (6 + (0.2 * shooter.getData())));
                setLocation(new Point(nx, ny));
                last = vec;
                if (sp instanceof MultiPlayer && ((MultiPlayer) sp).server) {
                    Packet13EntityTeleport tp = new Packet13EntityTeleport();
                    tp.entity = getUUID();
                    tp.rotation = getRotation();
                    tp.x = (int) getLocation().getX();
                    tp.y = (int) getLocation().getY();
                    tp.sendAllTCP();
                }
            } else {
                Mob e = null;
                double distance = 48 * 5;
                for (Entity en : sp.mobs) {
                    double i = en.getLocation().distance(location.x, location.y);
                    if (i < distance && i < distance) {
                        distance = i;
                        e = (Mob) en;
                    }
                }
                if (e != null && shooter != null) {
                    this.aimed = e.getUUID();
                    if (sp instanceof MultiPlayer && ((MultiPlayer) sp).server) {
                        new Packet10EntityValueUpdate(getUUID(), "aimanother", this.aimed).sendAllTCP();
                    }
                } else {
                    sp.removed.add(this);
                }
            }

            if (aimed.getLocation().distance(getLocation()) <= 32) {
                sp.removed.add(this);
                ((Mob) aimed).hurt(1);
                ((Turret) shooter).onDamage((Mob) aimed);
                onHurt(sp);
            }
        }
    }

    @Override
    public void draw(Graphics2D g2d, SinglePlayer sp) {
        double x = getLocation().getX();
        double y = getLocation().getY();
        double ro = getRotation();
        if (getType().level == 1)
            g2d.rotate(ro - Math.toRadians(getType().rotation), x, y + sp.CanvasY);
        Image i = getType().getTexture(0);
        try {
            int s = 20;
            if (getType().equals(EntityTypes.FIREBALL)) {
                s = 25;
                MapWritter.drawBlock(g2d, 51, 0, (int) x - ((MapWritter.getBlockWidth() - s) / 2), (int) y - ((MapWritter.getBlockHeight() - s) / 2), 0, TowerMiner.game.CanvasY, MapWritter.getBlockWidth() - s);
            }
            g2d.drawImage(i, (int) x - ((MapWritter.getBlockWidth() - s) / 2) + sp.CanvasX, (int) y + 14 - ((MapWritter.getBlockHeight() - s) / 2) + sp.CanvasY, MapWritter.getBlockWidth() - s, MapWritter.getBlockHeight() - s, null);
        } catch (Exception e) {
        }
        if (getType().level == 1)
            g2d.rotate(-ro + Math.toRadians(getType().rotation), x, y + sp.CanvasY);

    }

    public void onHurt(SinglePlayer sp) {
        if (getType().equals(EntityTypes.POISON_POTION)) {
            ParticleEffect.createParticleSplash(ParticleType.POTION_SPLASH, sp, (int) getLocation().getX(), (int) getLocation().getY(), Color.GREEN);
        } else if (getType().equals(EntityTypes.WEAKNESS_POTION)) {
            ParticleEffect.createParticleSplash(ParticleType.POTION_SPLASH, sp, (int) getLocation().getX(), (int) getLocation().getY(), Color.DARK_GRAY);
        }
    }

}
