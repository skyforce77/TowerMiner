package fr.skyforce77.towerminer.blocks.renders;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SaplingRender extends BlockRender {

    public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
        g2d.rotate(Math.toRadians(-45), x, y+pix);
        g2d.drawImage(b.getTexture(data), x, y, pix+pix/4, pix/2+pix/3, null);
    }

    @Override
    public boolean overrideNormalRender() {
        return true;
    }
}
