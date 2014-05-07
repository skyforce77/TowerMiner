package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.render.RenderHelper;

public class FireRender extends BlockRender {

    private static final long serialVersionUID = 6005625431020091158L;
    private static HashMap<Blocks, Integer> states = new HashMap<Blocks, Integer>();
    private static HashMap<Blocks, Integer> count = new HashMap<Blocks, Integer>();

    @Override
    public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
        int images = b.getTexture(data > 3 ? 1 : 0).getHeight(null) / b.getTexture(data > 3 ? 1 : 0).getWidth(null);
        Image image = null;
        if (b.isMapAdapted(data)) {
            Color c = Maps.getActualMap().getColorModifier();
            image = RenderHelper.getColoredImage(b.getTexture(data > 3 ? 1 : 0), c, 0.5F);
        } else {
            image = b.getTexture(data > 3 ? 1 : 0);
        }
        g2d.setClip(x, y, pix, pix);
        data = data > 3 ? data - 4 : data;
        g2d.rotate(Math.toRadians(90 * data), x + pix / 2, y + pix / 2);
        g2d.drawImage(image, x, y - (pix * states.get(b)), pix, pix * images, null);
    }

    @Override
    public void onGameTick() {
        for (Blocks b : Blocks.getList()) {
            if (b != null) {
                int images = b.getTexture(0).getHeight(null) / b.getTexture(0).getWidth(null);
                if (states.containsKey(b)) {
                    if (count.get(b) <= 1) {
                        count.put(b, count.get(b) + 1);
                    } else {
                        int i = states.get(b);
                        if (states.get(b) >= images - 1) {
                            states.put(b, 0);
                        } else {
                            states.put(b, i + 1);
                        }
                        count.put(b, 0);
                    }
                } else {
                    states.put(b, 0);
                    count.put(b, 0);
                }
            }
        }
    }

    @Override
    public boolean overrideNormalRender() {
        return true;
    }

    @Override
    public int dataNumber(Blocks b) {
        return 7;
    }

    @Override
    public ImageIcon getListIcon(Blocks b) {
        return b.getIcon(2);
    }
}
