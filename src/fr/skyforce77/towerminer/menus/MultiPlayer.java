package fr.skyforce77.towerminer.menus;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JTextField;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.Turret;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.additionals.Chat;
import fr.skyforce77.towerminer.multiplayer.MPInfos;
import fr.skyforce77.towerminer.multiplayer.ProtocolManager;
import fr.skyforce77.towerminer.protocol.BigSending;
import fr.skyforce77.towerminer.protocol.Connect;
import fr.skyforce77.towerminer.protocol.ObjectReceiver;
import fr.skyforce77.towerminer.protocol.packets.Packet12Popup;
import fr.skyforce77.towerminer.protocol.packets.Packet13EntityTeleport;
import fr.skyforce77.towerminer.protocol.packets.Packet1Disconnecting;
import fr.skyforce77.towerminer.protocol.packets.Packet3Action;
import fr.skyforce77.towerminer.protocol.packets.Packet4RoundFinished;
import fr.skyforce77.towerminer.protocol.packets.Packet5UpdateInfos;
import fr.skyforce77.towerminer.protocol.packets.Packet6Entity;
import fr.skyforce77.towerminer.protocol.packets.Packet7EntityMove;
import fr.skyforce77.towerminer.protocol.packets.Packet8EntityRemove;
import fr.skyforce77.towerminer.protocol.packets.Packet9MouseClick;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class MultiPlayer extends SinglePlayer {

	public boolean server = false;

	public boolean serverready = false;
	public boolean clientready = false;

	public int clientlastlife = 20;
	public int clientlife = 20;
	public int clientgold = 60;

	public Chat chat;
	public JTextField chatfield;
	public CopyOnWriteArrayList<String> typed = new CopyOnWriteArrayList<String>();
	public int select = 0;

	public MultiPlayer(final boolean server) {
		super(true);
		this.server = server;
		chat = new Chat(server);
		
		player = server ? "menu.mp.blue" : "menu.mp.red";

		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				next.setEnabled(false);

				if(server) {
					serverready = true;
					new Packet12Popup("menu.mp.ready", player).sendAllTCP();
					if(serverready && clientready) {
						serverready = false;
						clientready = false;
						new Packet3Action("startround").sendAllTCP();
						started = true;
					}
				} else {
					new Packet3Action("ready").sendAllTCP();
				}
			}
		});

		chatfield = new JTextField();
		chatfield.setPreferredSize(new Dimension(100,30));
		chatfield.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(!chatfield.getText().equals("")) {
					chat.onMessageWritten(player, chatfield.getText());
					typed.add(chatfield.getText());
					chatfield.setText("");
				}
			}
		});
		chatfield.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DOWN) {
					if(select+1 < typed.size() && typed.get(select+1) != null) {
						chatfield.setText(typed.get(select+1));
						select++;
					}
				} else if(e.getKeyCode() == KeyEvent.VK_UP) {
					if(select-1 > typed.size() && typed.get(select-1) != null) {
						chatfield.setText(typed.get(select-1));
						select--;
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyTyped(KeyEvent e) {}
		});
		chatfield.setToolTipText(LanguageManager.getText("menu.mp.message"));
		TowerMiner.game.add(chatfield);

		chatfield.setVisible(false);
	}

	@Override
	public void drawMenu(Graphics2D g2d) {
		super.drawMenu(g2d);
		int xt,yt;
		if(Maps.getActualMap().getDeathPoints()[0].equals(Maps.getActualMap().getDeathPoints()[1])) {
			xt = (int) (Maps.getActualMap().getDeathPoints()[0].getX()*48);
			yt = (int) (Maps.getActualMap().getDeathPoints()[0].getY()*48);
			g2d.drawImage(RessourcesManager.getTexture("death2"),xt,yt+40,48,48,null);
		} else {
			xt = (int) (Maps.getActualMap().getDeathPoints()[0].getX()*48);
			yt = (int) (Maps.getActualMap().getDeathPoints()[0].getY()*48);
			g2d.drawImage(RessourcesManager.getTexture("death"),xt,yt+40,48,48,null);

			xt = (int) (Maps.getActualMap().getDeathPoints()[1].getX()*48);
			yt = (int) (Maps.getActualMap().getDeathPoints()[1].getY()*48);
			g2d.drawImage(RessourcesManager.getTexture("death1"),xt,yt+40,48,48,null);
		}

		chat.draw(g2d);
	}

	@Override
	public void onUnused() {
		super.onUnused();

		if(server) {
			Connect.server.stop();
		} else {
			Connect.client.stop();
		}
		MPInfos.matchplaying = false;

		chatfield.setVisible(false);

		if(chat.opened) {
			chat.changeState();
		}
	}

	@Override
	public void onUsed() {
		super.onUsed();
		next.setText(LanguageManager.getText("menu.mp.ready.button"));
		speed.setVisible(false);
		chatfield.setVisible(true);
		pause.setVisible(false);
		options.setVisible(false);

		TowerMiner.game.setTitle(LanguageManager.getText("towerminer")+" | "+Game.version+" | "+LanguageManager.getText("menu.multiplayer")+" | "+LanguageManager.getText("menu.editor.map")+": "+Maps.getActualMap().getName()+" | "+LanguageManager.getText(player));

		new Thread() {
			public void run() {
				while(true) {
					if((Connect.client != null && !Connect.client.isConnected()) || (Connect.server != null && !MPInfos.connection.isConnected())) {
						if(TowerMiner.menu instanceof MultiPlayer) {
							MPDisconnected d = new MPDisconnected();
							d.reason = LanguageManager.getText("menu.mp.connection.lost");
							if(server) {
								d.text = LanguageManager.getText("menu.mp.connection.client");
							}
							TowerMiner.returnMenu(d);
						}
						break;
					}
					try {
						sleep(100l);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onFinishedRound() {

		if(clientlastlife == clientlife) {
			clientlife = clientlife+10;
		}
		clientlastlife = clientlife;

		Packet4RoundFinished pr = new Packet4RoundFinished();
		pr.gold = clientgold;
		pr.life = clientlife;
		pr.sendAllTCP();
	}

	public void setClientGold(int gold) {
		clientgold = gold;
		Packet5UpdateInfos pr = new Packet5UpdateInfos();
		pr.gold = clientgold;
		pr.life = clientlife;
		pr.sendAllTCP();
	}

	public void setClientLife(int life) {
		clientlife = life;
		Packet5UpdateInfos pr = new Packet5UpdateInfos();
		pr.gold = clientgold;
		pr.life = clientlife;
		pr.sendAllTCP();
	}

	@Override
	public void onEntityAdded(final Entity en) {
		if(server) {
			new Thread() {
				@Override
				public void run() {
					BigSending.sendBigObject(en, MPInfos.connection, new ObjectReceiver.ReceivingThread() {
						@Override
						public void run(int objectid) {
							Packet6Entity pe = new Packet6Entity();
							pe.eid = objectid;
							pe.sendAllTCP();
						}
					});
				}
			}.start();
		}
	}

	@Override
	public void onEntityMove(Entity en, Point p, Point to) {
		if(server) {
			Packet7EntityMove pem = new Packet7EntityMove();
			pem.entity = en.getUUID();
			pem.x = (int)p.getX();
			pem.y = (int)p.getY();
			pem.xto = (int)to.getX();
			pem.yto = (int)to.getY();
			pem.rotation = en.getRotation();
			pem.sendAllUDP();
		}
	}
	
	@Override
	public void onEntityTeleport(Entity en, Point p) {
		if(server) {
			Packet13EntityTeleport pem = new Packet13EntityTeleport();
			pem.entity = en.getUUID();
			pem.x = (int)p.getX();
			pem.y = (int)p.getY();
			pem.rotation = en.getRotation();
			pem.sendAllUDP();
		}
	}

	@Override
	public void onEntityRemoved(Entity en) {
		if(server) {
			final Packet8EntityRemove per = new Packet8EntityRemove();
			per.entity = en.getUUID();
			per.sendAllTCP();
		}
	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		if(server) {
			super.onMouseClicked(e);
		} else {
			if(e.getModifiers() == 16) {
				if(gameover) {
					onKeyPressed(KeyEvent.VK_ENTER);
				} else if(aimed == null){
					Point p = new Point(Xcursor/MapWritter.getBlockWidth()-(CanvasX/MapWritter.getBlockWidth()),Ycursor/MapWritter.getBlockHeight()-(CanvasY/MapWritter.getBlockHeight()));
					boolean turretplaced = false;
					for(Entity en : draw) {
						if(en instanceof Turret && en.getBlockLocation().equals(new Point(p.x,p.y-1))) {
							turretplaced = true;
							if(((Turret)en).isOwner(getPlayer()))
								aimed = (Turret)en;
						}
					}

					if(p.y-1 > -1 && !turretplaced && EntityTypes.turrets[selectedturret].getPrice() <= or && Maps.getActualMap().canPlaceTurret(p.x,p.y-1)) {
						Packet9MouseClick pm = new Packet9MouseClick();
						pm.modifier = e.getModifiers();
						pm.x = p.x;
						pm.y = p.y;
						pm.selected = selectedturret;
						pm.sendAllTCP();
					}
				} else {
					aimed = null;
				}
			} else if(aimed != null) {
				Packet9MouseClick pm = new Packet9MouseClick();
				pm.modifier = e.getModifiers();
				pm.x = Xcursor;
				pm.y = Ycursor;
				pm.selected = selectedturret;
				pm.aimed = aimed.getUUID();
				pm.sendAllTCP();
			}
		}
	}

	@Override
	public void addGold(int add) {
		or+=add;
		clientgold+=add;
		orgained+=add;

		Packet5UpdateInfos pr = new Packet5UpdateInfos();
		pr.gold = clientgold;
		pr.life = clientlife;
		pr.sendAllTCP();
	}

	@Override
	public void onGameOver() {
		boolean team = Maps.getActualMap().getDeathPoints()[0].equals(Maps.getActualMap().getDeathPoints()[1]);
		if(team) {
			new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.survive", round-1)).sendAllTCP();
			if(server) {
				new ProtocolManager().onServerReceived(MPInfos.connection, new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.survive", round-1)));
			} else {
				new ProtocolManager().onClientReceived(MPInfos.connection, new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.survive", round-1)));
			}
		} else {
			new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.lose", round)).sendAllTCP();
			if(server) {
				new ProtocolManager().onClientReceived(MPInfos.connection, new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.win")));
			} else {
				new ProtocolManager().onServerReceived(MPInfos.connection, new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.win")));
			}
		}
	}
	
	@Override
	public void setPlayer(String player) {
		super.setPlayer(player);
		TowerMiner.game.setTitle(LanguageManager.getText("towerminer")+" | "+Game.version+" | "+LanguageManager.getText("menu.multiplayer")+" | "+LanguageManager.getText("menu.editor.map")+": "+Maps.getActualMap().getName()+" | "+LanguageManager.getText(player));
	}
}
