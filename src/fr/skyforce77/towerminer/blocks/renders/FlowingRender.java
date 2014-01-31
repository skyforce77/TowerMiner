package fr.skyforce77.towerminer.blocks.renders;

import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.render.RenderHelper;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class FlowingRender extends BlockRender {

    private static final long serialVersionUID = 6005625431020091158L;
    private static HashMap<Blocks, Integer> states = new HashMap<Blocks, Integer>();
    private static HashMap<Blocks, Integer> count = new HashMap<Blocks, Integer>();

    @Override
    public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
        if (!states.containsKey(b)) {
            return;
        }

        int images = b.getTexture(0).getHeight(null) / (b.getTexture(0).getWidth(null) / 2);
        Image image = null;
        if (b.isMapAdapted(0)) {
            Color c = Maps.getActualMap().getColorModifier();
            image = RenderHelper.getColoredImage(b.getTexture(0), c, 0.5F);
        } else {
            image = b.getTexture(0);
        }
        g2d = (Graphics2D) g2d.create(x, y, pix, pix);

        if (data <= 3) {
            g2d.rotate(Math.toRadians(90 * data), pix / 2, pix / 2);
            g2d.drawImage(image, 0, -(pix * states.get(b)), pix * 2, pix * images, null);
        } else {
            g2d.rotate(Math.toRadians(90 * data + 45), pix / 2, pix / 2);
            g2d.drawImage(image, -pix / 2, -(pix * states.get(b)), pix * 2, pix * images, null);
            g2d.drawImage(image, -pix / 2, -(pix * states.get(b)) - (images * pix), pix * 2, pix * images, null);
            g2d.drawImage(image, -pix / 2, -(pix * states.get(b)) + (images * pix), pix * 2, pix * images, null);
        }
    }

    @Override
    public void onGameTick() {
        for (Blocks b : Blocks.getList()) {
            if (b != null) {
                int images = b.getTexture(0).getHeight(null) / (b.getTexture(0).getWidth(null) / 2);
                if (states.containsKey(b)) {
                    if (count.get(b) <= 2) {
                        count.put(b, count.get(b) + 1);
                    } else {
                        int i = states.get(b);
                        if (i == images - 1) {
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
        return 8;
    }

    @Override
    public ImageIcon getListIcon(Blocks b) {
        return b.getIcon(1);
    }

}
