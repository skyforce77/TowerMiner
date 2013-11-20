package fr.skyforce77.towerminer;

import javax.swing.SwingUtilities;

import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.save.DataBase;

public class TowerMiner {
	
	public static Game game;
	public static Menu menu;
	public static int launcherversion = 2;

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				LanguageManager.initLanguages();
				if(!RessourcesManager.getSaveDirectory().exists()) {
					RessourcesManager.getSaveDirectory().mkdirs();
				}
				DataBase.load();
				Maps.createMaps();
				Blocks.createNativeBlocks();
				Connect.initConnection();
				EntityTypes.createTurrets();
				Achievements.initAchievements();
				if(!RessourcesManager.getDirectory().exists()) {
					RessourcesManager.getDirectory().mkdirs();
				}
				if(!RessourcesManager.getMapsDirectory().exists()) {
					RessourcesManager.getMapsDirectory().mkdirs();
				}
				
				game = new Game();
				Menu.initMenus(game);
				setMenu(Menu.mainmenu);
				MainAchievements(args);
			}
		});
	}
	
	private static void MainAchievements(final String[] args) {
		new Thread() {
			@Override
			public void run() {
				try {
					sleep(800);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(args.length < 2 || (Integer.parseInt(args[0]) != -1 && Integer.parseInt(args[0]) < launcherversion)) {
					TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.update.needed"), 6000, "warning"));
				} else {
					if(args[1].equals("update")) {
						TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.update.client"), 6000, "warning"));
					} else if(args[1].equals("install")) {
						TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.install"), 6000));
					} else if(args[1].equals("offline")) {
						TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.offline"), 6000, "warning"));
					}
				}
			}
		}.start();
	}
	
	public static void setMenu(Menu m) {
		m.last = menu;
		if(menu != null) {
			menu.onUnused();
		}
		menu = m;
		m.onUsed();
	}
	
	public static void returnMenu(Menu m) {
		if(menu != null) {
			menu.onUnused();
		}
		menu = m;
		m.onUsed();
	}
}
