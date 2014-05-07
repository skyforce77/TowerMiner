package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MapEditor;

public class ColorBlockRender extends BlockRender {

	private static final long serialVersionUID = -1542308932664622611L;

	public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
		Color c = Color.WHITE;
		if (TowerMiner.menu instanceof MapEditor && pix != 48) {
            MapEditor me = (MapEditor) TowerMiner.menu;
            try {
    			c = new Color(Integer.parseInt(me.bdata.getText()));
    		} catch(Exception e) {}
        } else {
            Object storage = Maps.getActualMap().getBlockStorage(x / 48, y / 48);
            try {
    			c = new Color(Integer.parseInt((String)storage));
    		} catch(Exception e) {}
        }
        g2d.setColor(c);
        g2d.fillRect(x, y, pix, pix);
    }

    @Override
    public boolean overrideNormalRender() {
        return true;
    }

    @Override
    public void onPlaced(Blocks b, Maps m, int x, int y, int data, String st) {
        if(!st.equals("")) {
            m.setBlockStorage(x, y, st);
        } else if(m.getBlockStorage(x, y) != null) {
        	m.setBlockStorage(x, y, Color.white.getRGB()+"");
        }
    }
    
    @Override
    public boolean needNBT(int id) {
    	return true;
    }
    
    @Override
    public String formatNBT(int id) {
    	return "color";
    }
}
