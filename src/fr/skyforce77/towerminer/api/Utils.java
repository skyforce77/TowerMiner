package fr.skyforce77.towerminer.api;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.multiplayer.MPInfos;
import fr.skyforce77.towerminer.multiplayer.ProtocolManager;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.packets.Packet;
import fr.skyforce77.towerminer.protocol.packets.Packet11ChatMessage;

public class Utils {

	public static SinglePlayer getMultiPlayer() {
		return (SinglePlayer)TowerMiner.menu;
	}
	
	public static void write(String s) {
		getMultiPlayer().chat.onMessageReceived(new ChatMessage(s));
	}
	
	public static void write(ChatMessage m) {
		getMultiPlayer().chat.onMessageReceived(m);
	}
	
	public static void broadcast(String s) {
		sendTCP(new Packet11ChatMessage(new ChatMessage(s)));
	}
	
	public static void broadcast(ChatMessage m) {
		getMultiPlayer().chat.onMessageReceived(m);
		sendTCP(new Packet11ChatMessage(m));
	}
	
	public static void receive(Packet p) {
		ProtocolManager m = new ProtocolManager();
		m.onPacketReceived(MPInfos.connection, p);
		m.onServerReceived(MPInfos.connection, p);
		m.onClientReceived(MPInfos.connection, p);
	}
	
	public static void sendTCP(Packet p) {
		MPInfos.connection.sendTCP(p);
	}
	
	public static void sendUDP(Packet p) {
		MPInfos.connection.sendUDP(p);
	}
	
	public static void sendAllTCP(Packet p) {
		sendTCP(p);
		receive(p);
	}
	
	public static void sendAllUDP(Packet p) {
		sendUDP(p);
		receive(p);
	}
	
}
