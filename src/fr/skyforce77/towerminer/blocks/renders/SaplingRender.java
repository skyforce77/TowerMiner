package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.render.RenderHelper;

public class SaplingRender extends BlockRender {

	private static final long serialVersionUID = 5063981553075420759L;

	public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {
		Image image = null;
        if (b.isMapAdapted(data)) {
            Color c = Maps.getActualMap().getColorModifier();
            image = RenderHelper.getColoredImage(b.getTexture(data), c, 0.5F);
        } else {
            image = b.getTexture(data);
        }
		if(pix != 48) {
			g2d.drawImage(image, x, y, pix, pix, null);
		} else {
			g2d.rotate(Math.toRadians(-45), x+pix/2, y+pix/2);
			int movex = (TowerMiner.game.canvas.getWidth()/2-x)/(TowerMiner.game.canvas.getWidth()/30);
			int movey = -1*(TowerMiner.game.canvas.getHeight()/2-y)/(TowerMiner.game.canvas.getHeight()/30);
			g2d.drawImage(image, x, y+pix/2-movex, pix, movex, null);
			g2d.rotate(Math.toRadians(-90), x+pix/2, y+pix/2);
			g2d.drawImage(image, x, y+pix/2-movey, pix, movey, null);
		}
	}

	@Override
	public boolean overrideNormalRender() {
		return true;
	}
}
