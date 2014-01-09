package fr.skyforce77.towerminer.menus.additionals;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Date;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.packets.Packet11ChatMessage;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Chat {

	public boolean opened = false;
	public ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
	public ArrayList<Long> messagedate = new ArrayList<Long>();

	public Chat(final boolean server) {}

	public void onMessageReceived(ChatMessage message) {
		messages.add(message);
		messagedate.add(new Date().getTime());
	}

	public void onMessageWritten(String player, String message) {
		ChatModel name = new ChatModel(TowerMiner.player);
		if(player.equals("menu.mp.red")) {
			name.setForegroundColor(Color.RED);
		} else {
			name.setForegroundColor(Color.CYAN);
		}
		new Packet11ChatMessage(new ChatMessage(name, new ChatModel(": "+message))).sendAllTCP();
	}

	public void changeState() {
		//opened = !opened;
	}

	public void draw(Graphics2D g2d) {
		int width = 0;
		if(opened) {
			g2d.setColor(new Color(0,0,0,150));
			g2d.fillRect(0, TowerMiner.game.getHeight()-250, TowerMiner.game.getWidth()/2, TowerMiner.game.getHeight()/2);
		} else {
			int i = 0;
			int[] strings = new int[]{-1,-1,-1,-1,-1,-1,-1};
			ArrayList<Integer> remove = new ArrayList<Integer>();
			int n = messagedate.size()-1;
			while(n >= 0) {
				if(i < 6 && messagedate.get(n)+10000 > new Date().getTime()) {
					strings[i] = n;
					i++;

					int adv = getWidth(getRaw(messages.get(n)));
					if(adv > width)
						width = adv;
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
					g2d.fillRect(0, TowerMiner.game.getHeight()-i*26-55, width, 26);

					int x = 0;
					for(ChatModel model : messages.get(s).getModels()) {
						String text = LanguageManager.getText(model.getText());
						if(model.getOption() != null) {
							text = LanguageManager.getText(model.getText(), model.getOption());
						}
						
						if(model.getHighlightColor() != null) {
							Color h = model.getHighlightColor();
							if(difference < 3000) {
								g2d.setColor(new Color(h.getRed(),h.getGreen(),h.getBlue(),difference/20));
							} else {
								g2d.setColor(new Color(h.getRed(),h.getGreen(),h.getBlue(),150));
							}
							g2d.fillRect(x, TowerMiner.game.getHeight()-i*26-55, getWidth(text), 26);
						}

						g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
						FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

						drawColoredText(g2d, text, x+2, i, -2, difference, model.getBackgroundColor());
						drawColoredText(g2d, text, x, i, 0, difference, model.getForegroundColor());
						
						x = x+metrics.stringWidth(text);
					}
					i++;
				}
			}
		}
	}

	public int getWidth(String text) {
		Graphics2D g2d = (Graphics2D)TowerMiner.game.getGraphics();
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
		FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
		return metrics.stringWidth(text)+2;
	}
	
	public void drawColoredText(Graphics2D g2d, String text, int x, int i, int y, int difference, Color co) {
		if(difference < 3000) {
			g2d.setColor(new Color(co.getRed(),co.getGreen(),co.getBlue(),difference/12));
		} else {
			g2d.setColor(new Color(co.getRed(),co.getGreen(),co.getBlue(),250));
		}

		g2d.drawString(text, x, TowerMiner.game.getHeight()-(i*26)-35+y);
	}
	
	public String getRaw(ChatMessage message) {
		String s = "";
		for(ChatModel m : message.getModels()) {
			s = s+LanguageManager.getText(m.getText());
		}
		return s;
	}

}
