package fr.skyforce77.towerminer.menus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.Date;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.additionals.BlockCreator;
import fr.skyforce77.towerminer.menus.additionals.BlockSelectionList;
import fr.skyforce77.towerminer.menus.additionals.ColorSelection;
import fr.skyforce77.towerminer.menus.additionals.JNumberTextField;
import fr.skyforce77.towerminer.ressources.RessourcesManager;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class MapEditor extends Menu{

	public JMenuBar bar;

	public int selected = 1;
	public int data = 0;

	public JTextField name;
	public JTextField width;
	public JTextField height;
	public JMenuItem color;
	public JMenuItem saveinfo;
	public JMenuItem createblock;
	public JMenuItem editblock;

	public MapEditor() {
		super();

		Maps.setActualMap(1023);

		bar = new JMenuBar();
		JMenu file = new JMenu(LanguageManager.getText("menu.editor.file"));
		JMenu map = new JMenu(LanguageManager.getText("menu.editor.map"));
		JMenu blocks = new JMenu(LanguageManager.getText("menu.editor.blocks"));

		JMenuItem newm = new JMenuItem();
		newm.setText(LanguageManager.getText("menu.editor.new"));
		newm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Maps.createMaps();
				Maps.setActualMap(1023);
				Maps.resize();
			}
		});
		file.add(newm);

		JMenuItem save = new JMenuItem();
		save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		save.setText(LanguageManager.getText("menu.editor.save"));
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(RessourcesManager.getMapsDirectory());
				fc.setApproveButtonText(LanguageManager.getText("menu.editor.save"));
				fc.setDialogTitle(LanguageManager.getText("menu.editor.save"));
				fc.setSelectedFile(new File(RessourcesManager.getMapsDirectory(),Maps.getActualMap().getName()+".mtmap"));
				
				fc.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						return LanguageManager.getText("menu.editor.file.filter");
					}

					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}

						String extension = BlockCreator.getExtension(f);
						if (extension != null && extension.equals("mtmap")) {
							return true;
						} else {
							return false;
						}
					}
				});
				fc.setAcceptAllFileFilterUsed(false);

				int access = fc.showOpenDialog(TowerMiner.game);
				if(access == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					if(!f.getPath().endsWith(".mtmap")) {
						f = new File(f.getPath().substring(f.getPath().lastIndexOf("."))+".mtmap");
					}
					Maps.getActualMap().serialize(f);
				}
			}
		});
		file.add(save);

		JMenuItem load = new JMenuItem();
		load.setText(LanguageManager.getText("menu.editor.load"));
		load.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(RessourcesManager.getMapsDirectory());
				fc.setSelectedFile(new File(RessourcesManager.getMapsDirectory(),Maps.getActualMap().getName()+".mtmap"));
				fc.setFileFilter(new FileFilter() {

					@Override
					public String getDescription() {
						return LanguageManager.getText("menu.editor.file.filter");
					}

					@Override
					public boolean accept(File f) {
						if (f.isDirectory()) {
							return true;
						}

						String extension = BlockCreator.getExtension(f);
						if (extension != null && extension.equals("mtmap")) {
							return true;
						} else {
							return false;
						}
					}
				});
				fc.setAcceptAllFileFilterUsed(false);

				int access = fc.showOpenDialog(TowerMiner.game);
				if(access == JFileChooser.APPROVE_OPTION) {
					File f = fc.getSelectedFile();
					if(f.getPath().endsWith(".mtmap")) {
						Maps.maps[1023] = Maps.deserialize(f);
						Maps.setActualMap(1023);
						selected = 1;
						Maps.resize();
					}
				}
			}
		});
		file.add(load);

		JMenuItem retour = new JMenuItem();
		retour.setText(LanguageManager.getText("menu.main"));
		retour.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Maps.setActualMap(0);
				TowerMiner.setMenu(Menu.mainmenu);
			}
		});
		file.add(retour);

		name = new JTextField();
		name.setToolTipText(LanguageManager.getText("menu.editor.map.name"));
		name.setPreferredSize(new Dimension(90,20));
		name.setText(Maps.getActualMap().getName());
		map.add(name);

		width = new JNumberTextField();
		width.setToolTipText(LanguageManager.getText("menu.editor.map.width"));
		width.setPreferredSize(new Dimension(90,20));
		width.setText(Maps.getActualMap().getXMax()+"");
		map.add(width);

		height = new JNumberTextField();
		height.setToolTipText(LanguageManager.getText("menu.editor.map.height"));
		height.setPreferredSize(new Dimension(90,20));
		height.setText(Maps.getActualMap().getYMax()+"");
		map.add(height);

		color = new JMenuItem();
		color.setText(LanguageManager.getText("menu.editor.map.color"));
		color.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new ColorSelection();
			}
		});
		map.add(color);

		saveinfo = new JMenuItem();
		saveinfo.setText(LanguageManager.getText("menu.editor.save"));
		saveinfo.setPreferredSize(new Dimension(90,20));
		saveinfo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Maps.getActualMap().setName(name.getText());
				TowerMiner.game.setTitle(LanguageManager.getText("towerminer")+" | "+TowerMiner.game.version+" | "+LanguageManager.getText("menu.editor.map")+": "+Maps.getActualMap().getName());
				Maps.getActualMap().setXMax(Integer.parseInt(width.getText()));
				Maps.getActualMap().setYMax(Integer.parseInt(height.getText()));
				Maps.resize();
				TowerMiner.game.onInit();
			}
		});
		map.add(saveinfo);
		
		createblock = new JMenuItem();
		createblock.setText(LanguageManager.getText("menu.editor.create"));
		createblock.setPreferredSize(new Dimension(90,20));
		createblock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new BlockCreator(Maps.getActualMap(), new Blocks());
			}
		});
		blocks.add(createblock);
		
		editblock = new JMenuItem();
		editblock.setText(LanguageManager.getText("menu.editor.edit"));
		editblock.setPreferredSize(new Dimension(90,20));
		editblock.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new BlockSelectionList();
			}
		});
		blocks.add(editblock);

		bar.add(file);
		bar.add(map);
		bar.add(blocks);

		TowerMiner.game.add(bar);
		bar.setVisible(false);
	}

	public void drawMenu(Graphics2D g2d) {
		MapWritter.drawCanvas(g2d, TowerMiner.game.CanvasX, TowerMiner.game.CanvasY);

		g2d.setFont(new Font("TimesRoman", Font.CENTER_BASELINE, 18));
		g2d.setColor(Color.WHITE);

		if(cursorinwindow && (Ycursor+8)/48>0) {
			g2d.setColor(Color.BLACK);
			int xl = (Xcursor/48)*48;
			int yl = ((Ycursor+8)/48)*48-8;
			g2d.draw3DRect(xl, yl, 47, 47, false);
		}

		g2d.drawImage(Blocks.blocks[selected].getTexture(data),Xcursor-16,Ycursor-16,32,32,null);
		if(Blocks.blocks[selected].isMapAdapted(data)) {
			Color c = Maps.getActualMap().getColorModifier();
			g2d.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 100));
			g2d.fillRect(Xcursor-16,Ycursor-16,32,32);
		}

		int xt = Maps.getActualMap().getXDepart()*48;
		int yt = Maps.getActualMap().getYDepart()*48;

		g2d.rotate(Maps.getActualMap().getDepartRotation(), xt+48/2,yt+40+48/2);
		g2d.drawImage(RessourcesManager.getTexture("fleche"),xt,yt+40,48,48,null);
		g2d.rotate(-Maps.getActualMap().getDepartRotation(), xt+48/2,yt+40+48/2);

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
		
		if(TowerMiner.game.popup != null && TowerMiner.game.popup.time+TowerMiner.game.popup.displayed > new Date().getTime()) {
			TowerMiner.game.popup.draw(g2d, TowerMiner.game.CanvasY);
		}
		g2d.setColor(new Color(100,100,100));
		g2d.fillRect(0, 0, TowerMiner.game.getWidth()-TowerMiner.game.CanvasX, TowerMiner.game.CanvasY+2);
	}

	@Override
	public void onKeyPressed(int keyCode) {
		if(keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_Q || keyCode == KeyEvent.VK_D) {
			if(Blocks.blocks[selected].getTexture(data+1) == Blocks.blocks[selected].getTexture(0)) {
				data = 0;
			} else {
				data++;
			}
		} else if(keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_Z) {
			selected = Blocks.getNextVisibleBlock(selected);
			data = 0;
		} else if(keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) {
			selected = Blocks.getPreviousVisibleBlock(selected);
			data = 0;
		}
		super.onKeyPressed(keyCode);
	}

	@Override
	public void onMouseMoved(MouseEvent e) {
		Xcursor = e.getX()-2;
		Ycursor = e.getY()-30;
		super.onMouseMoved(e);
	}

	@Override
	public void onMouseClicked(MouseEvent e) {
		if(e.getModifiers() == 16 && cursorinwindow && (Ycursor+8)/48>0) {
			int xl = (Xcursor/48);
			int yl = ((Ycursor+8)/48)-1;
			if(selected == 1023) {
				Maps.getActualMap().setXDepart(xl);
				Maps.getActualMap().setYDepart(yl);
				Maps.getActualMap().setRawRotation(data);
				return;
			}
			if(selected == 1021) {
				if(Maps.getActualMap().deaths == null) {
					Maps.getActualMap().deaths = Maps.getActualMap().getDeathPoints();
				}
				Maps.getActualMap().deaths[data] = new Point(xl,yl);
				return;
			}
			if(Blocks.blocks[selected].isOverlay()) {
				Maps.getActualMap().setOverlayIdAndData(xl, yl, selected, data);
			} else {
				Maps.getActualMap().setBlockIdAndData(xl, yl, selected, data);
				Maps.getActualMap().setOverlayIdAndData(xl, yl, 1022, 0);
			}
		} else if(e.getModifiers() == 8) {
			int xl = (Xcursor/48);
			int yl = ((Ycursor+8)/48)-1;
			selected = Maps.getActualMap().getBlockId(xl, yl);
			data = Maps.getActualMap().getBlockData(xl, yl);
		} else if(e.getModifiers() == 4) {
			if(Blocks.blocks[selected].getTexture(data+1) == Blocks.blocks[selected].getTexture(0)) {
				data = 0;
			} else {
				data++;
			}
		}
	}

	@Override
	public void onMouseEntered(MouseEvent e) {cursorinwindow = true;}

	@Override
	public void onMouseExited(MouseEvent e) {cursorinwindow = false;}

	@Override
	public void onMouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() == -1) {
			selected = Blocks.getNextVisibleBlock(selected);
		} else {
			selected = Blocks.getPreviousVisibleBlock(selected);
		}
		data = 0;
	}

	@Override
	public void onUsed() {
		bar.setVisible(true);
		Maps.resize();
		TowerMiner.game.setTitle(LanguageManager.getText("towerminer")+" | "+TowerMiner.game.version+" | "+LanguageManager.getText("menu.editor")+" | "+LanguageManager.getText("menu.editor.map")+": "+Maps.getActualMap().getName());
	}

	@Override
	public void onUnused() {
		bar.setVisible(false);
	}
	
	@Override
	public void onMapChanged() {
		name.setText(Maps.getActualMap().getName());
		width.setText(Maps.getActualMap().getXMax()+"");
		height.setText(Maps.getActualMap().getYMax()+"");
		TowerMiner.game.setTitle(LanguageManager.getText("towerminer")+" | "+TowerMiner.game.version+" | "+LanguageManager.getText("menu.editor")+" | "+LanguageManager.getText("menu.editor.map")+": "+Maps.getActualMap().getName());
	}
}