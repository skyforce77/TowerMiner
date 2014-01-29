package fr.skyforce77.towerminer.multiplayer;

import com.esotericsoftware.kryonet.Connection;
import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Achievements;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.entity.*;
import fr.skyforce77.towerminer.entity.effects.EntityEffect;
import fr.skyforce77.towerminer.entity.effects.EntityEffectType;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.*;
import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.particles.Particle;
import fr.skyforce77.towerminer.particles.ParticleEffect;
import fr.skyforce77.towerminer.protocol.BigSending;
import fr.skyforce77.towerminer.protocol.ObjectReceiver;
import fr.skyforce77.towerminer.protocol.ObjectReceiver.ReceivingThread;
import fr.skyforce77.towerminer.protocol.Protocol;
import fr.skyforce77.towerminer.protocol.chat.ChatMessage;
import fr.skyforce77.towerminer.protocol.chat.ChatModel;
import fr.skyforce77.towerminer.protocol.listeners.PacketListener;
import fr.skyforce77.towerminer.protocol.packets.*;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

import java.awt.*;
import java.util.ArrayList;

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
                mp.chat.onMessageReceived(new ChatMessage(new ChatModel(LanguageManager.getText("achievement.text.mp", id) + ": " + Achievements.achievements.get(pack3.deserialize(pack3.data)).text)));
            }
        } else if (p.getId() == 11) {
            Packet11ChatMessage pack11 = (Packet11ChatMessage) p;
            mp = ((MultiPlayer) TowerMiner.menu);
            mp.chat.onMessageReceived(pack11.getMessage());
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
                TowerMiner.game.displayPopup(new Popup(message, 3000, (Image) pack12.deserialize(pack12.imagedata)));
            } else {
                TowerMiner.game.displayPopup(new Popup(message, 3000));
            }
        }
    }

    @Override
    public void onClientReceived(Connection c, Packet p) {
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
                rec.sent = (Integer) pack2.deserialize(pack2.data);
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
                Maps.maps[1023] = (Maps) pack3.deserialize(BigSending.receiving.get((int) pack3.data[0]).data);
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
            Packet6Entity pack6 = (Packet6Entity) p;
            mp = ((MultiPlayer) TowerMiner.menu);
            byte[] data = BigSending.receiving.get(pack6.eid).data;
            Entity en = (Entity) pack6.deserialize(data);
            boolean has = false;
            for (Entity es : mp.draw) {
                if (en != null && es.getUUID() == en.getUUID()) {
                    has = true;
                }
            }
            if (has == false && en != null && !mp.draw.contains(en))
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
            Packet10EntityValueUpdate pack10 = (Packet10EntityValueUpdate) p;
            mp = ((MultiPlayer) TowerMiner.menu);
            for (Entity ens : mp.draw) {
                if (ens.getUUID() == pack10.entity) {
                    if (pack10.value.equals("addeffect")) {
                        ((Mob) ens).addEffect((EntityEffect) pack10.deserialize(pack10.data));
                    } else if (pack10.value.equals("rmveffect")) {
                        ((Mob) ens).removeEffect(EntityEffectType.byId((Integer) pack10.deserialize(pack10.data)));
                    } else if (pack10.value.equals("turretdata")) {
                        int data = (Integer) pack10.deserialize(pack10.data);
                        Turret tu = (Turret) ens;
                        while (tu.getData() < data) {
                            tu.addData();
                        }
                    } else if (pack10.value.equals("life")) {
                        int data = (Integer) pack10.deserialize(pack10.data);
                        ((Mob) ens).setLife(data);
                    } else if (pack10.value.equals("aimanother")) {
                        int aim = (Integer) pack10.deserialize(pack10.data);
                        ((EntityProjectile) ens).setAimed(aim);
                    }
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
        } else if (p.getId() == 17) {
            Packet17Player pack17 = (Packet17Player) p;
            Menu.multiplayerclient.setPlayer(pack17.player);
        } else if (p.getId() == 18) {
            Packet18ParticleEffect pack18 = (Packet18ParticleEffect) p;
            ParticleEffect.createEffect(pack18.x, pack18.y, pack18.color, pack18.type, pack18.data);
        } else if (p.getId() == 19) {
            Packet19Particle pack19 = (Packet19Particle) p;
            mp = ((MultiPlayer) TowerMiner.menu);
            mp.particles.add((Particle) pack19.deserialize(pack19.particle));
        }
    }

    @Override
    public void onServerReceived(Connection c, Packet p) {
        MultiPlayer mp;
        if (p.getId() == 0) {
            Packet0Connecting pack = (Packet0Connecting) p;
            if (MPInfos.matchplaying) {
                new Packet1Disconnecting("menu.mp.client.kick.match").sendConnectionTCP(c);
            } else if (pack.version != Protocol.version) {
                c.sendTCP(new Packet1Disconnecting("menu.mp.client.kick.version"));
            } else {
                MPInfos.matchplaying = true;
                ((MPServerWait) TowerMiner.menu).text = LanguageManager.getText("menu.mp.server.connect");
                MPInfos.connection = c;
                new Packet3Action("sendingmap").sendConnectionTCP(MPInfos.connection);
                BigSending.sendBigObject(Maps.getActualMap(), MPInfos.connection, new ReceivingThread() {
                    @Override
                    public void run(int objectid) {
                        new Packet3Action("finishedsendingmap", (byte) objectid).sendConnectionTCP(MPInfos.connection);
                    }
                });
            }
        } else if (p.getId() == 1) {
            Packet1Disconnecting pack1 = (Packet1Disconnecting) p;
            MPDisconnected m = new MPDisconnected();
            m.reason = LanguageManager.getText(pack1.reason);
            m.text = LanguageManager.getText("menu.mp.connection.client");
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
                mp.setClientGold(mp.clientgold - EntityTypes.turrets[pack9.selected].getPrice());
                EntityTypes type = EntityTypes.turrets[pack9.selected];
                try {
                    Turret tu = (Turret) type.getEntityClass().getConstructor(EntityTypes.class, Point.class, String.class).newInstance(EntityTypes.turrets[pack9.selected], new Point(pack9.x, pack9.y - 1), "menu.mp.red");
                    mp.entity.add(tu);
                    mp.onEntityAdded(tu);
                } catch (Exception ex) {
                }
            } else if (pack9.modifier == 4) {
                if (aimed != null && aimed.getPrice() <= mp.clientgold) {
                    mp.setClientGold(mp.clientgold - aimed.getPrice());
                    aimed.addData();
                }
            } else if (pack9.modifier == 8) {
                if (aimed != null) {
                    mp.setClientGold(mp.clientgold + (int) (aimed.getCost() * 0.75));
                    mp.removed.add(aimed);
                }
            }
        } else if (p.getId() == 14) {
            new Packet15ServerInfos(((Packet14ServerPing) p).name, "A client server").sendConnectionTCP(c);
        }
    }

}
