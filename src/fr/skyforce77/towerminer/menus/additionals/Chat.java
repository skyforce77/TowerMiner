package fr.skyforce77.towerminer.menus.additionals;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.commands.CommandManager;
import fr.skyforce77.towerminer.api.commands.CommandSender;
import fr.skyforce77.towerminer.api.commands.SenderType;
import fr.skyforce77.towerminer.api.events.chat.ChatMouseHoverRenderEvent;
import fr.skyforce77.towerminer.api.events.chat.ChatPluginActionEvent;
import fr.skyforce77.towerminer.api.events.chat.PlayerChatEvent;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.chat.IconModel;
import fr.skyforce77.towerminer.protocol.packets.Packet11ChatMessage;
import fr.skyforce77.towerminer.render.RenderHelper;
import fr.skyforce77.towerminer.render.RenderRunnable;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
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
		if(message.toString().startsWith("/")) {
			return;
		}

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

	public void onMessageReceived(Connection c, ChatMessage message) {
		if(message.toString().startsWith("/") && TowerMiner.menu instanceof MultiPlayer && ((MultiPlayer)TowerMiner.menu).server) {
			String command = message.toString();
			String label = command.replaceFirst("/", "").split(" ")[0];
			CommandManager.onCommandTyped(new CommandSender(c), label, command.replaceFirst("/"+label, "").replaceFirst(" ", "").split(" "));
			return;
		}
		onMessageReceived(message);
	}

	public void onMessageWritten(String player, String message) {
		if(message.startsWith("/")) {
			if(TowerMiner.menu instanceof MultiPlayer && !((MultiPlayer)TowerMiner.menu).server) {
				new Packet11ChatMessage(new ChatMessage(message)).sendAllTCP();
			} else {
				String label = message.replaceFirst("/", "").split(" ")[0];
				CommandManager.onCommandTyped(new CommandSender(SenderType.CHAT), label, message.replaceFirst("/"+label, "").replaceFirst(" ", "").split(" "));
			}
			return;
		}

		ChatModel name = new ChatModel(TowerMiner.player);
		if (player.equals("menu.mp.red")) {
			name.setForegroundColor(Color.RED);
		} else {
			name.setForegroundColor(Color.CYAN);
		}

		ChatMessage msg = new ChatMessage(name, new ChatModel(":"));
		msg.add(getText(message));
		PlayerChatEvent pce = new PlayerChatEvent(msg, message);
		PluginManager.callSyncEvent(pce);
		if (!pce.isCancelled()) {
			if(menu instanceof MultiPlayer) {
				new Packet11ChatMessage(pce.getMessage()).sendAllTCP();
			} else {
				onMessageReceived(pce.getMessage());
			}
		}
	}

	public ChatMessage getText(String message) {
		String[] t = message.split(" ");
		ChatMessage cms = new ChatMessage();
		for(String s : t) {
			cms.addModel(new ChatModel(" "));
			if(s.startsWith("http")) {
				ChatModel m = new ChatModel(s);
				m.setLink(s);
				cms.addModel(m);
			} else {
				cms.addModel(new ChatModel(s));
			}
		}
		return cms;
	}

	public void changeState(boolean state) {
		opened = state;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
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

					Font font = TowerMiner.getFont(12);

					if(model.isBold())
						font = font.deriveFont(Font.BOLD);
					if(model.isItalic())
						font = font.deriveFont(Font.ITALIC);

					g2d.setFont(font);

					Map attributes = font.getAttributes();
					if(model.isUnderlined() || model.isLink())
						attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					if(model.isStriked())
						attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);

					font = font.deriveFont(attributes);
					g2d.setFont(font);

					FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

					if(model instanceof IconModel) {
						IconModel im = (IconModel)model;
						Image image = RessourcesManager.getDistantImage(im.getImage(), "unknown");
						g2d.drawImage(image, x+((metrics.stringWidth(text)-26)/2), TowerMiner.game.getHeight() - i * 26 - 55, 26, 26, null);
					} else {
						if (model.getHighlightColor() != null) {
							Color h = model.getHighlightColor();
							if (difference < 3000) {
								g2d.setColor(new Color(h.getRed(), h.getGreen(), h.getBlue(), difference / 20));
							} else {
								g2d.setColor(new Color(h.getRed(), h.getGreen(), h.getBlue(), 150));
							}
							g2d.fillRect(x, TowerMiner.game.getHeight() - i * 26 - 55, getWidth(text), 26);
						}

						drawColoredText(g2d, text, x + 2, i, -2, difference, model.getBackgroundColor());

						if(!model.isLink() || !model.getForegroundColor().equals(new Color(255,255,255))) {
							drawColoredText(g2d, text, x, i, 0, difference, model.getForegroundColor());
						} else {
							drawColoredText(g2d, text, x, i, 0, difference, new Color(100, 100, 200));
						}
					}

					if(model.isLink() || (model.getMouseModel() != null && model.getMouseModel().getText() != null)) {
						if(mp.Xcursor > x && mp.Xcursor < x+getWidth(text) && mp.Ycursor > TowerMiner.game.getHeight() - i * 26 - 55 && mp.Ycursor < TowerMiner.game.getHeight() - i * 26 - 55 +26) {
							if(model.isLink()) {
								if(model instanceof IconModel) {
									IconModel im = (IconModel)model;
									Image image = RessourcesManager.getDistantImage(im.getImage(), "unknown");
									image = RenderHelper.getColoredImage(image, Color.BLUE, 0.2f);
									g2d.drawImage(image, x+((metrics.stringWidth(text)-26)/2), TowerMiner.game.getHeight() - i * 26 - 55, 26, 26, null);
								} else {
									drawColoredText(g2d, text, x, i, 0, difference, new Color(100, 100, 230));
								}
							}

							if(model.getMouseModel() != null && model.getMouseModel().getText() != null) {
								render = new Thread("MouseModelRender") {
									public void run() {
										ChatMouseHoverRenderEvent mh = new ChatMouseHoverRenderEvent(model.getMouseModel());
										PluginManager.callSyncEvent(mh);
										if(mh.getReplaceRenders().size() == 0) {
											g2d.setFont(TowerMiner.getFont(12));
											FontMetrics metric = g2d.getFontMetrics(g2d.getFont());
											int hgt = metric.getHeight();
											int adv = metric.stringWidth(model.getMouseModel().getText());
											Dimension size = new Dimension(adv + 2, hgt + 2);
											g2d.setColor(new Color(0, 0, 0, 220));
											g2d.fillRoundRect(mp.Xcursor, mp.Ycursor - (int)size.getHeight() + 2, (int) (4 + size.getWidth()), (int)size.getHeight(), 5, 5);
											g2d.setColor(new Color(250, 250, 250, 250));
											g2d.drawRoundRect(mp.Xcursor, mp.Ycursor - (int)size.getHeight() + 2, (int) (4 + size.getWidth()), (int)size.getHeight(), 5, 5);
											drawColoredText(g2d, model.getMouseModel().getText(), mp.Xcursor + 5, mp.Ycursor - 5, 10000, model.getMouseModel().getBackgroundColor());
											drawColoredText(g2d, model.getMouseModel().getText(), mp.Xcursor + 3, mp.Ycursor - 3, 10000, model.getMouseModel().getForegroundColor());
										} else {
											for(RenderRunnable rr : mh.getReplaceRenders()) {
												rr.run(g2d);
											}
										}
										for(RenderRunnable rr : mh.getRenders()) {
											rr.run(g2d);
										}
									};
								};
							}
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean onClick(Graphics g2d, Menu mp) {
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
		for (int s : strings) {
			if (s != -1 && strings.length != 0 && (messagedate.size() > s || opened) && messagedate.get(s) != null) {
				int x = 0;
				for (final ChatModel model : messages.get(s).getModels()) {
					String text = LanguageManager.getText(model.getText());
					if (model.getOption() != null) {
						text = LanguageManager.getText(model.getText(), model.getOption());
					}

					Font font = TowerMiner.getFont(12);

					if(model.isBold())
						font = font.deriveFont(Font.BOLD);
					if(model.isItalic())
						font = font.deriveFont(Font.ITALIC);

					g2d.setFont(font);

					Map attributes = font.getAttributes();
					if(model.isUnderlined() || model.isLink())
						attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
					if(model.isStriked())
						attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);

					font = font.deriveFont(attributes);
					g2d.setFont(font);

					FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());

					if(model.isLink() || (model.getMouseModel() != null && model.getMouseModel().getText() != null)) {
						if(mp.Xcursor > x && mp.Xcursor < x+getWidth(text) && mp.Ycursor > TowerMiner.game.getHeight() - i * 26 - 55 && mp.Ycursor < TowerMiner.game.getHeight() - i * 26 - 55 +26) {
							if(model.isLink()) {
								if(model.getLink().startsWith("tmplugin://")) {
									String data = model.getLink().replaceFirst("tmplugin://", "");
									String channel = data.substring(0, data.indexOf("/"));
									data = data.substring(data.indexOf("/")+1);
									String[] actions = data.split("/");
									PluginManager.callAsyncEvent(new ChatPluginActionEvent(channel, actions));
								} else if(model.getLink().startsWith("tmcmd://")) {
									if(TowerMiner.menu.chatfield != null) {
										String fieldtext = model.getLink().replaceFirst("tmcmd://", "");
										TowerMiner.menu.chatfield.setText(fieldtext);
										TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("chat.click.command"), 4000l));
									}
								} else if(model.getLink().startsWith("tmtypecmd://")) {
									String cmd = model.getLink().replaceFirst("tmtypecmd://", "");
									String label = cmd.split(" ")[0].replaceFirst("/", "");
									String[] args = cmd.replaceFirst(cmd.split(" ")[0], "").replaceFirst(" ", "").split(" ");
									CommandManager.onCommandTyped(new CommandSender(SenderType.CHAT), label, args);
								} else if (Desktop.isDesktopSupported()) {
									Desktop desktop = Desktop.getDesktop();
									if (desktop.isSupported(Action.BROWSE)) {
										try {
											desktop.browse(URI.create(model.getLink()));
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
								return true;
							}
						}
					}
					x = x + metrics.stringWidth(text);
				}
			}
			i--;
		}
		return false;
	}

	public int getWidth(String text) {
		Graphics2D g2d = (Graphics2D) TowerMiner.game.getGraphics();
		g2d.setFont(TowerMiner.getFont(12));
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
