package fr.skyforce77.towerminer.render;

import java.awt.Graphics2D;

public class RenderRunnable {
	
	private boolean replace = false;
	
	public RenderRunnable() {}
	
	public RenderRunnable(boolean replace) {
		this.replace = replace;
	}
	
    public boolean isReplaceRender() {
        return replace;
    }

    public void setReplaceRender(boolean replace) {
        this.replace = replace;
    }
	
    public void run(Graphics2D g2d) {}
}