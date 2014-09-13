package fr.skyforce77.towerminer.api.events.menu;

import java.awt.Graphics2D;
import java.util.ArrayList;

import fr.skyforce77.towerminer.menus.Menu;

public class MenuRenderEvent extends MenuEvent {

    private ArrayList<RenderRunnable> renders = new ArrayList<>();
    private ArrayList<RenderRunnable> replacerenders = new ArrayList<>();

    public MenuRenderEvent(Menu menu) {
        super(menu);
    }

    public ArrayList<RenderRunnable> getRenders() {
        return renders;
    }
    
    public ArrayList<RenderRunnable> getReplaceRenders() {
        return replacerenders;
    }

    public void addRender(RenderRunnable render) {
    	if(render.isReplaceRender()) {
    		replacerenders.add(render);
    	} else {
    		renders.add(render);
    	}
    }
    
    @Deprecated
    public void setRender(RenderRunnable render) {
    	addRender(render);
    }

    public static class RenderRunnable {
    	
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
}
