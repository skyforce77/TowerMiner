package fr.skyforce77.towerminer.multiplayer.packets;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MPClientWait;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.multiplayer.BigSending;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Packet3Action extends Packet{
	
	public String action;
	public byte[] data;
	
	public Packet3Action() {}
	
	public Packet3Action(String action) {
		this.action = action;
	}
	
	public Packet3Action(String action, byte... data) {
		this.action = action;
		this.data = data;
	}
	
	@Override
	public void onServerReceived(Connection c) {
		if(action.equals("canstartgame")) {
			TowerMiner.setMenu(Menu.multiplayerserver);
		} else if(action.equals("ready")) {
			MultiPlayer mp = (MultiPlayer)TowerMiner.menu;
			mp.clientready = true;
			TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("menu.mp.ready", LanguageManager.getText("menu.mp.red")), 3000));
			if(mp.serverready && mp.clientready) {
				mp.serverready = false;
				mp.clientready = false;
				Connect.c.sendTCP(new Packet3Action("startround"));
				mp.started = true;
			}
		}
	}
	
	@Override
	public void onClientReceived(Connection c) {
		if(action.equals("sendingmap")) {
			Connect.c = c;
			((MPClientWait)TowerMiner.menu).text = LanguageManager.getText("menu.mp.client.map");
		} else if(action.equals("finishedsendingmap")) {
			Maps.maps[1023] = (Maps)deserialize(BigSending.receiving.get((int)data[0]).data);
			Maps.setActualMap(1023);
			c.sendTCP(new Packet3Action("canstartgame"));
			TowerMiner.setMenu(Menu.multiplayerclient);
		} else if(action.equals("startround")) {
			((MultiPlayer)TowerMiner.menu).renderarrow = false;
		}
	}

}
