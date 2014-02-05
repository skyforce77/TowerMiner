package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MapEditor;

public class SignRender extends BlockRender {

	private static final long serialVersionUID = -1542308932664622611L;

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
        
        if(data == 1) {
        	g2d.setColor(Color.LIGHT_GRAY);
        } else if(data == 5) {
        	g2d.setColor(Color.WHITE);
        } else {
        	g2d.setColor(Color.BLACK);
        }
        
        writeText(g2d, x, y + pix / (2 * size), pix, pix - pix / size, s);
    }

    public void writeText(Graphics2D g2d, int x, int y, int width, int height, String text) {
    	String ltext = text;
        g2d.setFont(new Font("TimesRoman", Font.BOLD, height / 2));
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        String sec = "";
        while (metrics.stringWidth(text) >= width || metrics.stringWidth(sec) >= width) {
            if (metrics.stringWidth(sec) >= width) {
            	g2d.setFont(new Font("TimesRoman", Font.BOLD, g2d.getFont().getSize() - 1));
                metrics = g2d.getFontMetrics(g2d.getFont());
                text = ltext;
                sec = "";
            } else if(metrics.getHeight() < height * 2){
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
            g2d.drawString(sec, x, y + height);
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
    
    @Override
    public boolean needNBT(int id) {
    	return true;
    }
    
    @Override
    public String formatNBT(int id) {
    	return "text";
    }
}
