package fr.skyforce77.towerminer.achievements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.Date;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

public class Popup implements Serializable{

	private static final long serialVersionUID = 2485877667208754375L;
	
	public String text;
	public long time;
	public long displayed;
	public String icon;
	
	public Popup(String text, long time) {
		this.text = text;
		this.displayed = time;
	}
	
	public Popup(String text, long time, String image) {
		this.text = text;
		this.displayed = time;
		icon = image;
	}
	
	public void draw(Graphics2D g2d, int CanvasY) {
		int difference = (int)((time+displayed)-new Date().getTime());

		g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 22));
		FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
		int hgt = metrics.getHeight();
		int adv = metrics.stringWidth(text);
		Dimension size = new Dimension(adv+2, hgt+2);

		int x = (int)(TowerMiner.game.getWidth()-size.getWidth());
		int y = CanvasY;

		if(difference < size.getHeight()*10) {
			y = y-((int)size.getHeight()-difference/10);
		} else if(displayed-difference < size.getHeight()*10) {
			y = y-((int)size.getHeight()-(int)((displayed-difference)/10));
		}

		g2d.setColor(new Color(0, 0, 0, 150));
		
		if(icon == null) {
			g2d.fillRect(x-8, y, (int)size.getWidth()+10, (int)size.getHeight());
		} else {
			g2d.fillRect((int)(x-8-size.getHeight()), y, (int)(size.getWidth()+10+size.getHeight()), (int)size.getHeight());
			g2d.setColor(new Color(255,255,255,30));
			g2d.fillRect((int)(x-8-size.getHeight()), y, (int)size.getHeight(), (int)size.getHeight()-1);
			g2d.setColor(Color.BLACK);
			g2d.drawRect((int)(x-8-size.getHeight()), y, (int)size.getHeight(), (int)size.getHeight()-1);
			g2d.drawImage(RessourcesManager.getTexture(icon), (int)(x-8-size.getHeight()), y, (int)size.getHeight(), (int)size.getHeight(), null);
		}

		g2d.setColor(Color.WHITE);
		g2d.drawString(text, x-4, y+(int)size.getHeight()-4);
	}
}
