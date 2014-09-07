package fr.skyforce77.towerminer.api.events.menu.interact;

import fr.skyforce77.towerminer.api.events.menu.MenuEvent;
import fr.skyforce77.towerminer.menus.Menu;

import java.awt.event.MouseEvent;

public class MenuMouseEvent extends MenuEvent {

    private MouseEvent event;

    public MenuMouseEvent(Menu menu, MouseEvent event) {
        super(menu);
        this.event = event;
    }

    public MouseEvent getRawEvent() {
        return event;
    }
}
