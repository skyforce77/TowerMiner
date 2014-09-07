package fr.skyforce77.towerminer.api.events.menu;

import java.awt.Graphics2D;

import fr.skyforce77.towerminer.menus.Menu;

public class MenuRenderEvent extends MenuEvent {

    private RenderRunnable render;
    private boolean replace = false;

    public MenuRenderEvent(Menu menu) {
        super(menu);
    }

    public RenderRunnable getRender() {
        return render;
    }

    public void setRender(RenderRunnable render) {
        this.render = render;
    }

    public boolean isReplaceRender() {
        return replace;
    }

    public void setReplaceRender(boolean replace) {
        this.replace = replace;
    }

    public static class RenderRunnable {
        public void run(Graphics2D g2d) {}
    }
}
