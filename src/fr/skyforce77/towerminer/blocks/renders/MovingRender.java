package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import javax.swing.ImageIcon;

import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.render.RenderHelper;

public class MovingRender extends BlockRender {

    private static final long serialVersionUID = 6005625431020091158L;
    private static HashMap<Blocks, Integer> states = new HashMap<Blocks, Integer>();
    private static HashMap<Blocks, Integer> count = new HashMap<Blocks, Integer>();
    private static HashMap<Blocks, Boolean> reverse = new HashMap<Blocks, Boolean>();

    @Override
    public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
        int images = b.getTexture(data).getHeight(null) / b.getTexture(data).getWidth(null);
        Image image = null;
        if (b.isMapAdapted(data)) {
            Color c = Maps.getActualMap().getColorModifier();
            image = RenderHelper.getColoredImage(b.getTexture(data), c, 0.5F);
        } else {
            image = b.getTexture(data);
        }
        g2d.setClip(x, y, pix, pix);
        if (states.containsKey(b))
            g2d.drawImage(image, x, y - (pix * states.get(b)), pix, pix * images, null);
    }

    @Override
    public void onGameTick() {
        for (Blocks b : Blocks.getList()) {
            if (b != null) {
                int images = b.getTexture(0).getHeight(null) / b.getTexture(0).getWidth(null);
                if (states.containsKey(b)) {
                    if (count.get(b) <= 6) {
                        count.put(b, count.get(b) + 1);
                    } else {
                        int i = states.get(b);
                        if ((!reverse.get(b) && i == images - 1) || (reverse.get(b) && i == 0)) {
                            reverse.put(b, !reverse.get(b));
                        } else {
                            states.put(b, reverse.get(b) ? i - 1 : i + 1);
                        }
                        count.put(b, 0);
                    }
                } else {
                    states.put(b, 0);
                    count.put(b, 0);
                    reverse.put(b, false);
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
        return 0;
    }

    @Override
    public ImageIcon getListIcon(Blocks b) {
        return b.getIcon(1);
    }
}
