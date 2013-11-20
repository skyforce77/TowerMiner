package fr.skyforce77.towerminer.maps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.render.RenderHelper;

public class MapWritter {

	private static void drawLine(Graphics2D g2d, int y, int m, int n) {
		int x = 0;
		while(x < Maps.getActualMap().getXMax()) {
			drawBlock(g2d, Maps.getActualMap().getBlockId(x,y), Maps.getActualMap().getBlockData(x,y), x, y, m, n);
			drawBlock(g2d, Maps.getActualMap().getOverlayId(x,y), Maps.getActualMap().getOverlayData(x,y), x, y, m, n);
			x++;
		}
	}

	public static void drawBlock(Graphics2D g2d, int id, int data, int x, int y, int m, int n) {
		if(Blocks.blocks[id].isMapAdapted(data)) {
			Color c = Maps.getActualMap().getColorModifier();
			g2d.drawImage(RenderHelper.getColoredImage(Blocks.blocks[id].getTexture(data), c, 0.5F),x*getBlockWidth()+m,y*getBlockHeight()+n,getBlockWidth(),getBlockHeight(),null);
		} else {
			g2d.drawImage(Blocks.blocks[id].getTexture(data),x*getBlockWidth()+m,y*getBlockHeight()+n,getBlockWidth(),getBlockHeight(),null);
		}
	}

	/*private static Point getBlockToCopy(int x, int y) {
		Blocks[] b = new Blocks[4];
		Point[] points = new Point[4];
		int[] count = new int[4];
		int i = 0;
		for(Point p : getAroundPoints(x, y)) {
			if(Maps.getActualMap().hasPoint(p) && !Maps.getActualMap().getBlock((int)p.getX(), (int)p.getY()).isTranslucent()) {
				b[i] = Maps.getActualMap().getBlock((int)p.getX(), (int)p.getY());
				points[i] = p;
			}
			i++;
		}
		i = 0;
		for(Blocks bl : b) {
			int it = 0;
			if(bl != null) {
				for(Blocks bl2 : b) {
					if(bl2 != null && bl.equals(bl2) && !bl.isTranslucent()) {
						it++;
					}
				}
			}
			count[i] = it;
			i++;
		}
		i = 0;
		int counts = 0;
		Point point = new Point(-1,-1);
		for(int c : count) {
			if(c >= counts) {
				counts = c;
				point = points[i];
			}
			i++;
		}
		return point;
	}*/

	public static ArrayList<Point> getAroundPoints(int x, int y) {
		ArrayList<Point> points = new ArrayList<>();
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
