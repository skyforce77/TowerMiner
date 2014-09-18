package fr.skyforce77.towerminer;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.commands.CommandManager;
import fr.skyforce77.towerminer.api.events.menu.MenuPreChangeEvent;
import fr.skyforce77.towerminer.api.events.menu.MenuReturnEvent;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.ChatContainer;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.multiplayer.ProtocolManager;
import fr.skyforce77.towerminer.multiplayer.ServerInfos;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.listeners.ListenersManager;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.save.DataBase;
import fr.skyforce77.towerminer.sounds.Music;

public class TowerMiner {

    public static Game game;
    public static Menu menu;
    public static int neededlauncherversion = 13;
    public static int actuallauncherversion = -1;

    public static boolean dev = true;
    public static String version = "1.0";

    public static boolean launcherupdateneeded = true;
    public static String[] os = new String[]{"linux", "windows"};
    public static String usedos = "linux";
    public static String player = "Player" + new Random().nextInt(1000);
    public static UUID id;
    public static int ldata;

    private static Font font;

    public static void startGame(final int launchedversion, final String state, final String os, String player, UUID id, int data) {
        usedos = os;
        TowerMiner.player = player;
        TowerMiner.id = id;
        actuallauncherversion = launchedversion;
        ldata = data;

        LanguageManager.initLanguages();

        if (launchedversion != -1 && launchedversion < neededlauncherversion) {
            if (launcherupdateneeded) {
                JOptionPane.showMessageDialog(game, LanguageManager.getText("launcher.outdated") + "\n" + launchedversion + " < " + neededlauncherversion, LanguageManager.getText("launcher.information"), JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Game.offline = state.equals("offline");

                if (!RessourcesManager.getSaveDirectory().exists()) {
                    RessourcesManager.getSaveDirectory().mkdirs();
                }
                printInfo("Initializing game system...");
                DataBase.load();
                Blocks.loadCustomBlocks();
                ServerInfos.initServerInfos();
                Maps.createMaps();
                CommandManager.createCommands();
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
                printInfo("Successfully initialized system!");

                printInfo("Creating menus..");
                game = new Game();
                Menu.initMenus(game);
                setMenu(Menu.mainmenu);
                MainAchievements(launchedversion, state, os);
                game.setVisible(true);
                Music.playMusic("incursion");

                PluginManager.initPlugins();
                printInfo("Successfully launched!");
                onLaunched(state);

                if(usedos.equals("linux"))
                	CommandManager.createTerminal();
            }
        });
    }

    private static void MainAchievements(final int launchedversion, final String state, final String usedos) {
        new Thread("Achievement") {
            @Override
            public void run() {
                try {
                    sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (launchedversion != -1 && launchedversion < neededlauncherversion) {
                    TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.update.needed"), 6000, "warning"));
                }

                if (state.equals("update")) {
                    TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.update.client"), 6000, "warning"));
                } else if (state.equals("install")) {
                    TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.install"), 6000));
                } else if (state.equals("offline")) {
                    TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("launcher.offline"), 6000, "warning"));
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
        MenuPreChangeEvent mpce = new MenuPreChangeEvent(menu, m);
        PluginManager.callSyncEvent(mpce);
        if (!mpce.isCancelled()) {
            m.last = menu;
            if (menu != null) {
                menu.callUnused();
                menu.selected = -1;
                m.Xcursor = menu.Xcursor;
                m.Ycursor = menu.Ycursor;
            }
            menu = mpce.getNextMenu();
            mpce.getNextMenu().callUsed();
        }
    }

    public static void returnMenu(Menu m) {
        MenuReturnEvent mpce = new MenuReturnEvent(menu, m);
        PluginManager.callSyncEvent(mpce);
        if (!mpce.isCancelled()) {
            if (menu != null) {
                menu.callUnused();
                menu.selected = -1;
                m.Xcursor = menu.Xcursor;
                m.Ycursor = menu.Ycursor;
            }
            menu = mpce.getNextMenu();
            mpce.getNextMenu().callUsed();
        }
    }

    public static void main(String[] args) {
        if (dev) {
            startGame(-1, "ok", "windows", "dev", UUID.randomUUID(), 0);
            return;
        }
        LanguageManager.initLanguages();
        JOptionPane.showMessageDialog(game, "- " + LanguageManager.getText("launcher.without") + "\n- " + LanguageManager.getText("launcher.outdated"), LanguageManager.getText("launcher.information"), JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("deprecation")
    public static void print(String message, String level) {
        Date d = new Date();
        System.out.println("[" + d.getHours() + ":" + d.getMinutes() + ":" + d.getSeconds() + "] " + "[" + level.toUpperCase() + "] " + message);
    }

    public static void printError(String error) {
        print(error, "ERROR");
        if (menu instanceof ChatContainer) {
            ChatModel head = new ChatModel("[Error] ");
            head.setForegroundColor(Color.RED);
            ChatMessage message = new ChatMessage(head, new ChatModel(error));
            ((ChatContainer) menu).getChat().onMessageReceived(message);
        }
    }

    public static void printWarning(String warning) {
        print(warning, "WARNING");
        if (menu instanceof ChatContainer) {
            ChatModel head = new ChatModel("[Warning] ");
            head.setForegroundColor(Color.ORANGE);
            ChatMessage message = new ChatMessage(head, new ChatModel(warning));
            ((ChatContainer) menu).getChat().onMessageReceived(message);
        }
    }

    public static void printInfo(String info) {
        print(info, "INFO");
    }

    public static Font getFont(int size) {
        if (font == null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, new URL("https://dl.dropboxusercontent.com/u/38885163/TowerMiner/fonts/default.ttf").openStream());
            } catch (Exception e) {
                TowerMiner.printError("Can't load font");
                font = new Font("TimesRoman", Font.BOLD, size);
            }
        }

        if (size > 22) {
            size -= 2;
        }

        return font.deriveFont(font.getStyle(), size);
    }
}
