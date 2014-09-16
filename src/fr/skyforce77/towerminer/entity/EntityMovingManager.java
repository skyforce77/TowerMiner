package fr.skyforce77.towerminer.entity;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.menus.SinglePlayer;

import java.awt.*;
import java.util.HashMap;

public class EntityMovingManager {

    public static HashMap<Mob, Thread> move = new HashMap<Mob, Thread>();

    @SuppressWarnings("deprecation")
    public static void moveTo(final Mob m, final Point block) {
        if (move.containsKey(m))
            move.get(m).stop();
        move.put(m, new Thread("EntityMovingManager-" + m.getUUID()) {
            @Override
            public void run() {
                Point to = block;
                m.last = m.getBlockLocation();
                while (m.getLocation().getX() != to.x) {
                    if (m.getLocation().getX() < to.x) {
                        m.move(1, 0);
                        m.setRotation(Math.toRadians(90));
                    } else {
                        m.move(-1, 0);
                        m.setRotation(Math.toRadians(270));
                    }
                    try {
                        int s = 10 / m.getSpeed();
                        if (TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer) TowerMiner.menu).speed.isSelected()) {
                            s = s / 2;
                        }
                        if (m.hasEffect(EntityEffectType.SLOW)) {
                            s = s * 2;
                        }
                        while (m.hasEffect(EntityEffectType.FREEZE)) {
                            Thread.sleep(10l);
                        }
                        sleep(s);
                        while (TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer) TowerMiner.menu).paused) {
                            Thread.sleep(10l);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                while (m.getLocation().getY() != to.y) {
                    if (m.getLocation().getY() < to.y) {
                        m.move(0, 1);
                        m.setRotation(Math.toRadians(180));
                    } else {
                        m.move(0, -1);
                        m.setRotation(Math.toRadians(0));
                    }
                    try {
                        int s = 10 / m.getSpeed();
                        if (TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer) TowerMiner.menu).speed.isSelected()) {
                            s = s / 2;
                        }
                        if (m.hasEffect(EntityEffectType.SLOW)) {
                            s = s * 2;
                        }
                        while (m.hasEffect(EntityEffectType.FREEZE)) {
                            Thread.sleep(10l);
                        }
                        sleep(s);
                        while (TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer) TowerMiner.menu).paused) {
                            Thread.sleep(10l);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                m.isgoing = false;
                move.remove(m);
            }
        });
        move.get(m).start();
    }
}
