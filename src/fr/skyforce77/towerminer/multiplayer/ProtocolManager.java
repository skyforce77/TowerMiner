package fr.skyforce77.towerminer.multiplayer;

import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.esotericsoftware.kryonet.Connection;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Achievement;
import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.achievements.ServerPopup;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.PluginUtils;
import fr.skyforce77.towerminer.api.events.cookie.CookieAddedEvent;
import fr.skyforce77.towerminer.api.events.cookie.CookieRemovedEvent;
import fr.skyforce77.towerminer.api.events.cookie.CookieRequestedEvent;
import fr.skyforce77.towerminer.api.events.plugin.PluginMessageEvent;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.mob.Mob;
import fr.skyforce77.towerminer.entity.projectile.Arrow;
import fr.skyforce77.towerminer.entity.projectile.EntityProjectile;
import fr.skyforce77.towerminer.entity.turret.Turret;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MPClientWait;
import fr.skyforce77.towerminer.menus.MPDisconnected;
import fr.skyforce77.towerminer.menus.MPServerWait;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.overlay.OverlayManager;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleEffect;
import fr.skyforce77.towerminer.protocol.BigSending;
import fr.skyforce77.towerminer.protocol.ObjectReceiver;
import fr.skyforce77.towerminer.protocol.ObjectReceiver.ReceivingThread;
import fr.skyforce77.towerminer.protocol.Protocol;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.chat.MessageModel;
import fr.skyforce77.towerminer.protocol.containers.FileContainer;
import fr.skyforce77.towerminer.protocol.listeners.PacketListener;
import fr.skyforce77.towerminer.protocol.packets.Packet;
import fr.skyforce77.towerminer.protocol.packets.Packet0Connecting;
import fr.skyforce77.towerminer.protocol.packets.Packet10DataValueUpdate;
import fr.skyforce77.towerminer.protocol.packets.Packet11ChatMessage;
import fr.skyforce77.towerminer.protocol.packets.Packet12Popup;
import fr.skyforce77.towerminer.protocol.packets.Packet13EntityTeleport;
import fr.skyforce77.towerminer.protocol.packets.Packet14ServerPing;
import fr.skyforce77.towerminer.protocol.packets.Packet15ServerInfos;
import fr.skyforce77.towerminer.protocol.packets.Packet16Sound;
import fr.skyforce77.towerminer.protocol.packets.Packet17Player;
import fr.skyforce77.towerminer.protocol.packets.Packet18ParticleEffect;
import fr.skyforce77.towerminer.protocol.packets.Packet19Particle;
import fr.skyforce77.towerminer.protocol.packets.Packet1Disconnecting;
import fr.skyforce77.towerminer.protocol.packets.Packet20EntityStorage;
import fr.skyforce77.towerminer.protocol.packets.Packet21LoadPlugin;
import fr.skyforce77.towerminer.protocol.packets.Packet22PluginMessage;
import fr.skyforce77.towerminer.protocol.packets.Packet23BlockChange;
import fr.skyforce77.towerminer.protocol.packets.Packet24ServerPopup;
import fr.skyforce77.towerminer.protocol.packets.Packet25RemoveOverlayComponent;
import fr.skyforce77.towerminer.protocol.packets.Packet26AddOverlayComponent;
import fr.skyforce77.towerminer.protocol.packets.Packet27UpdateOverlayComponent;
import fr.skyforce77.towerminer.protocol.packets.Packet28Cookie;
import fr.skyforce77.towerminer.protocol.packets.Packet29RequestCookie;
import fr.skyforce77.towerminer.protocol.packets.Packet2BigSending;
import fr.skyforce77.towerminer.protocol.packets.Packet31ServerProperties;
import fr.skyforce77.towerminer.protocol.packets.Packet3Action;
import fr.skyforce77.towerminer.protocol.packets.Packet4RoundFinished;
import fr.skyforce77.towerminer.protocol.packets.Packet5UpdateInfos;
import fr.skyforce77.towerminer.protocol.packets.Packet6EntityCreate;
import fr.skyforce77.towerminer.protocol.packets.Packet7EntityMove;
import fr.skyforce77.towerminer.protocol.packets.Packet8EntityRemove;
import fr.skyforce77.towerminer.protocol.packets.Packet9MouseClick;
import fr.skyforce77.towerminer.protocol.save.TMStorage;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.save.DataBase;
import fr.skyforce77.towerminer.sounds.Music;

public class ProtocolManager implements PacketListener {

	@Override
	public void onPacketReceived(Connection c, Packet p) {
		MultiPlayer mp;
		if (p.getId() == 3) {
			Packet3Action pack3 = (Packet3Action) p;
			if (pack3.action.equals("achievement")) {
				mp = ((MultiPlayer) TowerMiner.menu);
				String id = "server";
				if (mp.equals(id)) {
					id = LanguageManager.getText("menu.mp.red");
				} else {
					id = LanguageManager.getText("menu.mp.blue");
				}
				Achievement a = Achievements.achievements.get(Packet.deserialize(pack3.data));
				mp.chat.onMessageReceived(new ChatMessage(new ChatModel(LanguageManager.getText("achievement.text.mp", id) + ": "), new ChatModel(a.text).setMouseModel(new MessageModel(a.getDesc()))));
			}
		} else if (p.getId() == 11) {
			Packet11ChatMessage pack11 = (Packet11ChatMessage) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			mp.chat.onMessageReceived(c, pack11.getMessage());
			if (!pack11.response) {
				pack11.response = true;
				pack11.sendAllTCP();
			}
		} else if (p.getId() == 12) {
			Packet12Popup pack12 = (Packet12Popup) p;
			String message = LanguageManager.getText(pack12.message);
			if (pack12.option != null) {
				message = LanguageManager.getText(pack12.message, LanguageManager.getText(pack12.option));
			}

			if (pack12.image != null) {
				TowerMiner.game.displayPopup(new Popup(message, 3000, pack12.image));
			} else if (pack12.imagedata != null) {
				TowerMiner.game.displayPopup(new Popup(message, 3000, (Image) Packet.deserialize(pack12.imagedata)));
			} else {
				TowerMiner.game.displayPopup(new Popup(message, 3000));
			}
		} else if (p.getId() == 22) {
			Packet22PluginMessage pack22 = (Packet22PluginMessage) p;
			PluginMessageEvent pme = new PluginMessageEvent(pack22.plugin, pack22.version, pack22.type, Packet.deserialize(pack22.data));
            PluginManager.callAsyncEvent(pme);
        }
    }

	@Override
	public void onClientReceived(final Connection c, Packet p) {
		MultiPlayer mp;
		if (p.getId() == 1) {
			Packet1Disconnecting pack1 = (Packet1Disconnecting) p;
			MPDisconnected m = new MPDisconnected();
			m.reason = LanguageManager.getText(pack1.reason);
			TowerMiner.returnMenu(m);
		} else if (p.getId() == 2) {
			Packet2BigSending pack2 = (Packet2BigSending) p;
			if (pack2.type == 0) {
				ObjectReceiver rec = new ObjectReceiver();
				rec.id = pack2.pid;
				rec.sent = (Integer) Packet.deserialize(pack2.data);
				rec.data = new byte[pack2.lenght];
				rec.received = new boolean[rec.sent];
				BigSending.receiving.put(pack2.pid, rec);
			} else if (pack2.type == 2) {
				ArrayList<Byte> fails = new ArrayList<Byte>();
				boolean received = true;

				int i = 0;
				for (boolean b : BigSending.receiving.get(pack2.pid).received) {
					i++;
					if (!b) {
						received = false;
						fails.add((byte) i);
					}
				}

				Packet2BigSending pb = new Packet2BigSending();
				pb.type = 2;
				pb.pid = pack2.pid;
				if (received) {
					pb.pack = 1;
				} else {
					pb.data = new byte[fails.size()];
					i = 0;
					for (Byte b : fails) {
						pb.data[i] = b;
						i++;
					}
				}
				pb.sendClientTCP();
			} else {
				BigSending.receive(pack2.pid, pack2.lenght, pack2.pack, pack2.data);
			}
		} else if (p.getId() == 3) {
			Packet3Action pack3 = (Packet3Action) p;
			if (pack3.action.equals("sendingmap")) {
				((MPClientWait) TowerMiner.menu).text = LanguageManager.getText("menu.mp.client.map");
			} else if (pack3.action.equals("finishedsendingmap")) {
				Maps.maps[1023] = (Maps) Packet.deserialize(BigSending.receiving.get((int) pack3.data[0]).data);
				Maps.setActualMap(1023);
				c.sendTCP(new Packet3Action("canstartgame"));
				TowerMiner.setMenu(Menu.multiplayerclient);
			} else if (pack3.action.equals("startround")) {
				((MultiPlayer) TowerMiner.menu).renderarrow = false;
			}
		} else if (p.getId() == 4) {
			Packet4RoundFinished pack4 = (Packet4RoundFinished) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			mp.or = pack4.gold;
			mp.round = pack4.round;
			mp.vie = pack4.life;
			mp.next.setEnabled(true);
			if (pack4.timed)
				mp.countNext();
			((MultiPlayer) TowerMiner.menu).renderarrow = true;
			ArrayList<Entity> toremove = new ArrayList<Entity>();
			for (Entity en : ((MultiPlayer) TowerMiner.menu).draw) {
				if (en instanceof Mob || en instanceof EntityProjectile) {
					toremove.add(en);
				}
			}
			for (Entity en : toremove) {
				((MultiPlayer) TowerMiner.menu).draw.remove(en);
			}
		} else if (p.getId() == 5) {
			Packet5UpdateInfos pack5 = (Packet5UpdateInfos) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			mp.or = pack5.gold;
			mp.vie = pack5.life;
		} else if (p.getId() == 6) {
			Packet6EntityCreate pack6 = (Packet6EntityCreate) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			Entity en = null;
			if(pack6.shooter != 0) {
				try {
					en = EntityTypes.getType(pack6.type).getEntityClass().getConstructor(EntityTypes.class, Turret.class, Mob.class)
							.newInstance(EntityTypes.getType(pack6.type), (Turret)mp.byUUID(pack6.shooter), (Mob)mp.byUUID(pack6.mob));
				} catch (Exception e) {
					en = new Arrow(EntityTypes.ARROW, (Turret)mp.byUUID(pack6.shooter), (Mob)mp.byUUID(pack6.mob));
				}
			} else if(!pack6.owner.equals("") && pack6 != null) {
				try {
					en = EntityTypes.getType(pack6.type).getEntityClass().getConstructor(EntityTypes.class, Point.class, String.class)
							.newInstance(EntityTypes.getType(pack6.type), new Point(pack6.x, pack6.y), pack6.owner);
				} catch (Exception e) {
					en = new Turret(EntityTypes.SKELETON, new Point(pack6.x, pack6.y), pack6.owner);
				}
			} else {
				try {
					en = EntityTypes.getType(pack6.type).getEntityClass().getConstructor(EntityTypes.class)
							.newInstance(EntityTypes.getType(pack6.type));
				} catch (Exception e) {
					en = new Mob(EntityTypes.CHICKEN);
				}
			}
			en.setUUID(pack6.eid);

			for (Entity es : mp.draw) {
				if (en != null && es.getUUID() == en.getUUID()) {
					mp.draw.remove(es);
				}
			}

			mp.draw.add(en);
		} else if (p.getId() == 7) {
			Packet7EntityMove pack7 = (Packet7EntityMove) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			for (Entity ens : mp.draw) {
				if (ens.getUUID() == pack7.entity) {
					if (ens instanceof Mob) {
						ens.setLocation(new Point(pack7.x, pack7.y));
						ens.setRotation(pack7.rotation);
						((Mob) ens).moveTo(new Point(pack7.xto, pack7.yto));
					}
				}
			}
		} else if (p.getId() == 8) {
			final Packet8EntityRemove pack8 = (Packet8EntityRemove) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			for (Entity en : mp.draw) {
				if (en.getUUID() == pack8.entity) {
					mp.draw.remove(en);
				}
			}
		} else if (p.getId() == 10) {
			Packet10DataValueUpdate pack10 = (Packet10DataValueUpdate) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			for (Entity ens : mp.draw) {
				if (ens.getUUID() == pack10.entity) {
					ens.getData().addObject(pack10.value, (Serializable)Packet.deserialize(pack10.data));
				}
			}
		} else if (p.getId() == 13) {
			Packet13EntityTeleport pack13 = (Packet13EntityTeleport) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			for (Entity ens : mp.draw) {
				if (ens.getUUID() == pack13.entity) {
					ens.setLocation(new Point(pack13.x, pack13.y));
					ens.setRotation(pack13.rotation);
				}
			}
		} else if (p.getId() == 15) {
			Packet15ServerInfos pack15 = (Packet15ServerInfos) p;
			ServerInfos.onInfosReceived(pack15.name, pack15.message, pack15.date);
		} else if (p.getId() == 16) {
			Packet16Sound pack16 = (Packet16Sound) p;
			try {
				Music.playURL(pack16.channel, new URL(pack16.music), pack16.custom);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else if (p.getId() == 17) {
			Packet17Player pack17 = (Packet17Player) p;
			Menu.multiplayerclient.setPlayer(pack17.player);
		} else if (p.getId() == 18) {
			Packet18ParticleEffect pack18 = (Packet18ParticleEffect) p;
			ParticleEffect.createEffect(pack18.x, pack18.y, pack18.color, pack18.type, pack18.data);
		} else if (p.getId() == 19) {
			Packet19Particle pack19 = (Packet19Particle) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			TowerMiner.game.particles.add((Particle) Packet.deserialize(pack19.particle));
		} else if (p.getId() == 20) {
			Packet20EntityStorage pack20 = (Packet20EntityStorage) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			byte[] data = BigSending.receiving.get(pack20.bigsendingid).data;
			TMStorage tms = (TMStorage) Packet.deserialize(data);
			for (Entity es : mp.draw) {
				if (tms != null && es.getUUID() == pack20.entityid) {
					es.getData().add(tms);
				}
			}
		} else if (p.getId() == 21) {
			Packet21LoadPlugin pack21 = (Packet21LoadPlugin) p;
			byte[] data = BigSending.receiving.get(pack21.eid).data;
			final FileContainer fc = (FileContainer) Packet.deserialize(data);
            new Thread("PluginAutoInstall") {
                public void run() {
                	Packet21LoadPlugin load = new Packet21LoadPlugin();
                	File f = new File(RessourcesManager.getServerPluginsDirectory(c), fc.getFileName());
                	
                	if(f.exists()) {
                		PluginManager.loadPlugin(f);
						load.installed = true;
						load.sendAllTCP();
						return;
                	}
                	
                    int ok = JOptionPane.showConfirmDialog(null, LanguageManager.getText("plugin.auto.want", fc.getFileName().replace(".jar", "")), "Plugin", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if(ok == JOptionPane.YES_OPTION) {
						PluginManager.loadPlugin(c, fc);
						load.installed = true;
						load.sendAllTCP();
					} else {
						TowerMiner.returnMenu(Menu.mainmenu);
						load.installed = false;
						load.sendAllTCP();
					}
				};
			}.start();
		} else if (p.getId() == 23) {
			Packet23BlockChange pack23 = (Packet23BlockChange) p;
			if(pack23.overlay) {
				Maps.getActualMap().setOverlayIdAndData(pack23.x, pack23.y, pack23.type, pack23.data);
				if(pack23.hasStorage())
					Maps.getActualMap().setOverlayStorage(pack23.x, pack23.y, pack23.getStorage());
			} else {
				Maps.getActualMap().setBlockIdAndData(pack23.x, pack23.y, pack23.type, pack23.data);
				if(pack23.hasStorage())
					Maps.getActualMap().setBlockStorage(pack23.x, pack23.y, pack23.getStorage());
			}
		} else if (p.getId() == 24) {
			Packet24ServerPopup pack24 = (Packet24ServerPopup)p;
			BufferedImage bu = null;
			try {
				bu = ImageIO.read(new ByteArrayInputStream((byte[])Packet.deserialize(BigSending.receiving.get(pack24.imageid).data)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			TowerMiner.game.setTitle(pack24.message);
			TowerMiner.game.setIconImage(bu);
			TowerMiner.game.displayPopup(new ServerPopup(pack24.message, pack24.description, pack24.time, bu));
		} else if (p.getId() == 25) {
			Packet25RemoveOverlayComponent pack25 = (Packet25RemoveOverlayComponent)p;
			OverlayManager.removeComponent(pack25.getComponentId());
		} else if (p.getId() == 26) {
			Packet26AddOverlayComponent pack26 = (Packet26AddOverlayComponent)p;
			OverlayManager.addComponent(pack26.getComponentId(), pack26.getStorage());
		} else if (p.getId() == 27) {
			Packet27UpdateOverlayComponent pack27 = (Packet27UpdateOverlayComponent)p;
			TMStorage storage = OverlayManager.getComponent(pack27.getComponentId());
			if(storage != null) {
				storage.add(pack27.getStorage());
			}
		} else if (p.getId() == 28) {
			Packet28Cookie pack28 = (Packet28Cookie)p;
			
			if(pack28.action == 0) {
				String cookie = pack28.getCookie();
				if(cookie != null) {
					CookieAddedEvent cae = new CookieAddedEvent(c.getRemoteAddressTCP().getHostString(), pack28.getName(), cookie);
		            PluginManager.callSyncEvent(cae);
		            if(!cae.isCancelled())
		            	RessourcesManager.addServerCookie(c, pack28.getName(), cookie);
				}
			} else if(pack28.action == 1) {
				String cookie = RessourcesManager.getServerCookie(c, pack28.getName());
				if(cookie != null) {
					CookieRemovedEvent cre = new CookieRemovedEvent(c.getRemoteAddressTCP().getHostString(), pack28.getName(), cookie);
		            PluginManager.callSyncEvent(cre);
		            if(!cre.isCancelled())
		            	RessourcesManager.removeServerCookie(c, pack28.getName());
				}
			}
		} else if (p.getId() == 29) {
			Packet29RequestCookie pack29 = (Packet29RequestCookie)p;
			
			CookieRequestedEvent cre = new CookieRequestedEvent(c.getRemoteAddressTCP().getHostString(), pack29.getName(), RessourcesManager.getServerCookie(c, pack29.getName()));
		    PluginManager.callSyncEvent(cre);
		    if(!cre.isCancelled()) {
		    	c.sendTCP(new Packet28Cookie(Packet28Cookie.RESPOND, cre.getName(), cre.getCookie()));
		    }
		} else if (p.getId() == 31) {
			Packet31ServerProperties packet31 = (Packet31ServerProperties)p;
			MPInfos.setProperties(packet31.getStorage());
		}
	}

	@Override
	public void onServerReceived(Connection c, Packet p) {
		MultiPlayer mp;
		if (p.getId() == 0) {
			final Packet0Connecting pack = (Packet0Connecting) p;
			if (MPInfos.matchplaying) {
				new Packet1Disconnecting("menu.mp.client.kick.match").sendConnectionTCP(c);
			} else if (pack.version != Protocol.version) {
				c.sendTCP(new Packet1Disconnecting("menu.mp.client.kick.version"));
			} else {
				MPInfos.matchplaying = true;
				((MPServerWait) TowerMiner.menu).text = LanguageManager.getText("menu.mp.server.connect");
				MPInfos.connection = c;
                new Thread("MultiPlayerInfos") {
                    public void run() {
                    	boolean sent = true;
						if (!PluginManager.canConnect(pack.getPlugins())) {
							sent = PluginManager.sendNeededPlugins(pack.getPlugins());
						}
						if(!sent) {
							TowerMiner.returnMenu(Menu.mainmenu);
							PluginUtils.write("Client refused plugin sending");
						}
						new Packet3Action("sendingmap").sendConnectionTCP(MPInfos.connection);
						new BigSending(Maps.getActualMap(), MPInfos.connection, new ReceivingThread() {
							@Override
							public void run(int objectid) {
								new Packet3Action("finishedsendingmap", (byte) objectid).sendConnectionTCP(MPInfos.connection);

								ByteArrayOutputStream baos = new ByteArrayOutputStream();
								try {
									ImageIO.write(RessourcesManager.getBufferedTexture("skull_steve"), "png", baos);
								} catch (IOException e) {
									e.printStackTrace();
								}
								new BigSending(baos.toByteArray(), MPInfos.connection, new ReceivingThread() {
									@Override
									public void run(int objectid) {
										String name = (String)DataBase.getValue("server_name", "%p's server");
										name = name.replaceAll("%p", TowerMiner.player);
										String[] desc = ((String)DataBase.getValue("server_desc","Welcome to my local server!\nHave a good time")).split("\n");
										TowerMiner.game.setTitle(name);
										TowerMiner.game.setIconImage(RessourcesManager.getBufferedTexture("skull_steve"));
										new Packet24ServerPopup(name, desc, objectid, 10000).sendConnectionTCP(MPInfos.connection);
									}
								});
							}
						});
					};
				}.start();
			}
		} else if (p.getId() == 1) {
			Packet1Disconnecting pack1 = (Packet1Disconnecting) p;
			MPDisconnected m = new MPDisconnected();
			m.reason = LanguageManager.getText(pack1.reason);
			m.text = LanguageManager.getText("menu.mp.connection.client");
			MPInfos.remove();
			TowerMiner.returnMenu(m);
		} else if (p.getId() == 2) {
			Packet2BigSending pack2 = (Packet2BigSending) p;
			if (pack2.type == 2) {
				if (pack2.pack == 1) {
					BigSending.sending.get(pack2.pid).thread.run(pack2.pid);
				} else {
					for (byte b : pack2.data) {
						BigSending.sending.get(pack2.pid).packets[(int) b].sendConnectionTCP(MPInfos.connection);
					}
					BigSending.sending.get(pack2.pid).testpacket.sendConnectionTCP(MPInfos.connection);
				}
			}
		} else if (p.getId() == 3) {
			Packet3Action pack3 = (Packet3Action) p;
			if (pack3.action.equals("canstartgame")) {
				TowerMiner.setMenu(Menu.multiplayerserver);
			} else if (pack3.action.equals("ready")) {
				mp = ((MultiPlayer) TowerMiner.menu);
				mp.clientready = true;
				TowerMiner.game.displayPopup(new Popup(LanguageManager.getText("menu.mp.ready", LanguageManager.getText("menu.mp.red")), 3000));
				if (mp.serverready && mp.clientready) {
					mp.serverready = false;
					mp.clientready = false;
					new Packet3Action("startround").sendConnectionTCP(MPInfos.connection);
					mp.started = true;
				}
			}
		} else if (p.getId() == 9) {
			Packet9MouseClick pack9 = (Packet9MouseClick) p;
			mp = ((MultiPlayer) TowerMiner.menu);
			Turret aimed = null;
			for (Entity en : mp.entity) {
				if (en instanceof Turret && en.getUUID() == pack9.aimed) {
					aimed = (Turret) en;
				}
			}
			if (pack9.modifier == 16) {
				mp.setClientGold(mp.clientgold - EntityTypes.getType(pack9.selected).getPrice());
				EntityTypes type = EntityTypes.getType(pack9.selected);
				try {
					Turret tu = (Turret) type.getEntityClass().getConstructor(EntityTypes.class, Point.class, String.class).newInstance(type, new Point(pack9.x, pack9.y - 1), "menu.mp.red");
					mp.entity.add(tu);
					mp.onEntityAdded(tu);
				} catch (Exception ex) {
				}
			} else if (pack9.modifier == 4) {
				if (aimed != null && aimed.getPrice() <= mp.clientgold) {
					mp.setClientGold(mp.clientgold - aimed.getPrice());
					aimed.addLevel();
				}
			} else if (pack9.modifier == 8) {
				if (aimed != null) {
					mp.setClientGold(mp.clientgold + (int) (aimed.getCost() * 0.75));
					mp.removed.add(aimed);
				}
			}
		} else if (p.getId() == 14) {
			new Packet15ServerInfos(((Packet14ServerPing) p).name, "A client server").sendConnectionTCP(c);
		} else if (p.getId() == 21) {
			Packet21LoadPlugin pack21 = (Packet21LoadPlugin) p;
			PluginManager.pluginsReceived = pack21.installed;
			PluginManager.responseReceived = true;
		}
	}

}
