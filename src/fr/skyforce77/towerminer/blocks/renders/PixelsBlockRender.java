package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Color;
import java.awt.Graphics2D;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.menus.MapEditor;

public class PixelsBlockRender extends BlockRender {

	private static final long serialVersionUID = -1542308932664622611L;

	public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
		String text = "";
		if (TowerMiner.menu instanceof MapEditor && pix != 48) {
			MapEditor me = (MapEditor) TowerMiner.menu;
			text = me.bdata.getText();
		} else {
			Object storage = Maps.getActualMap().getBlockStorage(x / 48, y / 48);
			if(storage != null) {
				text = (String)storage;
			}
		}

		if(text.equals("")) {
			g2d.setColor(Color.WHITE);
			g2d.fillRect(x, y, pix, pix);
			g2d.setColor(Color.GRAY);
			g2d.drawRect(x, y, pix, pix);
		} else {
			int size = pix/16;
			if(text.contains("(") && text.contains(")")) {
				try {
					size = pix/Integer.parseInt(text.substring(text.indexOf("(")+1, text.indexOf(")")));
				} catch(Exception e) {}
			}
			while(text.contains("[") && text.contains(",") && text.contains(":") && text.contains("]")) {
				try {
					text = text.substring(text.indexOf("[")+1);
					int comaIndex = text.indexOf(",");
					int pixx = Integer.parseInt(text.substring(0, comaIndex));
					int pointsIndex = text.indexOf(":");
					int pixy = Integer.parseInt(text.substring(comaIndex+1, pointsIndex));
					int lastIndex = text.indexOf("]");
					int color = Integer.parseInt(text.substring(pointsIndex+1, lastIndex));
					if(pixx >= 0 && pixy >= 0 && pixx < pix/size && pixy < pix/size) {
						g2d.setColor(new Color(color));
						g2d.fillRect(x+pixx*size, y+pixy*size, size, size);
					}
				} catch(Exception e) {}
				text = text.substring(text.indexOf("]")+1);
			}
		}
	}

	@Override
	public boolean overrideNormalRender() {
		return true;
	}

	@Override
	public void onPlaced(Blocks b, Maps m, int x, int y, int data, String st) {
		if(!st.equals("")) {
			m.setBlockStorage(x, y, st);
		} else {
			m.setBlockStorage(x, y, "");
		}
	}

	@Override
	public boolean needNBT(int id) {
		return true;
	}

	@Override
	public String formatNBT(int id) {
		return "(size) [x,y:color] ...";
	}
}
