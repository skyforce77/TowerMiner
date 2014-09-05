package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.multiplayer.MPInfos;
import fr.skyforce77.towerminer.protocol.Connect;
import fr.skyforce77.towerminer.protocol.Protocol;
import fr.skyforce77.towerminer.protocol.packets.Packet0Connecting;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class MPClientWait extends Menu {

	public String ip = "";
	public String text = LanguageManager.getText("menu.mp.client.search");

	public void drawMenu(Graphics2D g2d) {
		g2d.drawImage(RessourcesManager.getBackground(), 0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight(), null);
		g2d.setColor(new Color(0, 0, 0, 150));
		g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
		g2d.fillRect(0, TowerMiner.game.getHeight() / 4, TowerMiner.game.getWidth(), TowerMiner.game.getHeight() - (TowerMiner.game.getHeight() / 2));
		g2d.setFont(TowerMiner.getFont(40));
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.drawString(LanguageManager.getText("menu.multiplayer"), 10, 90 + (xmove / 5));
		g2d.setColor(Color.WHITE);
		g2d.setFont(TowerMiner.getFont(24));
		g2d.drawString(text, 0, TowerMiner.game.getHeight() / 2);
		super.drawMenu(g2d);
	}

	@Override
	public void onUsed() {
		new Thread() {
			public void run() {
				Connect.initClient();
				if (!Connect.connect(ip)) {
					text = LanguageManager.getText("menu.mp.client.fail");
					try {
						Thread.sleep(3000l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					TowerMiner.returnMenu(Menu.mpjoinmenu);
				} else {
					MPInfos.matchplaying = true;
					text = LanguageManager.getText("menu.mp.client.sending");
					new Packet0Connecting(Protocol.version, TowerMiner.player, TowerMiner.id, PluginManager.getPlugins()).sendClientTCP();
				}
			}

			;
		}.start();
	}

}
