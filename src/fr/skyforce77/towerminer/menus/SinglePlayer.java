package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.entity.Entity;
import fr.skyforce77.towerminer.entity.EntityTypes;
import fr.skyforce77.towerminer.entity.Mob;
import fr.skyforce77.towerminer.entity.Turret;
import fr.skyforce77.towerminer.game.Game;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class SinglePlayer extends Menu {

	public int CanvasX = 0;
	public int CanvasY = 40;
	public int CANVAS_WIDTH;
	public int CANVAS_HEIGHT;

	public boolean terminated = false;
	public boolean gameover = false;
	public boolean paused = false;
	public boolean started = false;

	public int Xblocks;
	public int Yblocks;

	public int round;
	public int lastvie;
	public int vie;
	public int or;
	public int orgained;

	int tospawn;
	int spawned;
	int between;
	int time;

	public CopyOnWriteArrayList<Mob> entities = new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<Turret> turrets = new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<Entity> removed = new CopyOnWriteArrayList<>();
	public CopyOnWriteArrayList<Entity> draw = new CopyOnWriteArrayList<>();

	public JMenuItem pause;
	public JButton next;
	public JCheckBox speed;
	public JMenu options;
	public JMenuBar bar;

	public boolean multiplayer;
	public boolean renderarrow = true;

	int selectedturret = 0;
	Turret aimed = null;

	public SinglePlayer(boolean multi) {
		items = new MenuItem[3];
		canreturn = false;
		multiplayer = multi;

		onInit();

		round = 0;
		tospawn = 5;
		spawned = 0;
		between = 30;
		time = 30;
		lastvie = 20;
		vie = 20;
		or = 60;
		orgained = 0;

		bar = new JMenuBar();

		options = new JMenu(LanguageManager.getText("menu.options"));
		bar.add(options);
		options.setVisible(false);

		next = new JButton(LanguageManager.getText("menu.sp.next"));
		bar.add(next);
		next.setVisible(false);

		if(!multi)
			next.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					started = true;
					next.setEnabled(false);
				}
			});

		pause = new JMenuItem(LanguageManager.getText("menu.sp.pause"));
		options.add(pause);
		pause.setVisible(false);

		pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				paused = true;
			}
		});

		speed = new JCheckBox(LanguageManager.getText("menu.sp.boost"));
		options.add(speed);
		speed.setVisible(false);

		TowerMiner.game.add(bar);

		items[0] = new MenuItem(0, LanguageManager.getText("menu.sp.resume"), new Thread(){
			@Override
			public void run() {
				paused = false;
			}
		});

		items[1] = new MenuItem(1, LanguageManager.getText("menu.main"), new Thread(){
			@Override
			public void run() {
				TowerMiner.setMenu(Menu.mainmenu);
			}
		});

		items[2] = new MenuItem(2, LanguageManager.getText("menu.quit"), new Thread(){
			@Override
			public void run() {
				TowerMiner.game.setVisible(false);
				TowerMiner.game.dispose();
			}
		});
	}

	public void onInit() { 
		Xblocks = Maps.getActualMap().getXMax();
		Yblocks = Maps.getActualMap().getYMax();
		CANVAS_WIDTH = Xblocks*MapWritter.getBlockWidth()+CanvasX;
		CANVAS_HEIGHT = Yblocks*MapWritter.getBlockHeight()+CanvasY;
		gameover = false;
	}

	public void addGold(int add) {
		or+=add;
		orgained+=add;
	}

	public void onShutdown() { 
		terminated = true;
	}

	public void onTick() {
		for(Entity en : removed) {
			if(en instanceof Mob) {
				entities.remove(en);
			} else {
				turrets.remove(en);
			}
			onEntityRemoved(en);
		}
		removed = new CopyOnWriteArrayList<>();

		if(vie <= 0) {
			gameover = true;
			started = false;

			onGameOver();
		}

		if(!paused && started) {
			onUpdate();
		}

		super.onTick();
	}

	public void onUpdate() {
		if(spawned != tospawn) {
			if(between == 0) {
				EntityTypes type = EntityTypes.mobs[new Random().nextInt(EntityTypes.mobs.length)];
				if(type.getLevel() <= tospawn-spawned) {
					Mob m = new Mob(type);
					entities.add(m);
					spawned+=type.getLevel();
					time = (int)(30-(0.5*round));
					between = time;
					onEntityAdded(m);
				}
			} else {
				between--;
			}
		}

		if(spawned == tospawn && entities.isEmpty()) {
			round++;
			spawned = 0;
			tospawn = tospawn+(tospawn/5);
			next.setEnabled(true);
			addGold((int)(20*(round*0.2)));
			started = false;

			if(lastvie == vie) {
				vie = vie+10;
			}
			lastvie = vie;

			onFinishedRound();
		}

		try {
			for(Entity en : entities) {
				en.onTick();
			}
			for(Entity en : turrets) {
				en.onTick();
			}
		} catch(Exception e) {}
	}

	public void drawMenu(Graphics2D g2d) {
		MapWritter.drawCanvas(g2d, CanvasX, CanvasY);

		if(gameover) {
			onUnused();
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
			g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 40));
			g2d.setColor(new Color(0, 0, 0, 180));
			g2d.fillRect(TowerMiner.game.getWidth()/2-150, 150, 300, 70);
			g2d.setColor(new Color(0, 0, 20, 140));
			g2d.fillRect(TowerMiner.game.getWidth()/2-150, 150+70, 300, 80);
			g2d.setColor(Color.WHITE);
			g2d.drawString(LanguageManager.getText("menu.sp.gameover"), TowerMiner.game.getWidth()/2-120, 200);
			g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 20));
			g2d.drawString(LanguageManager.getText("menu.sp.rounds.win",(round-1)), TowerMiner.game.getWidth()/2-150, 250);
			g2d.drawString(LanguageManager.getText("menu.sp.golds.win",orgained), TowerMiner.game.getWidth()/2-150, 280);
			return;
		}

		if(!paused && cursorinwindow && (Ycursor+8)/MapWritter.getBlockHeight()>0) {
			Point p = new Point(Xcursor/MapWritter.getBlockWidth()-(CanvasX/MapWritter.getBlockWidth()),Ycursor/MapWritter.getBlockHeight()-(CanvasY/MapWritter.getBlockHeight()));
			boolean turretplaced = false;
			for(Entity en : turrets) {
				if(en.getBlockLocation().equals(new Point(p.x,p.y-1))) {
					turretplaced = true;
				}
			}
			for(Entity en : draw) {
				if(en != null && en.getBlockLocation().equals(new Point(p.x,p.y-1)) && en instanceof Turret) {
					turretplaced = true;
				}
			}
			if(EntityTypes.turrets[selectedturret].getPrice() <= or && !turretplaced && Maps.getActualMap().canPlaceTurret((Xcursor/MapWritter.getBlockWidth()),((Ycursor+8)/MapWritter.getBlockHeight())-1)) {
				g2d.setColor(Color.GREEN);
			} else {
				g2d.setColor(Color.RED);
			}
			int xl = (Xcursor/MapWritter.getBlockWidth())*MapWritter.getBlockWidth();
			int yl = ((Ycursor+8)/MapWritter.getBlockHeight())*MapWritter.getBlockHeight()-8;
			g2d.draw3DRect(xl, yl, MapWritter.getBlockWidth()-1, MapWritter.getBlockHeight()-1, false);
			g2d.draw3DRect(xl+1, yl+1, MapWritter.getBlockWidth()-3, MapWritter.getBlockHeight()-3, false);
		}

		for(Mob en : entities) {
			if(en != null) {
				en.draw(g2d, this);
			}
		}
		for(Entity en : draw) {
			if(en != null && en instanceof Mob) {
				en.draw(g2d, this);
			}
		}

		if(aimed != null) {
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillOval((int)(aimed.getLocation().getX()-aimed.getDistance()), (int)(aimed.getLocation().getY()-aimed.getDistance())+CanvasY, (int)aimed.getDistance()*2, (int)aimed.getDistance()*2);
		}

		for(Entity en : turrets) {
			if(en != null) {
				en.draw(g2d, this);
			}
		}
		for(Entity en : draw) {
			if(en != null && en instanceof Turret) {
				en.draw(g2d, this);
			}
		}

		if(aimed == null) {
			g2d.drawImage(EntityTypes.turrets[selectedturret].getTexture(0),Xcursor-16,Ycursor-16,16,16,null);
			g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 12));
			FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
			int hgt = metrics.getHeight();
			int adv = metrics.stringWidth(LanguageManager.getText("menu.sp.buy")+": "+EntityTypes.turrets[selectedturret].getPrice());
			Dimension size = new Dimension(adv+2, hgt+2);
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(Xcursor, Ycursor-16, (int)(4+size.getWidth()), 16);
			g2d.setColor(Color.WHITE);
			g2d.drawString(LanguageManager.getText("menu.sp.buy")+": "+EntityTypes.turrets[selectedturret].getPrice(), Xcursor+3, Ycursor-3);
		} else {
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(Xcursor-10, Ycursor-35, 120, 40);
			g2d.setColor(Color.WHITE);
			g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 12));
			g2d.drawString("("+LanguageManager.getText("menu.sp.click.right")+")", Xcursor, Ycursor-15);
			g2d.drawString(LanguageManager.getText("menu.sp.improve")+": "+aimed.getPrice(), Xcursor, Ycursor);
		}

		if(paused) {
			g2d.setColor(new Color(0, 0, 0, 150));
			g2d.fillRect(0, 0, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
			g2d.rotate(-0.1, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
			g2d.fillRect(TowerMiner.game.getWidth()-240, TowerMiner.game.getHeight()-100, TowerMiner.game.getWidth(), TowerMiner.game.getWidth());
			g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 40));
			g2d.setColor(Color.WHITE);
			g2d.drawString(LanguageManager.getText("menu.sp.paused"), TowerMiner.game.getWidth()-210, TowerMiner.game.getHeight()-60);
			g2d.rotate(0.1, TowerMiner.game.getWidth(), TowerMiner.game.getHeight());
			super.drawMenu(g2d);
		}

		if(!started && !paused) {
			if(renderarrow) {
				int xt = Maps.getActualMap().getXDepart()*MapWritter.getBlockWidth();
				int yt = Maps.getActualMap().getYDepart()*MapWritter.getBlockHeight();

				if(Maps.getActualMap().getRawRotation() == 0) {xt = xt+(xmove/8);}
				else if(Maps.getActualMap().getRawRotation() == 1) {yt = yt+(xmove/8);}
				else if(Maps.getActualMap().getRawRotation() == 2) {xt = xt-(xmove/8);}
				else {yt = yt-(xmove/8);}

				g2d.rotate(Maps.getActualMap().getDepartRotation(), xt+MapWritter.getBlockWidth()/2,yt+40+MapWritter.getBlockWidth()/2);
				g2d.drawImage(RessourcesManager.getTexture("fleche"),xt,yt+CanvasY,MapWritter.getBlockWidth(),MapWritter.getBlockHeight(),null);
				g2d.rotate(-Maps.getActualMap().getDepartRotation(), xt+MapWritter.getBlockWidth()/2,yt+CanvasY+MapWritter.getBlockHeight()/2);
			}
		}

		if(TowerMiner.game.popup != null && TowerMiner.game.popup.time+TowerMiner.game.popup.displayed > new Date().getTime()) {
			TowerMiner.game.popup.draw(g2d, CanvasY);
		}

		g2d.setColor(new Color(100,100,100));
		g2d.fillRect(0, 0, TowerMiner.game.getWidth()-CanvasX, CanvasY);

		g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 18));
		g2d.setColor(Color.WHITE);
		FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
		int hgt = metrics.getHeight();
		int adv = metrics.stringWidth(LanguageManager.getText("menu.sp.round")+": "+round);
		Dimension size = new Dimension(adv+2, hgt+2);
		g2d.drawString(LanguageManager.getText("menu.sp.round")+": "+round, 10, 25);
		g2d.drawString(LanguageManager.getText("menu.sp.golds")+": "+or, (int)(45+size.getWidth()), 25);
		g2d.drawString(LanguageManager.getText("menu.sp.life")+": "+vie, TowerMiner.game.getWidth()-110, 25);
		
		Point p = new Point(Xcursor/MapWritter.getBlockWidth()-(CanvasX/MapWritter.getBlockWidth()),Ycursor/MapWritter.getBlockHeight()-(CanvasY/MapWritter.getBlockHeight()));
		for(Entity en : turrets) {
			if(en.getBlockLocation().equals(new Point(p.x,p.y-1))) {
				en.drawInformations(g2d, this);
			}
		}
		for(Entity en : draw) {
			if(en.getBlockLocation().equals(new Point(p.x,p.y-1))) {
				en.drawInformations(g2d, this);
			}
		}

	}

	public void onKeyPressed(int keyCode) {
		if(keyCode == KeyEvent.VK_ESCAPE || keyCode == KeyEvent.VK_CLEAR) {
			paused = !paused;
		}

		if(!multiplayer && gameover && keyCode == KeyEvent.VK_ENTER) {
			TowerMiner.setMenu(Menu.mainmenu);
		}

		if(!paused) {
			if(keyCode == KeyEvent.VK_Z || keyCode == KeyEvent.VK_UP) {
				selectedturret = EntityTypes.getNextVisibleTurret(selectedturret);
			} else if(keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
				selectedturret = EntityTypes.getPreviousVisibleTurret(selectedturret);
			}
		}
		super.onKeyPressed(keyCode);
	}

	public void windowClosing(WindowEvent e) {onShutdown();}

	@Override
	public void onWindowDeiconified(WindowEvent e) {paused = false;}

	@Override
	public void onWindowIconified(WindowEvent e) {paused = true;}

	@Override
	public void onMouseClicked(MouseEvent e) {
		if(e.getModifiers() == 16) {
			if(paused) {
				super.onMouseClicked(e);
			} else if(gameover) {
				onKeyPressed(KeyEvent.VK_ENTER);
			} else if(aimed == null){
				Point p = new Point(Xcursor/MapWritter.getBlockWidth()-(CanvasX/MapWritter.getBlockWidth()),Ycursor/MapWritter.getBlockHeight()-(CanvasY/MapWritter.getBlockHeight()));
				boolean turretplaced = false;
				for(Entity en : turrets) {
					if(en.getBlockLocation().equals(new Point(p.x,p.y-1))) {
						turretplaced = true;
						if(((Turret)en).isOwner(getId()))
							aimed = (Turret)en;
					}
				}

				if(p.y-1 > -1 && !turretplaced && EntityTypes.turrets[selectedturret].getPrice() <= or && Maps.getActualMap().canPlaceTurret(p.x,p.y-1)) {
					or -= EntityTypes.turrets[selectedturret].getPrice();
					EntityTypes type = EntityTypes.turrets[selectedturret];
					try {
						Turret tu = (Turret)type.getEntityClass().getConstructor(EntityTypes.class, Point.class, String.class).newInstance(EntityTypes.turrets[selectedturret], new Point(p.x,p.y-1), getId());
						turrets.add(tu);
						onEntityAdded(tu);
					} catch (Exception ex) {}
				}
			} else {
				aimed = null;
			}
		} else if(e.getModifiers() == 4) {
			if(aimed != null && aimed.getPrice() <= or) {
				or -= aimed.getPrice();
				aimed.addData();
			}
		} else if(e.getModifiers() == 8) {
			if(aimed != null) {
				or += (aimed.getCost()*0.75);
				removed.add(aimed);
				aimed = null;
			}
		}
	}

	@Override
	public void onMouseEntered(MouseEvent e) {cursorinwindow = true;}

	@Override
	public void onMouseExited(MouseEvent e) {cursorinwindow = false;}

	@Override
	public void onUnused() {
		speed.setVisible(false);
		next.setVisible(false);
		pause.setVisible(false);
		options.setVisible(false);

		if(this.getClass().equals(SinglePlayer.class)) {
			Menu.singleplayer = new SinglePlayer(false);
		}
	}

	@Override
	public void onUsed() {
		speed.setVisible(true);
		next.setVisible(true);
		pause.setVisible(true);
		options.setVisible(true);
		Maps.resize();
		TowerMiner.game.setTitle(LanguageManager.getText("towerminer")+" | "+Game.version+" | "+LanguageManager.getText("menu.match")+" | "+LanguageManager.getText("menu.editor.map")+": "+Maps.getActualMap().getName());
	}

	public void onFinishedRound() {}

	public void onEntityMove(Entity en, Point p, Point loc) {}

	public void onEntityTeleport(Entity en, Point p) {}

	public void onEntityAdded(Entity en) {}

	public void onEntityRemoved(Entity en) {}

	public void onGameOver() {}

	public String getId() {
		return "server";
	}
}
