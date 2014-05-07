package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Graphics2D;

import fr.skyforce77.towerminer.blocks.Blocks;

public class AroundBlockBasedRender extends BlockRender {

    private static final long serialVersionUID = 60056254310200115L;

    @Override
    public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
        if (pix == 48) {
            onBlockRenderOnGround(b, g2d, data, x / 48, y / 48);
        } else {
            onBlockRenderInHand(b, g2d, data, x, y, pix);
        }
    }

    public void onBlockRenderInHand(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
    }

    public void onBlockRenderOnGround(Blocks b, Graphics2D g2d, int data, int x, int y) {
    }

}
