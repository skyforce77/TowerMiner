package fr.skyforce77.towerminer.multiplayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import fr.skyforce77.towerminer.menus.additionals.ServerSelectionList;
import fr.skyforce77.towerminer.save.DataBase;

public class ServerInfos implements Serializable{

	private static final long serialVersionUID = -5395113644833388260L;

	public static ArrayList<ServerInfos> infos = new ArrayList<>();
	
	public String name;
	public String ip;
	public String motd = "";
	public String ping;
	public boolean online = false;
	
	public ServerInfos(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}
	
	@SuppressWarnings("unchecked")
	public static void initServerInfos() {
		Object info = DataBase.getValue("serverinfos");
		if(info != null && info instanceof ArrayList<?>) {
			infos = (ArrayList<ServerInfos>)info;
			for(ServerInfos inf : infos) {
				inf.online = false;
			}
		}
	}
	
	public static void save() {
		DataBase.setValue("serverinfos", infos);
	}
	
	public static void onInfosReceived(String name, String motd, long date) {
		for(ServerInfos inf : infos) {
			if(inf.name.equals(name)) {
				inf.motd = motd;
				inf.ping = new Date().getTime()-date+"ms";
				inf.online = true;
				ServerSelectionList.instance.update();
				ServerSelectionList.instance.list.repaint();
			}
		}
	}

}
