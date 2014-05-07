package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.MapWritter;
import fr.skyforce77.towerminer.maps.Maps;

public class WallRender extends AroundBlockBasedRender {

    private static final long serialVersionUID = 600562543102009115L;

    @Override
    public void onBlockRenderInHand(Blocks b, Graphics2D g2d, int data, int x,
                                    int y, int pix) {
        int size = 8;
        g2d.setClip(new Rectangle2D.Float(x + size, y + size, pix - size * 2, pix - size * 2));
        g2d.drawImage(b.getTexture(data), x, y, pix, pix, null);
    }

    @Override
    public void onBlockRenderOnGround(Blocks b, Graphics2D g2d, int data,
                                      int x, int y) {
        int blocks = 0;
        Maps map = Maps.getActualMap();
        for (Point p : MapWritter.getAroundPoints(x, y)) {
            if (map.hasPoint(p) && canConnect(x, y, map.getOverlayId(p.x, p.y))) {
                blocks++;
            }
        }

        if (blocks == 2) {
            if (map.hasPoint(new Point(x, y + 1)) && canConnect(x, y, map.getOverlayId(x, y + 1)) && map.hasPoint(new Point(x, y - 1)) && canConnect(x, y, map.getOverlayId(x, y - 1))) {
                drawCenter(g2d, b, data, x * 48, y * 48 + TowerMiner.game.CanvasY);
            } else if (map.hasPoint(new Point(x + 1, y)) && canConnect(x, y, map.getOverlayId(x + 1, y)) && map.hasPoint(new Point(x - 1, y)) && canConnect(x, y, map.getOverlayId(x - 1, y))) {
                drawCenter(g2d, b, data, x * 48, y * 48 + TowerMiner.game.CanvasY);
            } else {
                drawAttach(g2d, b, data, x * 48, y * 48 + TowerMiner.game.CanvasY);
            }
        } else {
            drawAttach(g2d, b, data, x * 48, y * 48 + TowerMiner.game.CanvasY);
        }
    }

    public void drawAttach(Graphics2D g2d, Blocks b, int data, int x, int y) {
        Area area = new Area();
        int size = 13;
        area.add(new Area(new Rectangle2D.Float(x + size, y + size, 48 - size * 2, 48 - size * 2)));
        Maps map = Maps.getActualMap();
        int x1 = x / 48;
        int y1 = (y - TowerMiner.game.CanvasY) / 48;

        if (map.hasPoint(new Point(x1, y1 + 1)) && (canConnect(x1, y1, map.getBlockId(x1, y1 + 1)) || canConnect(x1, y1, map.getOverlayId(x1, y1 + 1)))) {
            area.add(new Area(new Rectangle2D.Float(x + size * 1.3f, y + 48 - size, 48 - size * 2 * 1.3f, size)));
        }
        if (map.hasPoint(new Point(x1, y1 - 1)) && (canConnect(x1, y1, map.getBlockId(x1, y1 - 1)) || canConnect(x1, y1, map.getOverlayId(x1, y1 - 1)))) {
            area.add(new Area(new Rectangle2D.Float(x + size * 1.3f, y, 48 - size * 2 * 1.3f, 48 - size)));
        }
        if (map.hasPoint(new Point(x1 + 1, y1)) && (canConnect(x1, y1, map.getBlockId(x1 + 1, y1)) || canConnect(x1, y1, map.getOverlayId(x1 + 1, y1)))) {
            area.add(new Area(new Rectangle2D.Float(x + 48 - size, y + size * 1.3f, size, 48 - size * 2 * 1.3f)));
        }
        if (map.hasPoint(new Point(x1 - 1, y1)) && (canConnect(x1, y1, map.getBlockId(x1 - 1, y1)) || canConnect(x1, y1, map.getOverlayId(x1 - 1, y1)))) {
            area.add(new Area(new Rectangle2D.Float(x, y + size * 1.3f, size, 48 - size * 2 * 1.3f)));
        }

        g2d.setClip(area);
        g2d.drawImage(b.getTexture(data), x, y, 48, 48, null);
    }

    public void drawCenter(Graphics2D g2d, Blocks b, int data, int x, int y) {
        Area area = new Area();
        float size = 13 * 1.3f;
        area.add(new Area(new Rectangle2D.Float(x + size, y + size, 48 - size * 2, 48 - size * 2)));
        Maps map = Maps.getActualMap();
        int x1 = x / 48;
        int y1 = (y - TowerMiner.game.CanvasY) / 48;

        if (map.hasPoint(new Point(x1, y1 + 1)) && (canConnect(x1, y1, map.getBlockId(x1, y1 + 1)) || canConnect(x1, y1, map.getOverlayId(x1, y1 + 1)))) {
            area.add(new Area(new Rectangle2D.Float(x + size, y + 48 - size, 48 - size * 2, size)));
        }
        if (map.hasPoint(new Point(x1, y1 - 1)) && (canConnect(x1, y1, map.getBlockId(x1, y1 - 1)) || canConnect(x1, y1, map.getOverlayId(x1, y1 - 1)))) {
            area.add(new Area(new Rectangle2D.Float(x + size, y, 48 - size * 2, 48 - size)));
        }
        if (map.hasPoint(new Point(x1 + 1, y1)) && (canConnect(x1, y1, map.getBlockId(x1 + 1, y1)) || canConnect(x1, y1, map.getOverlayId(x1 + 1, y1)))) {
            area.add(new Area(new Rectangle2D.Float(x + 48 - size, y + size, size, 48 - size * 2)));
        }
        if (map.hasPoint(new Point(x1 - 1, y1)) && (canConnect(x1, y1, map.getBlockId(x1 - 1, y1)) || canConnect(x1, y1, map.getOverlayId(x1 - 1, y1)))) {
            area.add(new Area(new Rectangle2D.Float(x, y + size, size, 48 - size * 2)));
        }

        g2d.setClip(area);
        g2d.drawImage(b.getTexture(data), x, y, 48, 48, null);
    }

    public boolean canConnect(int x, int y, int id) {
        if (Maps.getActualMap().hasPoint(new Point(x, y)) && Maps.getActualMap().getBlockId(x, y) == id) {
            return false;
        }
        if (Blocks.byId(id).getRender() instanceof WallRender) {
            return true;
        }
        return id != 0 && !Blocks.byId(id).isLiquid() && !Blocks.byId(id).isOverlay();
    }

    @Override
    public boolean overrideNormalRender() {
        return true;
    }

}
