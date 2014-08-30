package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MapEditor;

public class NumberBlockRender extends BlockRender {

	private static final long serialVersionUID = -9213478529378034829L;

	public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
		
		int i = 0;
		if (TowerMiner.menu instanceof MapEditor && pix != 48) {
            MapEditor me = (MapEditor) TowerMiner.menu;
            try {
    			i = Integer.parseInt(me.bdata.getText());
    		} catch(Exception e) {}
        } else {
            Object storage = Maps.getActualMap().getBlockStorage(x / 48, y / 48);
            try {
    			i = Integer.parseInt((String)storage);
    		} catch(Exception e) {}
        }

        g2d.setClip(new Rectangle2D.Float(x, y, pix, pix));
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x, y, pix, pix);
        g2d.setColor(Color.WHITE);
        g2d.drawRect(x, y, pix-1, pix-1);
        g2d.setFont(TowerMiner.getFont(15));

        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        int height = metrics.getHeight();
        int width = metrics.stringWidth(i+"");
        g2d.drawString(i+"", x+((pix-width)/2), y+pix-((pix-height)/2));
    }
	
	@Override
    public void onPlaced(Blocks b, Maps m, int x, int y, int data, String st) {
        if(!st.equals("")) {
            m.setBlockStorage(x, y, st);
        } else if(m.getBlockStorage(x, y) != null) {
        	m.setBlockStorage(x, y, 0+"");
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
    public boolean needNBT(int id) {
    	return true;
    }
    
    @Override
    public String formatNBT(int id) {
    	return "number";
    }
}
