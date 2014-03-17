package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Random;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class MainMenu extends Menu {

	private boolean hovertwitter = false;

	private int[] code = new int[] {38, 38, 40, 40, 37, 39, 37, 39, 66, 65};
	private int progress = 0;
	private boolean konami = false;
	private boolean stroboscope = false;
	private int alpha = 0;
	private int konamirot = 0;

	public MainMenu(final Game game) {
		super();
		canreturn = false;

		items = new MenuItem[4];

		items[0] = new MenuItem(0, LanguageManager.getText("menu.match"), new Thread() {
			@Override
			public void run() {
				Menu.mapselmenu.setCible(Menu.singleplayer);
				TowerMiner.setMenu(Menu.mapselmenu);
			}
		});

		MenuItem multi = new MenuItem(1, LanguageManager.getText("menu.multiplayer"), new Thread() {
			@Override
			public void run() {
				TowerMiner.setMenu(Menu.mpselmenu);
			}
		});
		items[1] = multi;

		items[2] = new MenuItem(2, LanguageManager.getText("menu.editor"), new Thread() {
			@Override
			public void run() {
				TowerMiner.setMenu(Menu.mapeditor);
			}
		});

		items[3] = new MenuItem(3, LanguageManager.getText("menu.more"), new Thread() {
			@Override
			public void run() {
				TowerMiner.setMenu(Menu.more);
			}
		});
	}

	public void drawMenu(Graphics2D g2d) {
		if(stroboscope || konami)
			g2d.rotate(Math.toRadians(konamirot), TowerMiner.game.getWidth()/2, TowerMiner.game.getHeight()/2);
		g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
		g2d.setColor(new Color(0, 0, 0, 150));
		g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
		g2d.fillRect(0, 0, TowerMiner.game.getWidth() / 2, TowerMiner.game.getHeight() * 2);
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 40));
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawString(LanguageManager.getText("towerminer"), 10, 100 + (xmove / 5));
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 18));

		int xmin = TowerMiner.game.getWidth() - 135 - (xmove / 2);
		int ymin = TowerMiner.game.getHeight() - 40;
		int xmax = xmin + 130;
		int ymax = ymin + 30;

		if (Xcursor + 2 > xmin && Xcursor + 2 < xmax
				&& Ycursor + 30 > ymin && Ycursor + 30 < ymax) {
			hovertwitter = true;
		} else {
			hovertwitter = false;
		}

		if (hovertwitter)
			g2d.setColor(Color.YELLOW);
		g2d.drawString("@Skyforce77", TowerMiner.game.getWidth() - 135 - (xmove / 2), TowerMiner.game.getHeight() - 40);
		super.drawMenu(g2d);

		if(stroboscope) {
			g2d.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255), alpha));
			g2d.fillRect(-2000, -2000, 4000, 4000);
			g2d.setColor(new Color(0, 0, 0, alpha));
			g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 48));
			FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
			int hgt = metrics.getHeight();
			int adv = metrics.stringWidth("Konami");
			Dimension size = new Dimension(adv + 2, hgt + 2);
			g2d.drawString("Konami", (float)(TowerMiner.game.getWidth()/2-size.getWidth()/2), (float)(TowerMiner.game.getHeight()/2));
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onUsed() {
		TowerMiner.game.resize(800, 500);
		TowerMiner.game.setTitle(LanguageManager.getText("towerminer") + " | " + Game.version);
	}

	public void onMouseClicked(MouseEvent e) {
		if (hovertwitter) {
			Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
			if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
				try {
					desktop.browse(new URL("https://twitter.com/Skyforce77").toURI());
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		super.onMouseClicked(e);
	}

	@Override
	public void onKeyPressed(int keyCode) {
		if(keyCode == code[progress]) {
			progress++;
			if(progress == code.length) {
				progress = 0;
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(100l);
							stroboscope = true;
							int i = 0;
							while(i < 255) {
								alpha = i;
								Thread.sleep(20l);
								i++;
							}
							konami = !konami;
							int u = 0;
							while(u < 180) {
								konamirot++;
								Thread.sleep(15l);
								u++;
							}
							Thread.sleep(100l);
							while(i > 0) {
								alpha = i;
								Thread.sleep(20l);
								i--;
							}
							stroboscope = false;
						} catch (InterruptedException e) {}
					}
				}.start();
			}
		} else if(!stroboscope) {
			progress = 0;
		}
		super.onKeyPressed(keyCode);
	}
}
