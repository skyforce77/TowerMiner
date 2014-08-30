package fr.skyforce77.towerminer.achievements;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Date;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.MultiPlayer;

public class ServerPopup extends Popup {

	private static final long serialVersionUID = -3665505443733804552L;
	public String[] description;

	public ServerPopup(String server, String[] description, long time, Image image) {
		super(server, time, image);
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public String[] getDesc() {
		return description;
	}

	@Override
	public void draw(Graphics2D g2d, int CanvasY) {
		if(!(TowerMiner.menu instanceof MultiPlayer)) {
			return;
		}
		
		int difference = (int) ((time + displayed) - new Date().getTime());

		g2d.setFont(TowerMiner.getFont(26));
		FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
		int hgt = metrics.getHeight();
		int adv = metrics.stringWidth(text);

		for(String s : description) {
			if(g2d.getFontMetrics(TowerMiner.getFont(20)).stringWidth(s) > adv) {
				adv = g2d.getFontMetrics(TowerMiner.getFont(20)).stringWidth(s);
			}
		}

		Dimension size = new Dimension(adv + 2, hgt + 2);

		double height = size.getHeight()+10;
		if(description.length > 0) {
			height = height + description.length*height;
		}

		int x = (int) (TowerMiner.game.getWidth()/2 - size.getWidth()/2);
		int y = CanvasY;

		g2d.setColor(new Color(0, 0, 0, 150));

		g2d.fillRect((int) (x - 8 - size.getHeight()/2), y, (int) (size.getWidth() + 10 + size.getHeight()), (int) height);
		g2d.setColor(new Color(255, 255, 255, 30));
		g2d.fillRect((int) (x - 8 - size.getHeight()/2), y, (int) size.getHeight(), (int) size.getHeight() - 1);
		g2d.setColor(Color.BLACK);
		g2d.drawRect((int) (x - 8 - size.getHeight()/2), y, (int) size.getHeight(), (int) size.getHeight() - 1);
		g2d.drawImage(icon, (int) (x - 8 - size.getHeight()/2), y, (int) size.getHeight(), (int) size.getHeight(), null);

		float f = (float)difference/displayed;
		f = 1-f;
		g2d.setColor(Color.GREEN);
		g2d.fillRect((int) (x - 8 - size.getHeight()/2), (int) (y+height-10), (int) ((size.getWidth() + 10 + size.getHeight())*f), (int) 10);
		
		g2d.setColor(Color.ORANGE);
		g2d.drawString(text, (int) (x - 4 + size.getHeight()/2), y + (int) size.getHeight() - 4);
		g2d.setColor(Color.WHITE);
		g2d.drawRect((int) (x - 8 - size.getHeight()/2), (int) (y+height-10), (int) (size.getWidth() + 10 + size.getHeight()), (int) 10);

		g2d.setFont(TowerMiner.getFont(20));
		int i = 2;
		for(String s : description) {
			g2d.drawString(s, (int) (x - 4 + size.getHeight()/2), y + (int) (i*size.getHeight() - 4));
			i++;
		}
	}
}
