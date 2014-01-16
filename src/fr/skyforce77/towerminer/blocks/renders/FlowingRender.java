package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.HashMap;

import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.render.RenderHelper;

public class FlowingRender extends BlockRender{

	private static final long serialVersionUID = 6005625431020091158L;
	private static HashMap<Blocks, Integer> states = new HashMap<>();
	private static HashMap<Blocks, Integer> count = new HashMap<>();
	
	@Override
	public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
		int images = b.getTexture(0).getHeight(null)/(b.getTexture(0).getWidth(null)/2);
		Image image = null;
		if(b.isMapAdapted(0)) {
			Color c = Maps.getActualMap().getColorModifier();
			image = RenderHelper.getColoredImage(b.getTexture(0), c, 0.5F);
		} else {
			image = b.getTexture(0);
		}
		g2d.setClip(x, y, pix, pix);
		g2d.drawImage(image, x, y-(pix*states.get(b)), pix*2, pix*images, null);
	}
	
	@Override
	public void onGameTick() {
		for(Blocks b : Blocks.blocks) {
			if(b != null) {
				int images = b.getTexture(0).getHeight(null)/(b.getTexture(0).getWidth(null)/2);
				if(states.containsKey(b)) {
					if(count.get(b) <= 5) {
						count.put(b, count.get(b)+1);
					} else {
						int i = states.get(b);
						if(i == images-1) {
							states.put(b, 0);
						} else {
							states.put(b, i+1);
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

}
