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

import javax.swing.JRadioButton;
import javax.swing.JTextField;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityProjectile;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.Mob;
import fr.skyforce77.towerminer.entity.Turret;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.additionals.Chat;
import fr.skyforce77.towerminer.multiplayer.MPInfos;
import fr.skyforce77.towerminer.multiplayer.ProtocolManager;
import fr.skyforce77.towerminer.particles.ParticleEffect;
import fr.skyforce77.towerminer.protocol.BigSending;
import fr.skyforce77.towerminer.protocol.Connect;
import fr.skyforce77.towerminer.protocol.ObjectReceiver;
import fr.skyforce77.towerminer.protocol.packets.Packet12Popup;
import fr.skyforce77.towerminer.protocol.packets.Packet13EntityTeleport;
import fr.skyforce77.towerminer.protocol.packets.Packet1Disconnecting;
import fr.skyforce77.towerminer.protocol.packets.Packet20EntityData;
import fr.skyforce77.towerminer.protocol.packets.Packet3Action;
import fr.skyforce77.towerminer.protocol.packets.Packet4RoundFinished;
import fr.skyforce77.towerminer.protocol.packets.Packet5UpdateInfos;
import fr.skyforce77.towerminer.protocol.packets.Packet6EntityCreate;
import fr.skyforce77.towerminer.protocol.packets.Packet7EntityMove;
import fr.skyforce77.towerminer.protocol.packets.Packet8EntityRemove;
import fr.skyforce77.towerminer.protocol.packets.Packet9MouseClick;
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
    public JRadioButton enablechat;
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
                next.setText(LanguageManager.getText("menu.mp.ready.button"));

                if (server) {
                    serverready = true;
                    new Packet12Popup("menu.mp.ready", player).sendAllTCP();
                    if (serverready && clientready) {
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
        chatfield.setPreferredSize(new Dimension(100, 30));
        chatfield.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!chatfield.getText().equals("")) {
                    chat.onMessageWritten(player, chatfield.getText());
                    typed.add(chatfield.getText());
                    select = typed.size();
                    chatfield.setText("");
                }
            }
        });
        
        enablechat = new JRadioButton();
        enablechat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                chat.changeState(enablechat.isSelected());
            }
        });
        
        chatfield.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (select - 1 >= 0) {
                        chatfield.setText(typed.get(select - 1));
                        select--;
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (select + 1 < typed.size() && typed.get(select + 1) != null) {
                        chatfield.setText(typed.get(select + 1));
                        select++;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
        chatfield.setToolTipText(LanguageManager.getText("menu.mp.message"));
        TowerMiner.game.add(chatfield);
        TowerMiner.game.add(enablechat);

        chatfield.setVisible(false);
        enablechat.setVisible(false);
    }
    
    @Override
    public void onTick() {
    	for (Entity en : draw) {
            try {
            	if(en instanceof EntityProjectile)
            		en.onTick();
            	if(en instanceof Turret)
            		((Turret)en).onClientTick();
            } catch (Exception e) {}
        }
    	super.onTick();
    }

    @Override
    public void drawMenu(Graphics2D g2d) {
        super.drawMenu(g2d);
        
        if(gameover) {
        	boolean team = Maps.getActualMap().getDeathPoints()[0].equals(Maps.getActualMap().getDeathPoints()[1]);
            if (team) {
                new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.survive", round - 1)).sendAllTCP();
                if (server) {
                    new ProtocolManager().onServerReceived(MPInfos.connection, new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.survive", round - 1)));
                } else {
                    new ProtocolManager().onClientReceived(MPInfos.connection, new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.survive", round - 1)));
                }
            } else {
                new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.lose", round)).sendAllTCP();
                if (server) {
                    new ProtocolManager().onClientReceived(MPInfos.connection, new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.win")));
                } else {
                    new ProtocolManager().onServerReceived(MPInfos.connection, new Packet1Disconnecting(LanguageManager.getText("menu.mp.gameover.win")));
                }
            }
        }

        chat.draw(g2d, this);
    }

    @Override
    public void onUnused() {
        super.onUnused();

        if (server) {
            Connect.server.stop();
        } else {
            Connect.client.stop();
        }
        MPInfos.matchplaying = false;

        chatfield.setVisible(false);
        enablechat.setVisible(false);

        if (chat.opened) {
            chat.changeState(false);
        }
    }

    @Override
    public void onUsed() {
        super.onUsed();
        next.setText(LanguageManager.getText("menu.mp.ready.button"));
        countNext();
        speed.setVisible(false);
        chatfield.setVisible(true);
        enablechat.setVisible(true);
        pause.setVisible(false);
        options.setVisible(false);

        TowerMiner.game.setTitle(LanguageManager.getText("towerminer") + " | " + Game.version + " | " + LanguageManager.getText("menu.multiplayer") + " | " + LanguageManager.getText("menu.editor.map") + ": " + Maps.getActualMap().getName() + " | " + LanguageManager.getText(player));

        new Thread() {
            public void run() {
                while (true) {
                    if ((Connect.client != null && !Connect.client.isConnected()) || (Connect.server != null && !MPInfos.connection.isConnected())) {
                        if (TowerMiner.menu instanceof MultiPlayer) {
                            MPDisconnected d = new MPDisconnected();
                            d.reason = LanguageManager.getText("menu.mp.connection.lost");
                            if (server) {
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
            }

            ;
        }.start();
    }

    @Override
    public void onFinishedRound() {

        if (clientlastlife == clientlife) {
            clientlife = clientlife + 10;
        }
        clientlastlife = clientlife;

        Packet4RoundFinished pr = new Packet4RoundFinished();
        pr.gold = clientgold;
        pr.life = clientlife;
        pr.round = round;
        pr.sendAllTCP();

        new Thread() {
        	public void run() {
                if (server) {
                    for (Entity en : entity) {
                        sendData(en);
                        try {
        					Thread.sleep(50l);
        				} catch (Exception e) {}
                    }
                }
        	};
        }.start();
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
    public void onEntityAdded(Entity en) {
        if (server) {
        	if(en instanceof Mob) {
        		new Packet6EntityCreate(en.getUUID(), en.getType().getId()).sendAllTCP();
        	} else if(en instanceof EntityProjectile) {
        		new Packet6EntityCreate(en.getUUID(), en.getType().getId(), ((EntityProjectile) en).getShooter(), ((EntityProjectile) en).getAimed()).sendAllTCP();
        	} else if(en instanceof Turret) {
        		new Packet6EntityCreate(en.getUUID(), en.getType().getId(), en.getBlockLocation(), ((Turret) en).getOwner()).sendAllTCP();
        	}
        	sendData(en);
        }
    }
    
    public void sendData(final Entity en) {
    	new Thread() {
            @Override
            public void run() {
                BigSending.sendBigObject(en, MPInfos.connection, new ObjectReceiver.ReceivingThread() {
                    @Override
                    public void run(int objectid) {
                        Packet20EntityData pe = new Packet20EntityData();
                        pe.eid = objectid;
                        pe.sendAllTCP();
                    }
                });
            }
        }.start();
    }

    @Override
    public void onEntityMove(Entity en, Point p, Point to) {
        if (server) {
            Packet7EntityMove pem = new Packet7EntityMove();
            pem.entity = en.getUUID();
            pem.x = (int) p.getX();
            pem.y = (int) p.getY();
            pem.xto = (int) to.getX();
            pem.yto = (int) to.getY();
            pem.rotation = en.getRotation();
            pem.sendAllUDP();
        }
    }

    @Override
    public void onEntityTeleport(Entity en, Point p) {
        if (server) {
            Packet13EntityTeleport pem = new Packet13EntityTeleport();
            pem.entity = en.getUUID();
            pem.x = (int) p.getX();
            pem.y = (int) p.getY();
            pem.rotation = en.getRotation();
            pem.sendAllUDP();
        }
    }

    @Override
    public void onEntityRemoved(final Entity en) {
        if (server) {
            final Packet8EntityRemove per = new Packet8EntityRemove();
            per.entity = en.getUUID();
            per.sendAllTCP();
            if(en instanceof Mob)
            	ParticleEffect.createMobDestructionParticles(en.getType(), this, (int)en.getLocation().getX(), (int)en.getLocation().getY());
        }
    }

    @Override
    public void onMouseClicked(MouseEvent e) {
        if (server) {
            super.onMouseClicked(e);
        } else {
        	if (items != null && paused) {
                for (MenuItem item : items) {
                    if (item != null && item.isEnabled())
                        if (Xcursor > item.getX() && Xcursor < item.getX() + item.getWidth()
                                && Ycursor > item.getY() && Ycursor < item.getY() + item.getHeight()) {
                            item.run();
                            return;
                        }
                }
            }
            if (e.getModifiers() == 16) {
                if (gameover) {
                	TowerMiner.setMenu(Menu.mainmenu);
                } else if (aimed == null) {
                    Point p = new Point(Xcursor / MapWritter.getBlockWidth() - (CanvasX / MapWritter.getBlockWidth()), Ycursor / MapWritter.getBlockHeight() - (CanvasY / MapWritter.getBlockHeight()));
                    boolean turretplaced = false;
                    for (Entity en : draw) {
                        if (en instanceof Turret && en.getBlockLocation().equals(new Point(p.x, p.y - 1))) {
                            turretplaced = true;
                            if (((Turret) en).isOwner(getPlayer()))
                                aimed = (Turret) en;
                        }
                    }

                    if (p.y - 1 > -1 && !turretplaced && EntityTypes.turrets.get(selectedturret).getPrice() <= or && Maps.getActualMap().canPlaceTurret(p.x, p.y - 1)) {
                        Packet9MouseClick pm = new Packet9MouseClick();
                        pm.modifier = e.getModifiers();
                        pm.x = p.x;
                        pm.y = p.y;
                        pm.selected = EntityTypes.turrets.get(selectedturret).getId();
                        pm.sendAllTCP();
                    }
                } else {
                    aimed = null;
                }
            } else if (aimed != null) {
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
        or += add;
        clientgold += add;
        orgained += add;

        Packet5UpdateInfos pr = new Packet5UpdateInfos();
        pr.gold = clientgold;
        pr.life = clientlife;
        pr.sendAllTCP();
    }

    @Override
    public void onGameOver() {
    	chatfield.setVisible(false);
        gameover = true;
    }

    @Override
    public void setPlayer(String player) {
        super.setPlayer(player);
        TowerMiner.game.setTitle(LanguageManager.getText("towerminer") + " | " + Game.version + " | " + LanguageManager.getText("menu.multiplayer") + " | " + LanguageManager.getText("menu.editor.map") + ": " + Maps.getActualMap().getName() + " | " + LanguageManager.getText(player));
    }
}
