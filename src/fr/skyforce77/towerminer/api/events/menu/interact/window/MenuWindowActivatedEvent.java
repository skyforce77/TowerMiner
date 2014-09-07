package fr.skyforce77.towerminer.api.events.menu.interact.window;

import fr.skyforce77.towerminer.api.events.menu.interact.MenuWindowEvent;
import fr.skyforce77.towerminer.menus.Menu;

import java.awt.event.WindowEvent;

public class MenuWindowActivatedEvent extends MenuWindowEvent {

    public MenuWindowActivatedEvent(Menu menu, WindowEvent event) {
        super(menu, event);
    }
}
