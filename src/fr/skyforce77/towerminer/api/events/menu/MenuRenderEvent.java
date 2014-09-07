package fr.skyforce77.towerminer.api.events.menu;

import fr.skyforce77.towerminer.menus.Menu;

import java.awt.*;

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

    public class RenderRunnable {
        public void run(Graphics2D g2d) {
        }
    }
}
