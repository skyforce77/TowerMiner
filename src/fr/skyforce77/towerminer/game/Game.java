package fr.skyforce77.towerminer.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.api.PluginManager;
import fr.skyforce77.towerminer.api.events.chat.PopupDisplayedEvent;
import fr.skyforce77.towerminer.blocks.renders.BlockRender;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.protocol.packets.Packet12Popup;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;
import fr.skyforce77.towerminer.sounds.Music;

public class Game extends JFrame implements MouseListener, MouseWheelListener, MouseMotionListener, WindowListener, KeyListener {

	private static final long serialVersionUID = 1L;

	public static String version = TowerMiner.version;
	public boolean fpsdisplay = false;
	public static boolean offline = false;

	public int CanvasX = 0;
	public int CanvasY = 40;
	public int CANVAS_WIDTH;
	public int CANVAS_HEIGHT;

	public boolean terminated = false;

	public int Xblocks;
	public int Yblocks;

	public GameCanvas canvas;

	public int frames = 0;
	public int fps = 0;
	public int freeze = 25;

	public Popup popup;
	public HashMap<Popup, Boolean> nextpopups = new HashMap<Popup, Boolean>();

	private int[] code = new int[] {38, 38, 40, 40, 37, 39, 37, 39, 66, 65};
	private int progress = 0;
	private boolean konami = false;
	private boolean stroboscope = false;
	private int alpha = 0;
	private int konamirot = 0;

	public Game() {
		onInit();

		canvas = new GameCanvas();
		canvas.addKeyListener(this);
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		this.setContentPane(canvas);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setTitle(LanguageManager.getText("towerminer") + " | " + version);
		this.setIconImage(RessourcesManager.getIcon());
		this.setVisible(false);
		this.setResizable(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addWindowListener(this);
		this.addKeyListener(this);
		this.setLocationRelativeTo(null);

		onStart();

		new Thread() {
			public void run() {
				try {
					while (!terminated) {
						sleep(1000l);
						fps = frames;
						frames = 0;

						if (fps < 40) {
							freeze--;
						} else if (fps > 40) {
							freeze++;
						}
					}
				} catch (Exception e) {
				}
			}

			;
		}.start();
	}

	public void onInit() {
		Xblocks = Maps.getActualMap().getXMax();
		Yblocks = Maps.getActualMap().getYMax();
		CANVAS_WIDTH = Xblocks * 48 + CanvasX;
		CANVAS_HEIGHT = Yblocks * 48 + CanvasY;
	}

	public void onStart() {
		new Thread() {
			public void run() {
				while (!terminated) {
					if(popup != null) {
						int dif = (int) ((popup.time + popup.displayed) - new Date().getTime());
						if(dif <= 0) {
							popup = null;
						}
					}
					if(popup == null) {
						if(!nextpopups.isEmpty()) {
							PopupDisplayedEvent pde = new PopupDisplayedEvent((Popup)nextpopups.keySet().toArray()[0]);
							PluginManager.callEvent(pde);
							if(!pde.isCancelled()) {
								popup = pde.getPopup();
								if (TowerMiner.menu instanceof MultiPlayer && nextpopups.get(popup)) {
									MultiPlayer mp = (MultiPlayer) TowerMiner.menu;
									if (mp.server) {
										new Packet12Popup(popup.text, null, popup.icon).sendClientTCP();
									}
								}
								popup.time = new Date().getTime();
							}
							nextpopups.remove(popup);
						}
					}
					if (TowerMiner.menu != null) {
						TowerMiner.menu.onTick();
						for (BlockRender r : BlockRender.renders) {
							if (r != null) {
								r.onGameTick();
							}
						}
					}

					try {
						if (TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer) TowerMiner.menu).speed.isSelected()) {
							Thread.sleep(10l);
						} else {
							Thread.sleep(20l);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			;
		}.start();

		new Thread() {
			public void run() {
				while (!terminated) {
					repaint();
					frames++;

					try {
						Thread.sleep(freeze);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			;
		}.start();
	}

	public class GameCanvas extends JPanel {

		private static final long serialVersionUID = 1L;

		public GameCanvas() {
			setFocusable(true);
			requestFocus();
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			super.paintComponent(g2d);
			setBackground(Color.WHITE);
			g2d = (Graphics2D)g2d.create();
			if(stroboscope || konami) {
				if(konamirot != 0) {
					g2d.translate(0,getSize().getHeight());
					g2d.rotate(Math.toRadians(konamirot),getSize().getWidth()/2,0);
				}
			}
			TowerMiner.menu.drawMenu(g2d);
			g2d.setColor(new Color(0, 0, 0, 180));
			if (fpsdisplay) {
				g2d.fillRect(0, 0, 30, 25);
				g2d.setColor(Color.YELLOW);
				g2d.setFont(TowerMiner.getFont(20));
				g2d.drawString(fps + "", 0, 20);
			}
			if(stroboscope) {
				g2d = (Graphics2D) g.create();
				g2d.rotate(Math.toRadians(konamirot),getSize().getWidth()/2,getSize().getHeight()/2);
				g2d.setColor(new Color(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255), alpha));
				g2d.fillRect(-2000, -2000, 4000, 4000);
				g2d.setColor(new Color(0, 0, 0, alpha));
				g2d.setFont(TowerMiner.getFont(48));
				FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
				int hgt = metrics.getHeight();
				int adv = metrics.stringWidth("Konami");
				Dimension size = new Dimension(adv + 2, hgt + 2);
				g2d.drawString("Konami", (float)(TowerMiner.game.getWidth()/2-size.getWidth()/2), (float)(TowerMiner.game.getHeight()/2));
			}
		}
	}

	public void displayPopup(Popup popup) {
		if(this.popup == null) {
			this.popup = popup;
			this.popup.time = new Date().getTime();
		} else {
			this.nextpopups.put(popup, false);
		}
	}

	public void displayMultiPlayerPopup(Popup popup) {
		if(this.popup == null) {
			if (TowerMiner.menu instanceof MultiPlayer) {
				MultiPlayer mp = (MultiPlayer) TowerMiner.menu;
				if (mp.server) {
					new Packet12Popup(popup.text, null, popup.icon).sendClientTCP();
					this.popup = popup;
				}
			} else {
				this.popup = popup;
			}
			this.popup.time = new Date().getTime();
		} else {
			this.nextpopups.put(popup,  false);
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode() == code[progress]) {
			progress++;
			if(progress == code.length) {
				progress = 0;
				new Thread() {
					@Override
					public void run() {
						try {
							Thread.sleep(100l);
							stroboscope = true;
							int i = 0;
							while(i < 255) {
								alpha = i;
								Thread.sleep(20l);
								i++;
							}
							konami = !konami;
							if(konamirot > 0) {
								int u = 0;
								while(u < 180) {
									konamirot--;
									Thread.sleep(15l);
									u++;
								}
							} else {
								int u = 0;
								while(u < 180) {
									konamirot++;
									Thread.sleep(15l);
									u++;
								}
							}
							Thread.sleep(100l);
							while(i > 0) {
								alpha = i;
								Thread.sleep(20l);
								i--;
							}
							stroboscope = false;
						} catch (InterruptedException e) {}
					}
				}.start();
			}
		} else if(!stroboscope) {
			progress = 0;
		}
		TowerMiner.menu.onKeyPressed(arg0.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		TowerMiner.menu.onKeyReleased(arg0.getKeyCode());
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		TowerMiner.menu.onKeyTyped(arg0.getKeyChar());
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		TowerMiner.menu.onWindowActivated(arg0);
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		TowerMiner.menu.onWindowClosed(arg0);
		int i = 0;
		while(i < 10) {
			Music.stop(i);
			i++;
		}
		System.exit(1);
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		TowerMiner.menu.onWindowClosing(arg0);
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		TowerMiner.menu.onWindowDeactivated(arg0);
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		TowerMiner.menu.onWindowDeiconified(arg0);
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		TowerMiner.menu.onWindowIconified(arg0);
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		TowerMiner.menu.onWindowOpened(arg0);
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(konami) {
			arg0 = new MouseEvent(this, arg0.getID(), arg0.getWhen(), arg0.getModifiers(), (int)(getSize().getWidth()-arg0.getX()), (int)(getSize().getHeight()-arg0.getY())
					, arg0.getXOnScreen(), arg0.getYOnScreen(), arg0.getClickCount(), arg0.isPopupTrigger(), arg0.getButton());
		}
		TowerMiner.menu.onMouseDragged(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		if(konami) {
			arg0 = new MouseEvent(this, arg0.getID(), arg0.getWhen(), arg0.getModifiers(), (int)(getSize().getWidth()-arg0.getX()), (int)(getSize().getHeight()-arg0.getY())
					, arg0.getXOnScreen(), arg0.getYOnScreen(), arg0.getClickCount(), arg0.isPopupTrigger(), arg0.getButton());
		}
		TowerMiner.menu.onMouseMoved(arg0);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		TowerMiner.menu.onMouseWheelMoved(arg0);
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		TowerMiner.menu.onMouseClicked(arg0);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		TowerMiner.menu.onMouseEntered(arg0);
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		TowerMiner.menu.onMouseExited(arg0);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		TowerMiner.menu.onMousePressed(arg0);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		TowerMiner.menu.onMouseReleased(arg0);
	}
}
