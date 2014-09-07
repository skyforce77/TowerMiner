package fr.skyforce77.towerminer.api.events.menu.interact;

import fr.skyforce77.towerminer.api.events.menu.MenuEvent;
import fr.skyforce77.towerminer.menus.Menu;

import java.awt.event.WindowEvent;

public class MenuWindowEvent extends MenuEvent {

    private WindowEvent event;

    public MenuWindowEvent(Menu menu, WindowEvent event) {
        super(menu);
        this.event = event;
    }

    public WindowEvent getRawEvent() {
        return event;
    }
}
