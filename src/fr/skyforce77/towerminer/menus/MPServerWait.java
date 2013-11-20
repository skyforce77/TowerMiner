package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class MPServerWait extends Menu{

	public String text = LanguageManager.getText("menu.mp.server.wait");
	
	public void drawMenu(Graphics2D g2d) {
		g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
		g2d.setColor(new Color(0, 0, 0, 150));
		g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
		g2d.fillRect(0, TowerMiner.game.getHeight()/4, TowerMiner.game.getWidth(), TowerMiner.game.getHeight()-(TowerMiner.game.getHeight()/2));
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 40));
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawString(LanguageManager.getText("menu.multiplayer"), 10, 90+(xmove/5));
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 24));
		g2d.drawString(LanguageManager.getText("menu.mp.server.ip")+":"+Connect.getLocalIp(), 0, TowerMiner.game.getHeight()/2-50);
		g2d.drawString(text, 0, TowerMiner.game.getHeight()/2+50);
		super.drawMenu(g2d);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onUsed() {
		Connect.initServer();
		TowerMiner.game.resize(800, 500);
	}

}