package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JRadioButton;
import javax.swing.JTextField;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.events.menu.MenuUnusedEvent;
import fr.skyforce77.towerminer.api.events.menu.MenuUsedEvent;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.menus.additionals.Chat;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.sounds.Music;

public class Menu {

    public static MainMenu mainmenu;
    public static MapEditor mapeditor;
    public static MapSelectionMenu mapselmenu;
    public static MPSelectionMenu mpselmenu;
    public static MPJoinMenu mpjoinmenu;
    public static MPServerWait mpservwait;
    public static MPClientWait mpclientwait;
    public static SinglePlayer singleplayer;
    public static MultiPlayer multiplayerserver;
    public static MultiPlayer multiplayerclient;
    public static OptionsMenu options;
    public static MoreMenu more;
    public static AboutMenu about;
    public static VolumeControlMenu volumecontrol;
    public static AchievementsMenu achievements;
    public static ServerPropertiesMenu serverprops;
    public MenuItem[] items = null;
    public int selected = -1;
    public int xmove = 0;
    public boolean positivemove = true;
    public int Xcursor = 0;
    public
    int Ycursor = 0;
    public boolean cursorinwindow = true;
    public Menu last = null;
    public boolean canreturn = true;
    public Chat chat;
    public JTextField chatfield;
    public JRadioButton enablechat;
    public CopyOnWriteArrayList<String> typed = new CopyOnWriteArrayList<String>();
    public int select = 0;

    public static void initMenus(Game game) {
        mainmenu = new MainMenu(game);
        mapeditor = new MapEditor();
        mapselmenu = new MapSelectionMenu();
        mpselmenu = new MPSelectionMenu();
        mpjoinmenu = new MPJoinMenu();
        mpservwait = new MPServerWait();
        mpclientwait = new MPClientWait();
        singleplayer = new SinglePlayer(false);
        multiplayerserver = new MultiPlayer(true);
        multiplayerclient = new MultiPlayer(false);
        options = new OptionsMenu();
        more = new MoreMenu();
        about = new AboutMenu();
        volumecontrol = new VolumeControlMenu();
        achievements = new AchievementsMenu();
        serverprops = new ServerPropertiesMenu();
    }

    public void drawMenu(Graphics2D g2d) {
        if (items != null) {
        	boolean hover = false;
            for (MenuItem item : items) {
                if (item != null) {
                    g2d.setFont(TowerMiner.getFont(26));
                    if ((Xcursor > item.getX() && Xcursor < item.getX() + item.getWidth()
                            && Ycursor > item.getY() && Ycursor < item.getY() + item.getHeight() && item.enabled) || selected == item.getId()) {
                        g2d.setColor(new Color(250, 250, 0, 20));
                        g2d.fillRoundRect(item.getX(), item.getY(), item.getWidth(), item.getHeight(), 5, 5);
                        g2d.setColor(Color.YELLOW);
                        g2d.drawRoundRect(item.getX(), item.getY(), item.getWidth(), item.getHeight(), 5, 5);
                        hover = true;
                    } else {
                    	g2d.setColor(new Color(250, 250, 250, 20));
                        g2d.fillRoundRect(item.getX(), item.getY(), item.getWidth(), item.getHeight(), 5, 5);
                        g2d.setColor(new Color(250, 250, 250, 30));
                        g2d.drawRoundRect(item.getX(), item.getY(), item.getWidth(), item.getHeight(), 5, 5);
                        g2d.setColor(Color.WHITE);
                    }

                    if (!item.isEnabled()) {
                        g2d.setColor(Color.GRAY);
                    }

                    g2d.drawString(item.getName(), 15, 204 + 70 * item.getId());
                }
            }
    		if(hover) {
    			TowerMiner.game.setCursor(new Cursor(Cursor.HAND_CURSOR));
    		} else {
    			TowerMiner.game.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    		}
        }

        if (last != null && canreturn) {
            g2d.setFont(TowerMiner.getFont(14));
            FontMetrics metrics = g2d.getFontMetrics(TowerMiner.getFont(14));
            int hgt = metrics.getHeight();
            int adv = metrics.stringWidth(LanguageManager.getText("menu.back"));
            Dimension size = new Dimension(adv + 5, hgt + 5);
            Dimension rect = new Dimension((int) size.getWidth() + 3, (int) size.getHeight());
            
            if (Xcursor < TowerMiner.game.getWidth() && Xcursor > TowerMiner.game.getWidth() - rect.getWidth()
                    && Ycursor < TowerMiner.game.getHeight() && Ycursor > TowerMiner.game.getHeight() - rect.getHeight() - 29) {
                g2d.setColor(new Color(250, 250, 0, 20));
                g2d.fillRoundRect((int) (TowerMiner.game.getWidth() - rect.getWidth()), (int) (TowerMiner.game.getHeight() - rect.getHeight() - 29),
                        (int) rect.getWidth(), (int) rect.getHeight(), 5, 5);
                g2d.setColor(Color.YELLOW);
                g2d.drawRoundRect((int) (TowerMiner.game.getWidth() - rect.getWidth()), (int) (TowerMiner.game.getHeight() - rect.getHeight() - 29),
                        (int) rect.getWidth(), (int) rect.getHeight(), 5, 5);
            } else {
            	g2d.setColor(new Color(250, 250, 250, 20));
                g2d.fillRoundRect((int) (TowerMiner.game.getWidth() - rect.getWidth()), (int) (TowerMiner.game.getHeight() - rect.getHeight() - 29),
                        (int) rect.getWidth(), (int) rect.getHeight(), 5, 5);
                g2d.setColor(new Color(250, 250, 250, 30));
                g2d.drawRoundRect((int) (TowerMiner.game.getWidth() - rect.getWidth()), (int) (TowerMiner.game.getHeight() - rect.getHeight() - 29),
                        (int) rect.getWidth(), (int) rect.getHeight(), 5, 5);
                g2d.setColor(Color.WHITE);
            }

            g2d.drawString(LanguageManager.getText("menu.back"), (int) (TowerMiner.game.getWidth() - (size.getWidth() - 2)), (int) (TowerMiner.game.getHeight() - 35));
        }

        if (TowerMiner.game.popup != null && TowerMiner.game.popup.time + TowerMiner.game.popup.displayed > new Date().getTime()) {
            TowerMiner.game.popup.draw(g2d, 0);
        }
    }

    public void onKeyPressed(int keyCode) {
        if (items != null) {
            if (keyCode == KeyEvent.VK_Z || keyCode == KeyEvent.VK_UP) {
                selected = getPreviousVisibleItem(selected);
            } else if (keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
                selected = getNextVisibleItem(selected);
            } else if (keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
                if (selected >= 0 && items[selected] != null || !items[selected].isEnabled()) {
                    items[selected].run();
                }
            } else if (keyCode == KeyEvent.VK_ESCAPE && last != null && !(this instanceof SinglePlayer)) {
                TowerMiner.returnMenu(last);
            }
        }
    }

    public void onKeyReleased(int keyCode) {
    }

    public void onKeyTyped(char keyChar) {
    }

    public void onWindowActivated(WindowEvent e) {
    }

    public void onWindowClosed(WindowEvent e) {
    }

    public void onWindowClosing(WindowEvent e) {
    }

    public void onWindowDeactivated(WindowEvent e) {
    }

    public void onWindowDeiconified(WindowEvent e) {
    }

    public void onWindowIconified(WindowEvent e) {
    }

    public void onWindowOpened(WindowEvent e) {
    }

    public void onMouseDragged(MouseEvent e) {
    	Xcursor = e.getX() - 2;
        Ycursor = e.getY() - 30;
    }

    public void onMouseMoved(MouseEvent e) {
        Xcursor = e.getX() - 2;
        Ycursor = e.getY() - 30;

        if (items != null)
            for (MenuItem item : items) {
                if (item != null && item.isEnabled())
                    if (Xcursor > item.getX() && Xcursor < item.getX() + item.getWidth()
                            && Ycursor > item.getY() && Ycursor < item.getY() + item.getHeight()) {
                        selected = -1;
                    }
            }
    }

    public void onMouseClicked(MouseEvent e) {
        if (items != null) {
            for (MenuItem item : items) {
                if (item != null && item.isEnabled())
                    if (Xcursor > item.getX() && Xcursor < item.getX() + item.getWidth()
                            && Ycursor > item.getY() && Ycursor < item.getY() + item.getHeight()) {
                        item.run();
                    }
            }
        }
        if (last != null && canreturn) {
            FontMetrics metrics = ((Graphics2D) TowerMiner.game.canvas.getGraphics()).getFontMetrics(TowerMiner.getFont(14));
            int hgt = metrics.getHeight();
            int adv = metrics.stringWidth(LanguageManager.getText("menu.back"));
            Dimension size = new Dimension(adv + 2, hgt + 2);
            Dimension rect = new Dimension((int) size.getWidth() + 3, (int) size.getHeight());
            if (Xcursor < TowerMiner.game.getWidth() && Xcursor > TowerMiner.game.getWidth() - rect.getWidth()
                    && Ycursor < TowerMiner.game.getHeight() && Ycursor > TowerMiner.game.getHeight() - rect.getHeight() - 29) {
                Music.playSound("b1");
                TowerMiner.returnMenu(last);
            }
        }
    }

    public void onMouseEntered(MouseEvent e) {
        cursorinwindow = true;
    }

    public void onMouseExited(MouseEvent e) {
        cursorinwindow = false;
    }

    public void onMousePressed(MouseEvent e) {
    }

    public void onMouseReleased(MouseEvent e) {
    }

    public void onMouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() == -1) {
            onKeyPressed(KeyEvent.VK_UP);
        } else {
            onKeyPressed(KeyEvent.VK_DOWN);
        }
    }

    public void onTick() {
        if (positivemove) {
            if (xmove < 100) {
                xmove++;
            } else {
                positivemove = false;
            }
        } else {
            if (xmove > 0) {
                xmove--;
            } else {
                positivemove = true;
            }
        }
    }

    public void callUsed() {
        PluginManager.callAsyncEvent(new MenuUsedEvent(this));
        onUsed();
    }
    
    public void callUnused() {
        PluginManager.callAsyncEvent(new MenuUnusedEvent(this));
        onUnused();
    }
    
    public void onUsed() {
    }

    public void onUnused() {
    	TowerMiner.game.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    public int getFirstVisibleItem() {
        return getNextVisibleItem(-1);
    }

    public int getLatestVisibleItem() {
        return getPreviousVisibleItem(items.length);
    }

    public int getNextVisibleItem(int now) {
        int i = now + 1;
        if (i == items.length) {
            return getFirstVisibleItem();
        }
        while (items[i] == null || !items[i].isEnabled()) {
            i++;
            if (i == items.length) {
                return getFirstVisibleItem();
            }
        }
        return i;
    }

    public int getPreviousVisibleItem(int now) {
        int i = now - 1;
        while (now == -1 || now == 0 || items[i] == null || !items[i].isEnabled()) {
            i--;
            if (i == -1) {
                return getLatestVisibleItem();
            }
        }
        return i;
    }

    public void onMapChanged() {
    }

    public Menu getLastMenu() {
        return last;
    }

    public void setLastMenu(Menu m) {
        last = m;
    }

}
