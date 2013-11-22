package fr.skyforce77.towerminer;

import javax.swing.JOptionPane;
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

public class TowerMiner{

	public static Game game;
	public static Menu menu;
	public static int launcherversion = 4;
	public static boolean dev = false;
	public static boolean launcherupdateneeded = true;
	public static String[] os = new String[]{"linux"};

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
				game.setVisible(true);
			}
		});
	}

	private static void MainAchievements(final String[] args) {
		new Thread() {
			@Override
			public void run() {
				if(args.length < 2) {
					if(!dev) {
						game.dispose();
						JOptionPane.showMessageDialog(game, LanguageManager.getText("launcher.without"), LanguageManager.getText("launcher.information"),JOptionPane.INFORMATION_MESSAGE);
					}
					return;
				} else {
					if(args[1].equals("offline")) {
						Game.offline = true;
						Menu.mainmenu.items[1].disable();
					}
					
					try {
						sleep(800);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if(Integer.parseInt(args[0]) != -1 && Integer.parseInt(args[0]) < launcherversion) {
					if(launcherupdateneeded) {
						game.dispose();
						JOptionPane.showMessageDialog(game, LanguageManager.getText("launcher.outdated"), LanguageManager.getText("launcher.information"),JOptionPane.ERROR_MESSAGE);
						return;
					}
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
				
				try {
					sleep(6005);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				if(args.length >= 3) {
					boolean ok = false;
					int i = 0;
					while(i < os.length) {
						if(os[i].equals(args[2])) {
							ok = true;
						}
						i++;
					}
					
					if(!ok) {
						TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.unsupported"), 6000, "warning"));
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
