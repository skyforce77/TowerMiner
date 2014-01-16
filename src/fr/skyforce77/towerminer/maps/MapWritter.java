package fr.skyforce77.towerminer.maps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;

import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.render.RenderHelper;

public class MapWritter {

	private static void drawLine(Graphics2D g2d, int y, int m, int n) {
		int x = 0;
		while(x < Maps.getActualMap().getXMax()) {
			drawBlock(g2d, Maps.getActualMap().getBlockId(x,y), Maps.getActualMap().getBlockData(x,y), x*getBlockWidth(), y*getBlockHeight(), m, n, 48);
			drawBlock(g2d, Maps.getActualMap().getOverlayId(x,y), Maps.getActualMap().getOverlayData(x,y), x*getBlockWidth(), y*getBlockHeight(), m, n, 48);
			x++;
		}
	}

	public static void drawBlock(Graphics2D g2d, int id, int data, int x, int y, int m, int n, int size) {
		g2d = (Graphics2D)g2d.create();
		if(id < Blocks.blocks.length && Blocks.blocks[id] != null) {
			Blocks.blocks[id].onDraw(g2d, data, x, y, m, n);
			if(Blocks.blocks[id].getRender() == null || (Blocks.blocks[id].getRender() != null && !Blocks.blocks[id].getRender().overrideNormalRender())) {
				Image image = null;
				if(Blocks.blocks[id].isMapAdapted(data)) {
					Color c = Maps.getActualMap().getColorModifier();
					image = RenderHelper.getColoredImage(Blocks.blocks[id].getTexture(data), c, 0.5F);
				} else {
					image = Blocks.blocks[id].getTexture(data);
				}
				g2d.drawImage(image,x+m,y+n,size,size,null);
			}
			if(Blocks.blocks[id].getRender() != null) {
				Blocks.blocks[id].getRender().onBlockRender(Blocks.blocks[id], g2d, data, x+m, y+n, size);
			}
		} else {
			drawBlock(g2d, 1, data, x, y, m, n, size);
		}
	}

	public static ArrayList<Point> getAroundPoints(int x, int y) {
		ArrayList<Point> points = new ArrayList<Point>();
		points.add(new Point(x, y+1));
		points.add(new Point(x, y-1));
		points.add(new Point(x+1, y));
		points.add(new Point(x-1, y));
		return points;
	}

	public static void drawCanvas(Graphics2D g2d, int m, int n) {
		int y = 0;
		while(y < Maps.getActualMap().getYMax()) {
			drawLine(g2d, y, m, n);
			y++;
		}
		g2d.setColor(Color.GRAY);
	}

	public static int getBlockHeight() {
		return 48;
		//return (TowerMiner.game.getHeight()-TowerMiner.game.CanvasY)/Maps.getActualMap().getYMax();
	}

	public static int getBlockWidth() {
		return 48;
		//return (TowerMiner.game.getWidth()-TowerMiner.game.CanvasX)/Maps.getActualMap().getXMax();
	}

}
