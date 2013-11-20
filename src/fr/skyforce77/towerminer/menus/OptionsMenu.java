package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.additionals.LanguageSelectionList;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class OptionsMenu extends Menu {

	public OptionsMenu() {
		super();
		
		items = new MenuItem[4];
		
		items[0] = new MenuItem(0, LanguageManager.getText("menu.languages"), new Thread(){
			@Override
			public void run() {
				new LanguageSelectionList();
			}
		});
	}
	
	public void drawMenu(Graphics2D g2d) {
		g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
		g2d.setColor(new Color(0, 0, 0, 150));
		g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
		g2d.fillRect(0, 0, TowerMiner.game.getWidth()/2, TowerMiner.game.getHeight()*2);
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 40));
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawString(LanguageManager.getText("menu.options"), 10, 100+(xmove/5));
		super.drawMenu(g2d);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onUsed() {
		TowerMiner.game.resize(800, 500);
		TowerMiner.game.setTitle(LanguageManager.getText("towerminer")+" | "+TowerMiner.game.version);
	}
}
