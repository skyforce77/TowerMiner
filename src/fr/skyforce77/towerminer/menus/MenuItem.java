package fr.skyforce77.towerminer.menus;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;

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
		y = 175+70*id;
	}
	
	public int getId() {
		return id;
	}
	
	public int getWidth() {
		Graphics2D g2d = (Graphics2D)TowerMiner.game.canvas.getGraphics();
		FontMetrics metrics = g2d.getFontMetrics(new Font("TimesRoman", Font.CENTER_BASELINE, 30));
		int hgt = metrics.getHeight();
		int adv = metrics.stringWidth(name);
		Dimension size = new Dimension(adv+2, hgt+2);
		return (int)size.getWidth()+10;
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
	}
	
	public void run() {
		t.run();
	}

	public void disable() {
		enabled = false;
	}
}
