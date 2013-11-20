package fr.skyforce77.towerminer.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import javax.swing.JFrame;
import javax.swing.JPanel;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.achievements.Popup;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MultiPlayer;
import fr.skyforce77.towerminer.menus.SinglePlayer;
import fr.skyforce77.towerminer.multiplayer.Connect;
import fr.skyforce77.towerminer.multiplayer.packets.Packet12Popup;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class Game extends JFrame implements MouseListener,MouseWheelListener,MouseMotionListener,WindowListener,KeyListener{

	private static final long serialVersionUID = 1L;

	public String version = "Alpha 0.4e";
	public int connectversion = 4 ;
	public boolean fpsdisplay = false;

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
	
	public Popup popup;

	public Game() {
		onInit();
		
		canvas = new GameCanvas();
		canvas.addKeyListener(this);
		canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
		this.setContentPane(canvas);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setTitle(LanguageManager.getText("towerminer")+" | "+version);
		this.setIconImage(RessourcesManager.getIcon());
		this.setVisible(true);
		this.setResizable(false);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.addMouseWheelListener(this);
		this.addKeyListener(this);
		
		new Thread(){
			public void run() {
				try {
					onStart();
				} catch(Exception e) {}
			};
		}.start();
		
		new Thread(){
			public void run() {
				try {
					while(!terminated) {
						sleep(1000l);
						fps = frames;
						frames = 0;
					}
				} catch(Exception e) {}
			};
		}.start();
	}

	public void onInit() { 
		Xblocks = Maps.getActualMap().getXMax();
		Yblocks = Maps.getActualMap().getYMax();
		CANVAS_WIDTH = Xblocks*48+CanvasX;
		CANVAS_HEIGHT = Yblocks*48+CanvasY;
	}

	public void onStart() throws InterruptedException {
		boolean refresh = true;
		while (!terminated) {
			if(TowerMiner.menu != null) {
				TowerMiner.menu.onTick();
			}
			
			if(refresh) {
				repaint();
				frames++;
			}

			if(TowerMiner.menu instanceof SinglePlayer && ((SinglePlayer)TowerMiner.menu).speed.isSelected()) {
				Thread.sleep(10l);
				refresh = !refresh;
			} else {
				Thread.sleep(20l);
				refresh = true;
			}
		}
	}

	public class GameCanvas extends JPanel{

		private static final long serialVersionUID = 1L;

		public GameCanvas() {
			setFocusable(true);
			requestFocus();
		}

		@Override
		public void paintComponent(Graphics g) {
			Graphics2D g2d = (Graphics2D)g;
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			super.paintComponent(g2d);
			setBackground(Color.WHITE);
			TowerMiner.menu.drawMenu(g2d);
			g2d.setColor(new Color(0,0,0,180));
			if(fpsdisplay) {
				g2d.fillRect(0, 0, 30, 25);
				g2d.setColor(Color.YELLOW);
				g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 20));
				g2d.drawString(fps+"", 0, 20);
			}
		}
	}
	
	public void displayPopup(Popup popup) {
		this.popup = popup;
	}
	
	public void displayMultiPlayerPopup(Popup popup) {
		if(TowerMiner.menu instanceof MultiPlayer) {
			MultiPlayer mp = (MultiPlayer)TowerMiner.menu;
			if(mp.server) {
				Connect.c.sendTCP(new Packet12Popup(popup.text, null, popup.icon));
				this.popup = popup;
			}
		} else {
			this.popup = popup;
		}
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
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
		TowerMiner.menu.onMouseDragged(arg0);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
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