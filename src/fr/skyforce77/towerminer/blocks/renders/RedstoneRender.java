package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.render.RenderHelper;

public class RedstoneRender extends AroundBlockBasedRender{

	private static final long serialVersionUID = 600562543102009115L;

	@Override
	public void onBlockRenderInHand(Blocks b, Graphics2D g2d, int data, int x,
			int y, int pix) {
		if(data == 1)
			g2d.drawImage(b.getTexture(0), x, y, pix, pix, null);
		if(data == 0)
			g2d.drawImage(RenderHelper.getColoredImage(b.getTexture(0), Color.RED, 0.3f), x, y, pix, pix, null);
	}
	
	@Override
	public void onBlockRenderOnGround(Blocks b, Graphics2D g2d, int data,
			int x, int y) {
		Color c = data == 1 ? new Color(80,0,0) : new Color(200,0,0);
		int blocks = 0;
		Maps map = Maps.getActualMap();
		for(Point p : MapWritter.getAroundPoints(x, y)) {
			if(map.hasPoint(p) && canConnect(map.getOverlayId(p.x, p.y))) {
				blocks++;
			}
		}
		
		if(blocks == 0 || blocks == 4) {
			g2d.drawImage(RenderHelper.getColoredImage(b.getTexture(1), c, 0.8f), x*48, y*48+TowerMiner.game.CanvasY, 48, 48, null);
		} else if(blocks == 2) {
			if(map.hasPoint(new Point(x,y+1)) && canConnect(map.getOverlayId(x, y+1)) && map.hasPoint(new Point(x,y-1)) && canConnect(map.getOverlayId(x, y-1))) {
				g2d = (Graphics2D)g2d.create(x*48, y*48+TowerMiner.game.CanvasY, 48, 48);
				g2d.rotate(Math.toRadians(90), 24, 24);
				g2d.drawImage(RenderHelper.getColoredImage(b.getTexture(2), c, 0.8f), 0, 0, 48, 48, null);
			} else if(map.hasPoint(new Point(x+1,y)) && canConnect(map.getOverlayId(x+1, y)) && map.hasPoint(new Point(x-1,y)) && canConnect(map.getOverlayId(x-1, y))) {
				g2d.drawImage(RenderHelper.getColoredImage(b.getTexture(2), c, 0.8f), x*48, y*48+TowerMiner.game.CanvasY, 48, 48, null);
			} else {
				drawAttach(g2d, b, c, x*48, y*48+TowerMiner.game.CanvasY);
			}
		} else if(blocks == 1) {
			if((map.hasPoint(new Point(x,y+1)) && canConnect(map.getOverlayId(x, y+1))) || (map.hasPoint(new Point(x,y-1)) && canConnect(map.getOverlayId(x, y-1)))) {
				g2d = (Graphics2D)g2d.create(x*48, y*48+TowerMiner.game.CanvasY, 48, 48);
				g2d.rotate(Math.toRadians(90), 24, 24);
				g2d.drawImage(RenderHelper.getColoredImage(b.getTexture(2), c, 0.8f), 0, 0, 48, 48, null);
			} else if((map.hasPoint(new Point(x+1,y)) && canConnect(map.getOverlayId(x+1, y))) || (map.hasPoint(new Point(x-1,y)) && canConnect(map.getOverlayId(x-1, y)))) {
				g2d.drawImage(RenderHelper.getColoredImage(b.getTexture(2), c, 0.8f), x*48, y*48+TowerMiner.game.CanvasY, 48, 48, null);
			}
		} else {
			drawAttach(g2d, b, c, x*48, y*48+TowerMiner.game.CanvasY);
		}
	}
	
	public void drawAttach(Graphics2D g2d, Blocks b, Color c, int x, int y) {
		Rectangle2D rect = new Rectangle2D.Float();
		int size = 15;
		rect.setFrame(x+size, y+size, 48-size*2, 48-size*2);
		Maps map = Maps.getActualMap();
		int x1 = x/48;
		int y1 = (y-TowerMiner.game.CanvasY)/48;
		
		if(map.hasPoint(new Point(x1, y1+1)) && canConnect(map.getOverlayId(x1, y1+1))) {
			Rectangle2D r = new Rectangle2D.Float();
			r.setFrame(x+size, y+48-size, 48-size*2, size);
			rect.add(r);
		}
		if(map.hasPoint(new Point(x1, y1-1)) && canConnect(map.getOverlayId(x1, y1-1))) {
			Rectangle2D r = new Rectangle2D.Float();
			r.setFrame(x+size, y, 48-size*2, 48-size);
			rect.add(r);
		}
		if(map.hasPoint(new Point(x1+1, y1)) && canConnect(map.getOverlayId(x1+1, y1))) {
			Rectangle2D r = new Rectangle2D.Float();
			r.setFrame(x+48-size, y+size, size, 48-size*2);
			rect.add(r);
		}
		if(map.hasPoint(new Point(x1-1, y1)) && canConnect(map.getOverlayId(x1-1, y1))) {
			Rectangle2D r = new Rectangle2D.Float();
			r.setFrame(x, y+size, size, 48-size*2);
			rect.add(r);
		}
		
		g2d.setClip(rect);
		g2d.drawImage(RenderHelper.getColoredImage(b.getTexture(1), c, 0.8f), x, y, 48, 48, null);
	}
	
	public boolean canConnect(int id) {
		return id == 55 || id == 76;
	}

	@Override
	public boolean overrideNormalRender() {
		return true;
	}
	
	@Override
	public int dataNumber(Blocks b) {
		return 2;
	}

}
