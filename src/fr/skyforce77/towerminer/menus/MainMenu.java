package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.net.URL;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.menus.additionals.Chat;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class MainMenu extends Menu implements ChatContainer{

	private boolean hovertwitter = false;

	public MainMenu(final Game game) {
		super();
		chat = new Chat(this);
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
		multi.enabled = !Game.offline;

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
		g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
		g2d.setColor(new Color(0, 0, 0, 150));
		g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
		g2d.fillRect(0, 0, TowerMiner.game.getWidth() / 2, TowerMiner.game.getHeight() * 2);
		g2d.drawImage(RessourcesManager.getTexture("TowerMiner"), 0, 30, TowerMiner.game.getWidth() / 2, TowerMiner.game.getHeight() / 5, null);
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.setFont(TowerMiner.getFont(18));

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
		g2d.setFont(TowerMiner.getFont(16));
		g2d.drawString("@Skyforce77", TowerMiner.game.getWidth() - 135 - (xmove / 2), TowerMiner.game.getHeight() - 40);
		super.drawMenu(g2d);
		chat.draw(g2d, this);
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
		chat.onClick();
		super.onMouseClicked(e);
	}

	@Override
	public Chat getChat() {
		return chat;
	}
}
