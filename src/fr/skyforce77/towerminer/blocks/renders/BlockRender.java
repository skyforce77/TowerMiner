package fr.skyforce77.towerminer.blocks.renders;

import java.awt.Graphics2D;
import java.io.Serializable;

import fr.skyforce77.towerminer.blocks.Blocks;

public class BlockRender implements Serializable{

	private static final long serialVersionUID = -5745290778062774752L;
	public static BlockRender[] renders = new BlockRender[50];
	
	public static void createRenders() {
		renders[0] = new MovingRender();
		renders[1] = new FlowingRender();
	}

	public void onBlockRender(Blocks b, Graphics2D g2d, int data, int x, int y, int pix) {}
	
	public void onGameTick() {}
	
	public boolean overrideNormalRender() {
		return false;
	}

}
