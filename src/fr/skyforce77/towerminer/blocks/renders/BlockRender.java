package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Graphics2D;
import java.io.Serializable;

import javax.swing.ImageIcon;

import fr.skyforce77.towerminer.blocks.Blocks;

public class BlockRender implements Serializable{

	private static final long serialVersionUID = -5745290778062774752L;
	public static BlockRender[] renders = new BlockRender[50];
	
	public static void createRenders() {
		renders[0] = new BlockRender();
		renders[1] = new MovingRender();
		renders[2] = new FlowingRender();
		renders[3] = new RedstoneRender();
		renders[4] = new FireRender();
		renders[5] = new FenceRender();
		renders[6] = new WallRender();
	}

	public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {}
	
	public void onGameTick() {}
	
	public boolean overrideNormalRender() {
		return false;
	}
	
	public int dataNumber(Blocks b) {
		int i = 1;
		while(!b.getTexture(i).equals(b.getTexture(0))) {
			i++;
		}
		return i;
	}
	
	public ImageIcon getListIcon(Blocks b) {
		return b.getIcon(0);
	}

}
