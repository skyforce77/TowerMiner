package fr.skyforce77.towerminer.api.events.menu;

import java.util.ArrayList;

import fr.skyforce77.towerminer.menus.Menu;
import fr.skyforce77.towerminer.render.RenderRunnable;

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

}
