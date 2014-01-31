package fr.skyforce77.towerminer.blocks.renders;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MapEditor;
import fr.skyforce77.towerminer.ressources.RessourcesManager;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class UnknownBlockRender extends BlockRender {

    public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
        int bid = Maps.getActualMap().getBlockId(x / 48, (y - TowerMiner.game.CanvasY) / 48);
        int oid = Maps.getActualMap().getOverlayId(x / 48, (y - TowerMiner.game.CanvasY) / 48);

        if(Blocks.byId(oid).equals(Blocks.byId(1024))) {
            bid = oid;
        }

        g2d.setClip(new Rectangle2D.Float(x, y, pix, pix));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x, y, pix, pix);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, pix-1, pix-1);
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));

        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int height = metrics.getHeight();
        int width = metrics.stringWidth(bid+"");
        g2d.drawString(bid+"", x+((pix-width)/2), y+pix-((pix-height)/2));
    }

    @Override
    public boolean overrideNormalRender() {
        return true;
    }

    @Override
    public int dataNumber(Blocks b) {
        return 0;
    }
}
