package fr.skyforce77.towerminer.menus;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.sounds.Music;

public class MenuItem {

    int id;
    String name;
    Thread t;
    int height;
    int x;
    int y;
    boolean enabled = true;

    public MenuItem(int id, String name, Thread t) {
        this.id = id;
        this.t = t;
        this.name = name;
        height = 36;
        x = 7;
        y = 175 + 70 * id;
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        Graphics2D g2d = (Graphics2D) TowerMiner.game.canvas.getGraphics();
        FontMetrics metrics = g2d.getFontMetrics(TowerMiner.getFont(28));
        int hgt = metrics.getHeight();
        int adv = metrics.stringWidth(name);
        Dimension size = new Dimension(adv + 2, hgt + 2);
        return (int) size.getWidth()+5;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getName() {
        return name;
    }

    public void start() {
        t.start();
        Music.playSound("b1",false,false);
    }

    public void run() {
        t.run();
        Music.playSound("b1",false,false);
    }

    public void disable() {
        enabled = false;
    }
}
