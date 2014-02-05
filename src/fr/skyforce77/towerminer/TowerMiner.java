package fr.skyforce77.towerminer;

import java.net.URL;
import java.util.Random;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.multiplayer.ProtocolManager;
import fr.skyforce77.towerminer.multiplayer.ServerInfos;
import fr.skyforce77.towerminer.protocol.listeners.ListenersManager;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.save.DataBase;
import fr.skyforce77.towerminer.sounds.Music;

public class TowerMiner {

    public static Game game;
    public static Menu menu;
    public static int launcherversion = 7;
    public static boolean dev = false;
    public static boolean launcherupdateneeded = true;
    public static String[] os = new String[]{"linux"};
    public static String usedos = "linux";
    public static String player = "Player" + new Random().nextInt(1000);
    public static UUID id;

    public static void startGame(final int launchedversion, final String state, final String os, String player, UUID id) {
    	count();
        usedos = os;
        TowerMiner.player = player;
        TowerMiner.id = id;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (state.equals("offline")) {
                    Game.offline = true;
                }
                LanguageManager.initLanguages();
                if (!RessourcesManager.getSaveDirectory().exists()) {
                    RessourcesManager.getSaveDirectory().mkdirs();
                }
                DataBase.load();
                ServerInfos.initServerInfos();
                Maps.createMaps();
                Blocks.createNativeBlocks();
                ListenersManager.register(new ProtocolManager());
                EntityTypes.createTurrets();
                EntityEffectType.createEntityEffectTypes();
                Achievements.initAchievements();
                if (!RessourcesManager.getDirectory().exists()) {
                    RessourcesManager.getDirectory().mkdirs();
                }
                if (!RessourcesManager.getMapsDirectory().exists()) {
                    RessourcesManager.getMapsDirectory().mkdirs();
                }

                game = new Game();
                Menu.initMenus(game);
                setMenu(Menu.mainmenu);
                MainAchievements(launchedversion, state, os);
                game.setVisible(true);
                Music.playMusic("incursion");
            }
        });
    }

    private static void MainAchievements(final int version, final String state, final String usedos) {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                boolean popup = true;
                if (version != -1 && version < launcherversion) {
                    if (launcherupdateneeded) {
                        game.dispose();
                        JOptionPane.showMessageDialog(game, LanguageManager.getText("launcher.outdated"), LanguageManager.getText("launcher.information"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.update.needed"), 6000, "warning"));
                } else {
                    if (state.equals("update")) {
                        TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.update.client"), 6000, "warning"));
                    } else if (state.equals("install")) {
                        TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.install"), 6000));
                    } else if (state.equals("offline")) {
                        TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.offline"), 6000, "warning"));
                    } else {
                        popup = false;
                    }
                }

                try {
                    if (popup)
                        sleep(6005);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                boolean ok = false;
                int i = 0;
                while (i < os.length) {
                    if (os[i].equals(usedos)) {
                        ok = true;
                    }
                    i++;
                }

                if (!ok) {
                    TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.unsupported"), 6000, "warning"));
                }
            }
        }.start();
    }

    public static void onLaunched(String state) {
        if (state.equals("offline")) {
            Game.offline = true;
            Menu.mainmenu.items[1].disable();
        }
    }

    public static void setMenu(Menu m) {
        m.last = menu;
        if (menu != null) {
            menu.onUnused();
            menu.selected = -1;
            m.Xcursor = menu.Xcursor;
            m.Ycursor = menu.Ycursor;
        }
        menu = m;
        m.onUsed();
    }

    public static void returnMenu(Menu m) {
        if (menu != null) {
            menu.onUnused();
            menu.selected = -1;
            m.Xcursor = menu.Xcursor;
            m.Ycursor = menu.Ycursor;
        }
        menu = m;
        m.onUsed();
    }

    public static void main(String[] args) {
        if (dev) {
            startGame(-1, "ok", "linux", "dev", UUID.randomUUID());
            return;
        }
        LanguageManager.initLanguages();
        JOptionPane.showMessageDialog(game, "- " + LanguageManager.getText("launcher.without") + "\n- " + LanguageManager.getText("launcher.outdated"), LanguageManager.getText("launcher.information"), JOptionPane.ERROR_MESSAGE);
        return;
    }
    
    public static void count() {
    	try {
    		new URL("http://skyforce77.fr/count.php").openConnection().getInputStream();
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
