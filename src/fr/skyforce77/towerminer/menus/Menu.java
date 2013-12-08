package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.WindowEvent;
import java.util.Date;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.sounds.Music;

public class Menu {

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
	}

	public void drawMenu(Graphics2D g2d) {
		if(items != null) {
			for(MenuItem item : items) {
				if(item != null) {
					g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 30));
					g2d.setColor(Color.WHITE);
					if((Xcursor > item.getX() && Xcursor < item.getX()+item.getWidth()
							&& Ycursor > item.getY() && Ycursor < item.getY()+item.getHeight() && item.enabled) || selected == item.getId()) {
						g2d.setColor(Color.YELLOW);
						g2d.drawRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());
					}

					if(!item.isEnabled()) {
						g2d.setColor(Color.GRAY);
					}

					g2d.drawString(item.getName(), 15, 200+70*item.getId());
				}
			}
		}
		
		if(last != null && canreturn) {
			g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 14));
			FontMetrics metrics = g2d.getFontMetrics(new Font("TimesRoman", Font.CENTER_BASELINE, 14));
			int hgt = metrics.getHeight();
			int adv = metrics.stringWidth(LanguageManager.getText("menu.back"));
			Dimension size = new Dimension(adv+5, hgt+5);
			Dimension rect = new Dimension((int)size.getWidth()+3, (int)size.getHeight());
			g2d.setColor(new Color(0,0,0,150));
			g2d.fillRect((int)(TowerMiner.game.getWidth()-rect.getWidth()), (int)(TowerMiner.game.getHeight()-rect.getHeight()-29),
					(int)rect.getWidth(), (int)rect.getHeight());
			g2d.setColor(Color.WHITE);
			if(Xcursor < TowerMiner.game.getWidth() && Xcursor > TowerMiner.game.getWidth()-rect.getWidth()
					&& Ycursor < TowerMiner.game.getHeight() && Ycursor > TowerMiner.game.getHeight()-rect.getHeight()-29) {
				g2d.setColor(Color.YELLOW);
			}
			g2d.drawRect((int)(TowerMiner.game.getWidth()-rect.getWidth()), (int)(TowerMiner.game.getHeight()-rect.getHeight()-29),
					(int)rect.getWidth(), (int)rect.getHeight());
			g2d.drawString(LanguageManager.getText("menu.back"), (int)(TowerMiner.game.getWidth()-(size.getWidth()-2)), (int)(TowerMiner.game.getHeight()-30));
		}
		
		if(TowerMiner.game.popup != null && TowerMiner.game.popup.time+TowerMiner.game.popup.displayed > new Date().getTime()) {
			TowerMiner.game.popup.draw(g2d, 0);
		}
	}

	public void onKeyPressed(int keyCode) {
		if(items != null) {
			if(keyCode == KeyEvent.VK_Z || keyCode == KeyEvent.VK_UP) {
				selected = getPreviousVisibleItem(selected);
			} else if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
				selected = getNextVisibleItem(selected);
			} else if(keyCode == KeyEvent.VK_ENTER || keyCode == KeyEvent.VK_SPACE) {
				if(selected >= 0 && items[selected] != null || !items[selected].isEnabled()) {
					items[selected].run();
				}
			} else if(keyCode == KeyEvent.VK_ESCAPE && last != null) {
				TowerMiner.returnMenu(last);
			}
		}
	}

	public void onKeyReleased(int keyCode) {}

	public void onKeyTyped(char keyChar) {}

	public void onWindowActivated(WindowEvent e) {}

	public void onWindowClosed(WindowEvent e) {}

	public void onWindowClosing(WindowEvent e) {}

	public void onWindowDeactivated(WindowEvent e) {}

	public void onWindowDeiconified(WindowEvent e) {}

	public void onWindowIconified(WindowEvent e) {}

	public void onWindowOpened(WindowEvent e) {}

	public void onMouseDragged(MouseEvent e) {}

	public void onMouseMoved(MouseEvent e) {
		Xcursor = e.getX()-2;
		Ycursor = e.getY()-30;

		if(items != null)
			for(MenuItem item : items) {
				if(item != null && item.isEnabled())
					if(Xcursor > item.getX() && Xcursor < item.getX()+item.getWidth()
							&& Ycursor > item.getY() && Ycursor < item.getY()+item.getHeight()) {
						selected = -1;
					}
			}
	}

	public void onMouseClicked(MouseEvent e) {
		if(items != null) {
			for(MenuItem item : items) {
				if(item != null && item.isEnabled())
					if(Xcursor > item.getX() && Xcursor < item.getX()+item.getWidth()
							&& Ycursor > item.getY() && Ycursor < item.getY()+item.getHeight()) {
						item.run();
					}
			}
		}
		if(last != null && canreturn) {
			FontMetrics metrics = ((Graphics2D)TowerMiner.game.canvas.getGraphics()).getFontMetrics(new Font("TimesRoman", Font.CENTER_BASELINE, 14));
			int hgt = metrics.getHeight();
			int adv = metrics.stringWidth(LanguageManager.getText("menu.back"));
			Dimension size = new Dimension(adv+2, hgt+2);
			Dimension rect = new Dimension((int)size.getWidth()+3, (int)size.getHeight());
			if(Xcursor < TowerMiner.game.getWidth() && Xcursor > TowerMiner.game.getWidth()-rect.getWidth()
					&& Ycursor < TowerMiner.game.getHeight() && Ycursor > TowerMiner.game.getHeight()-rect.getHeight()-29) {
				Music.playSound("b1");
				TowerMiner.returnMenu(last);
			}
		}
	}

	public void onMouseEntered(MouseEvent e) {cursorinwindow = true;}

	public void onMouseExited(MouseEvent e) {cursorinwindow = false;}

	public void onMousePressed(MouseEvent e) {}

	public void onMouseReleased(MouseEvent e) {}

	public void onMouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() == -1) {
			onKeyPressed(KeyEvent.VK_UP);
		} else {
			onKeyPressed(KeyEvent.VK_DOWN);
		}
	}

	public void onTick() {
		if(positivemove) {
			if(xmove < 100) {
				xmove++;
			} else {
				positivemove = false;
			}
		} else {
			if(xmove > 0) {
				xmove--;
			} else {
				positivemove = true;
			}
		}
	}

	public void onUsed() {}

	public void onUnused() {}

	public int getFirstVisibleItem() {
		return getNextVisibleItem(-1);
	}

	public int getLatestVisibleItem() {
		return getPreviousVisibleItem(items.length);
	}

	public int getNextVisibleItem(int now) {
		int i = now+1;
		if(i == items.length) {
			return getFirstVisibleItem();
		}
		while(items[i] == null || !items[i].isEnabled()) {
			i++;
			if(i == items.length) {
				return getFirstVisibleItem();
			}
		}
		return i;
	}

	public int getPreviousVisibleItem(int now) {
		int i = now-1;
		while(now == -1 || now == 0 || items[i] == null || !items[i].isEnabled()) {
			i--;
			if(i == -1) {
				return getLatestVisibleItem();
			}
		}
		return i;
	}

	public void onMapChanged() {}

	public Menu getLastMenu() {
		return last;
	}

	public void setLastMenu(Menu m) {
		last = m;
	}

}
