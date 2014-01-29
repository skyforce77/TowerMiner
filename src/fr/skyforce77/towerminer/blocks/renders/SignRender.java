package fr.skyforce77.towerminer.blocks.renders;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MapEditor;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class SignRender extends BlockRender {

    public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
        Image image = b.getTexture(data);
        int size = 3;
        g2d.setClip(new Rectangle2D.Float(x, y + pix / (2 * size), pix, pix - pix / size));
        g2d.drawImage(image, x, y + pix / (2 * size), pix, pix, null);
        String s = "";
        if (TowerMiner.menu instanceof MapEditor && pix != 48) {
            MapEditor me = (MapEditor) TowerMiner.menu;
            s = me.bdata.getText();
        } else if (getOverlayStorage(x, y) != null && getOverlayStorage(x, y) instanceof String) {
            s = (String) getOverlayStorage(x, y);
        }
        writeText(g2d, x, y + pix / (2 * size), pix, pix - pix / size, s);
    }

    public void writeText(Graphics2D g2d, int x, int y, int width, int height, String text) {
        g2d.setFont(new Font("TimesRoman", Font.BOLD, height / 2));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        String sec = "";
        while (metrics.stringWidth(text) >= width) {
            if (metrics.getHeight() < height * 2) {
                sec = text.substring(text.length() - 1) + sec;
                text = text.substring(0, text.length() - 1);
            } else {
                g2d.setFont(new Font("TimesRoman", Font.BOLD, g2d.getFont().getSize() - 1));
                metrics = g2d.getFontMetrics(g2d.getFont());
            }
        }
        if (sec.equals("")) {
            g2d.drawString(text, x, y + height / 2 + metrics.getHeight() / 2);
        } else {
            g2d.drawString(text, x, y + (height / 2 + metrics.getHeight() / 2) / 2);
            g2d.drawString(sec, x, y + (height - metrics.getHeight() / 2 * 3));
        }
    }

    @Override
    public boolean overrideNormalRender() {
        return true;
    }

    @Override
    public void onPlaced(Blocks b, Maps m, int x, int y, int data, String st) {
        if (!st.equals(""))
            m.setOverlayStorage(x, y, st);
    }
}
