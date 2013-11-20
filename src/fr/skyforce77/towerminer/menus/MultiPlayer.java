package fr.skyforce77.towerminer.menus;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.Turret;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.additionals.Chat;
import fr.skyforce77.towerminer.multiplayer.BigSending;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.multiplayer.ObjectReceiver;
import fr.skyforce77.towerminer.multiplayer.packets.Packet12Popup;
import fr.skyforce77.towerminer.multiplayer.packets.Packet13EntityTeleport;
import fr.skyforce77.towerminer.multiplayer.packets.Packet1Disconnecting;
import fr.skyforce77.towerminer.multiplayer.packets.Packet3Action;
import fr.skyforce77.towerminer.multiplayer.packets.Packet4RoundFinished;
import fr.skyforce77.towerminer.multiplayer.packets.Packet5UpdateInfos;
import fr.skyforce77.towerminer.multiplayer.packets.Packet6Entity;
import fr.skyforce77.towerminer.multiplayer.packets.Packet7EntityMove;
import fr.skyforce77.towerminer.multiplayer.packets.Packet8EntityRemove;
import fr.skyforce77.towerminer.multiplayer.packets.Packet9MouseClick;
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

	public MultiPlayer(final boolean server) {
		super(true);
		this.server = server;
		chat = new Chat(server);

		next.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				next.setEnabled(false);

				if(server) {
					serverready = true;
					Connect.c.sendTCP(new Packet12Popup("menu.mp.ready", "menu.mp.blue"));
					if(serverready && clientready) {
						serverready = false;
						clientready = false;
						Connect.c.sendTCP(new Packet3Action("startround"));
						started = true;
					}
				} else {
					Connect.c.sendTCP(new Packet3Action("ready"));
				}
			}
		});

		chatfield = new JTextField();
		chatfield.setPreferredSize(new Dimension(100,30));
		chatfield.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String ad = "menu.mp.red";
				if(server) {
					ad = "menu.mp.blue";
				}
				if(!chatfield.getText().equals("")) {
					chat.onMessageWritten(ad, chatfield.getText());
					chatfield.setText("");
				}
			}
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

		if(server) {
			TowerMiner.game.setTitle(LanguageManager.getText("towerminer")+" | "+TowerMiner.game.version+" | "+LanguageManager.getText("menu.multiplayer")+" | "+LanguageManager.getText("menu.editor.map")+": "+Maps.getActualMap().getName()+" | "+LanguageManager.getText("menu.mp.blue"));
		} else {
			TowerMiner.game.setTitle(LanguageManager.getText("towerminer")+" | "+TowerMiner.game.version+" | "+LanguageManager.getText("menu.multiplayer")+" | "+LanguageManager.getText("menu.editor.map")+": "+Maps.getActualMap().getName()+" | "+LanguageManager.getText("menu.mp.red"));
		}

		new Thread() {
			public void run() {
				while(true) {
					if(!Connect.c.isConnected()) {
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
		Connect.c.sendTCP(pr);
	}

	public void setClientGold(int gold) {
		clientgold = gold;
		Packet5UpdateInfos pr = new Packet5UpdateInfos();
		pr.gold = clientgold;
		pr.life = clientlife;
		Connect.c.sendTCP(pr);
	}

	public void setClientLife(int life) {
		clientlife = life;
		Packet5UpdateInfos pr = new Packet5UpdateInfos();
		pr.gold = clientgold;
		pr.life = clientlife;
		Connect.c.sendTCP(pr);
	}

	@Override
	public void onEntityAdded(final Entity en) {
		if(server) {
			new Thread() {
				@Override
				public void run() {
					BigSending.sendBigObject(en, new ObjectReceiver.ReceivingThread() {
						@Override
						public void run(int objectid) {
							Packet6Entity pe = new Packet6Entity();
							pe.id = objectid;
							Connect.c.sendTCP(pe);
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
			Connect.c.sendUDP(pem);
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
			Connect.c.sendUDP(pem);
		}
	}

	@Override
	public void onEntityRemoved(Entity en) {
		if(server) {
			final Packet8EntityRemove per = new Packet8EntityRemove();
			per.entity = en.getUUID();
			Connect.c.sendTCP(per);
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
							if(((Turret)en).isOwner(getId()))
								aimed = (Turret)en;
						}
					}

					if(p.y-1 > -1 && !turretplaced && EntityTypes.turrets[selectedturret].getPrice() <= or && Maps.getActualMap().canPlaceTurret(p.x,p.y-1)) {
						Packet9MouseClick pm = new Packet9MouseClick();
						pm.modifier = e.getModifiers();
						pm.x = p.x;
						pm.y = p.y;
						pm.selected = selectedturret;
						Connect.c.sendTCP(pm);
					}
				} else {
					aimed = null;
				}
			} else {
				Packet9MouseClick pm = new Packet9MouseClick();
				pm.modifier = e.getModifiers();
				pm.x = Xcursor;
				pm.y = Ycursor;
				pm.selected = selectedturret;
				pm.aimed = aimed.getUUID();
				Connect.c.sendTCP(pm);
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
		Connect.c.sendTCP(pr);
	}

	@Override
	public void onGameOver() {
		boolean team = Maps.getActualMap().getDeathPoints()[0].equals(Maps.getActualMap().getDeathPoints()[1]);
		if(team) {
			Connect.c.sendTCP(new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.survive", round-1)));
			if(server) {
				new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.survive", round-1)).onServerReceived(Connect.c);
			} else {
				new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.survive", round-1)).onClientReceived(Connect.c);
			}
		} else {
			Connect.c.sendTCP(new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.lose", round)));
			if(server) {
				new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.win")).onClientReceived(Connect.c);
			} else {
				new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.win")).onServerReceived(Connect.c);
			}
		}
	}
	
	@Override
	public String getId() {
		String id = "server";
		if(!server) {
			id = "client";
		}
		return id;
	}
}
