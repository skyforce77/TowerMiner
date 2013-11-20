package fr.skyforce77.towerminer.menus.additionals;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Date;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.multiplayer.packets.Packet11ChatMessage;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Chat {

	public boolean opened = false;
	public ArrayList<String> messages = new ArrayList<>();
	public ArrayList<Long> messagedate = new ArrayList<>();

	public Chat(final boolean server) {}

	public void onMessageReceived(String message) {
		messages.add(message);
		messagedate.add(new Date().getTime());
	}

	public void onMessageWritten(String player, String message) {
		messages.add(LanguageManager.getText(player)+": "+message);
		messagedate.add(new Date().getTime());
		Connect.c.sendTCP(new Packet11ChatMessage(message, player));
	}

	public void changeState() {
		//opened = !opened;
	}

	public void draw(Graphics2D g2d) {
		if(opened) {
			g2d.setColor(new Color(0,0,0,150));
			g2d.fillRect(0, TowerMiner.game.getHeight()-250, TowerMiner.game.getWidth()/2, TowerMiner.game.getHeight()/2);
		} else {
			int i = 0;
			int[] strings = new int[]{-1,-1,-1,-1,-1,-1,-1};
			ArrayList<Integer> remove = new ArrayList<>();
			int n = messagedate.size()-1;
			while(n >= 0) {
				if(i < 6 && messagedate.get(n)+10000 > new Date().getTime()) {
					strings[i] = n;
					i++;
				} else {
					remove.add(n);
				}
				n--;
			}
			for(int s : remove) {
				messages.remove(s);
				messagedate.remove(s);
			}
			i = 0;
			for(int s : strings) {
				if(s != -1 && strings.length != 0 && messagedate.size() > s && messagedate.get(s) != null) {
					int difference = (int)((messagedate.get(s)+10000)-new Date().getTime());
					if(difference < 3000) {
						g2d.setColor(new Color(0,0,0,difference/20));
					} else {
						g2d.setColor(new Color(0,0,0,150));
					}
					g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
					g2d.fillRect(0, TowerMiner.game.getHeight()-i*26-55, TowerMiner.game.getWidth()/2, 26);
					
					if(difference < 3000) {
						g2d.setColor(new Color(250,250,250,difference/12));
					} else {
						g2d.setColor(new Color(250,250,250,250));
					}
					g2d.drawString(messages.get(s), 0, TowerMiner.game.getHeight()-(i*26)-35);
					i++;
				}
			}
		}
	}

}
