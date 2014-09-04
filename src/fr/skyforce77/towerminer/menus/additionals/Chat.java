package fr.skyforce77.towerminer.menus.additionals;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Date;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.commands.CommandManager;
import fr.skyforce77.towerminer.api.commands.CommandSender;
import fr.skyforce77.towerminer.api.events.chat.PlayerChatEvent;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.packets.Packet11ChatMessage;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Chat {

	public boolean opened = false;
	public ArrayList<ChatMessage> messages = new ArrayList<ChatMessage>();
	public ArrayList<Long> messagedate = new ArrayList<Long>();
	public int max = 8;
	public Menu menu;

	public Chat(Menu menu) {
		this.menu = menu;
	}

	public void onMessageReceived(ChatMessage message) {
		if(message.toString().startsWith("/"))
			return;
		messages.add(message);
		messagedate.add(new Date().getTime());
		TowerMiner.print(message.toString(), "chat");
		
		int h = 0;
		while(messagedate.size() > max) {
			if(messagedate.get(h) != null) {
				messagedate.remove(h);
				messages.remove(h);
			}
			h++;
		}
	}

	public void onMessageWritten(String player, String message) {
		if(message.startsWith("/")) {
			if(TowerMiner.menu instanceof MultiPlayer) {
				new Packet11ChatMessage(new ChatMessage(message)).sendAllTCP();
			} else {
				String label = message.replaceFirst("/", "").split(" ")[0];
				CommandManager.onCommandTyped(new CommandSender(), label, message.replaceFirst("/"+label, "").replaceFirst(" ", "").split(" "));
			}
			return;
		}
		
		ChatModel name = new ChatModel(TowerMiner.player);
		if (player.equals("menu.mp.red")) {
			name.setForegroundColor(Color.RED);
		} else {
			name.setForegroundColor(Color.CYAN);
		}

		ChatMessage msg = new ChatMessage(name, new ChatModel(": " + message));
		PlayerChatEvent pce = new PlayerChatEvent(msg, message);
		PluginManager.callEvent(pce);
		if(!pce.isCancelled()) {
			if(menu instanceof MultiPlayer) {
				new Packet11ChatMessage(pce.getMessage()).sendAllTCP();
			} else {
				onMessageReceived(pce.getMessage());
			}
		}
	}

	public void changeState(boolean state) {
		opened = state;
	}

	public void draw(final Graphics2D g2d, final Menu mp) {
		int width = 0;
		int i = max-1;
		int[] strings = new int[max];
		int g = 0;
		while(g <= i) {
			strings[g] = -1;
			g++;
		}
		int n = messagedate.size() - 1;
		while (n >= 0) {
			if (i >= 0 && (messagedate.get(n) + 10000 > new Date().getTime() || opened)) {
				strings[i] = n;
				i--;

				int adv = getWidth(getRaw(messages.get(n)));
				if (adv > width)
					width = adv;
			}
			n--;
		}
		i = max-1;
		Thread render = null;
		for (int s : strings) {
			if (s != -1 && strings.length != 0 && (messagedate.size() > s || opened) && messagedate.get(s) != null) {
				int difference = (int) ((messagedate.get(s) + 10000) - new Date().getTime());
				if(opened)
					difference = 3000;
				if (difference < 3000) {
					g2d.setColor(new Color(0, 0, 0, difference / 20));
				} else {
					g2d.setColor(new Color(0, 0, 0, 150));
				}
				g2d.fillRect(0, TowerMiner.game.getHeight() - i * 26 - 55, width, 26);

				int x = 0;
				for (final ChatModel model : messages.get(s).getModels()) {
					String text = LanguageManager.getText(model.getText());
					if (model.getOption() != null) {
						text = LanguageManager.getText(model.getText(), model.getOption());
					}

					if (model.getHighlightColor() != null) {
						Color h = model.getHighlightColor();
						if (difference < 3000) {
							g2d.setColor(new Color(h.getRed(), h.getGreen(), h.getBlue(), difference / 20));
						} else {
							g2d.setColor(new Color(h.getRed(), h.getGreen(), h.getBlue(), 150));
						}
						g2d.fillRect(x, TowerMiner.game.getHeight() - i * 26 - 55, getWidth(text), 26);
					}

					g2d.setFont(TowerMiner.getFont(20));
					FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

					drawColoredText(g2d, text, x + 2, i, -2, difference, model.getBackgroundColor());
					drawColoredText(g2d, text, x, i, 0, difference, model.getForegroundColor());

					if(model.getMouseModel() != null && model.getMouseModel().getText() != null) {
						if(mp.Xcursor > x && mp.Xcursor < x+getWidth(text) && mp.Ycursor > TowerMiner.game.getHeight() - i * 26 - 55 && mp.Ycursor < TowerMiner.game.getHeight() - i * 26 - 55 +26) {
							render = new Thread() {
								public void run() {
									g2d.setFont(TowerMiner.getFont(15));
									FontMetrics metric = g2d.getFontMetrics(g2d.getFont());
									int hgt = metric.getHeight();
									int adv = metric.stringWidth(model.getMouseModel().getText());
									Dimension size = new Dimension(adv + 2, hgt + 2);
									g2d.setColor(new Color(0, 0, 0, 200));
									g2d.fillRect(mp.Xcursor, mp.Ycursor - 16, (int) (4 + size.getWidth()), 16);
									g2d.setColor(Color.WHITE);
									g2d.drawRect(mp.Xcursor, mp.Ycursor - 16, (int) (4 + size.getWidth()), 16);
									drawColoredText(g2d, model.getMouseModel().getText(), mp.Xcursor + 5, mp.Ycursor - 5, 10000, model.getMouseModel().getBackgroundColor());
									drawColoredText(g2d, model.getMouseModel().getText(), mp.Xcursor + 3, mp.Ycursor - 3, 10000, model.getMouseModel().getForegroundColor());
								};
							};
						}
					}
					x = x + metrics.stringWidth(text);
				}
			}
			i--;
		}
		if(render != null) {
			render.run();
		}
	}

	public int getWidth(String text) {
		Graphics2D g2d = (Graphics2D) TowerMiner.game.getGraphics();
		g2d.setFont(TowerMiner.getFont(20));
		FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
		return metrics.stringWidth(text) + 2;
	}

	public void drawColoredText(Graphics2D g2d, String text, int x, int i, int y, int difference, Color co) {
		if (difference < 3000) {
			g2d.setColor(new Color(co.getRed(), co.getGreen(), co.getBlue(), difference / 12));
		} else {
			g2d.setColor(new Color(co.getRed(), co.getGreen(), co.getBlue(), 250));
		}

		g2d.drawString(text, x, TowerMiner.game.getHeight() - (i * 26) - 35 + y);
	}

	public void drawColoredText(Graphics2D g2d, String text, int x, int y, int difference, Color co) {
		if (difference < 3000) {
			g2d.setColor(new Color(co.getRed(), co.getGreen(), co.getBlue(), difference / 12));
		} else {
			g2d.setColor(new Color(co.getRed(), co.getGreen(), co.getBlue(), 250));
		}

		g2d.drawString(text, x, y);
	}

	public String getRaw(ChatMessage message) {
		String s = "";
		for (ChatModel m : message.getModels()) {
			s = s + LanguageManager.getText(m.getText());
		}
		return s;
	}

}
